package laba5.server;

import laba5.common.Configuration;
import laba5.common.dataExchanging.Request;
import laba5.common.dataExchanging.Response;
import laba5.common.dataExchanging.ResponseClaster;
import laba5.server.logging.IServerLogger;
import laba5.server.logic.CollectionManager;
import laba5.common.model.Dragon;
import laba5.server.storage.IModelStorageManager;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Класс, объединяющий все серверные модули.
 * @author Homoursus
 * @version 0.1
 */
public class Server {

    private static final Map<SocketChannel, Queue<Object>> clientResponseQueues = new ConcurrentHashMap<>();
    private static final Map<SocketChannel, ByteBuffer> clientRequestBuffers = new ConcurrentHashMap<>();
    private static final Map<SocketChannel, Integer> expectedSizes = new ConcurrentHashMap<>();
    /**
     * Менеджер управления коллекцией.
     * @see CollectionManager
     * */
    private final CollectionManager<Dragon> collectionManager;

    /**
     * Менеджер работы с записью коллекции в хранилище.
     * @see IModelStorageManager
     * */
    private final IModelStorageManager storageManager;

    /**
     * Флаг состояния, показывающий, включен ли сервер.
     * */
    private boolean isServerRunning = true;

    private static final int BUFFER_SIZE = 4096; // Увеличенный размер буфера для больших объектов

    /**
     * Конструктор сервера. Инициализирует всех серверных менеджеров.
     * @param storageManager класс-реализация менеджера управления хранением коллекции.
     * */
    public Server(IModelStorageManager storageManager, IServerLogger logger) {
        this.storageManager = storageManager;
        this.collectionManager = new CollectionManager<>(new LinkedList<>());
        Dragon.setIdGenerator(storageManager.getNextID());
        collectionManager.setCollection(storageManager.readFromStorage(logger));
        Collections.sort(collectionManager.getCollection());
    }

    /**
     * Метод, запускающий сервер.
     */
    public void run() throws IOException {
        System.out.println("Server starting...");
        Selector selector = Selector.open();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(Configuration.SERVER_PORT));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (isServerRunning) {
            try {
                selector.select(100);
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keys = selectedKeys.iterator();

                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if (!key.isValid()) {
                        continue;
                    }

                    if (key.isAcceptable()) {
                        acceptClient(selector, serverChannel);
                    } else if (key.isReadable()) {
                        readFromClient(key);
                    } else if (key.isWritable()) {
                        writeToClient(key);
                    }
                }
            } catch (IOException e) {
                System.err.println("Ошибка при обработке соединения: " + e.getMessage());
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

    private void acceptClient(Selector selector, ServerSocketChannel serverChannel) throws IOException {
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
        clientResponseQueues.put(clientChannel, new LinkedList<>());
        clientRequestBuffers.put(clientChannel, ByteBuffer.allocate(BUFFER_SIZE));
        System.out.println("Client connected: " + clientChannel.getRemoteAddress());
    }

    private void readFromClient(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = clientRequestBuffers.get(clientChannel);
        System.out.println(Arrays.toString(buffer.array()));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int objectSize;
        clientChannel.read(buffer);
        System.out.println(Arrays.toString(buffer.array()));
        int bytesRead = clientChannel.read(buffer);
        if (bytesRead == -1) {
            closeClientConnection(clientChannel, key);
            return;
        }

        buffer.flip();

        // Если мы еще не знаем размер объекта, считываем его
        if (!expectedSizes.containsKey(clientChannel)) {
            if (buffer.remaining() < 10) {
                // Недостаточно данных для чтения размера
                buffer.compact();
                return;
            }

            // Считываем размер объекта
            buffer.position(6);
            objectSize = buffer.getInt();
            expectedSizes.put(clientChannel, objectSize);
        }

        int expectedSize = expectedSizes.get(clientChannel);
        System.out.println(expectedSize);
        ByteBuffer objectBuffer = ByteBuffer.allocate(expectedSize);

        for (int i = 0; i < expectedSize-4; i++) {
            byte b = buffer.get();
            objectBuffer.put(b);
        }

        if (objectBuffer.position() < expectedSize-4) {
            // Объект еще не полностью получен
            buffer.compact();
            return;
        }

        // Объект полностью получен, десериализуем его
        objectBuffer.flip();
        //System.out.println(Arrays.toString(objectBuffer.array()));

        try {
            byte[] result = new byte[objectBuffer.array().length + 4];
            System.arraycopy(new byte[]{-84, -19, 0, 5}, 0, result, 0,4);
            System.arraycopy(objectBuffer.array(), 0, result, 4,objectBuffer.array().length);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(result));
            Request request = (Request) ois.readObject();
            System.out.println("Получен запрос от " + clientChannel.getRemoteAddress() + ": " + request);

            Response response = request.command().execute(this, request.args());
            System.out.println(response);

            clientResponseQueues.get(clientChannel).add(response);
            key.interestOps(SelectionKey.OP_WRITE);
        } catch (ClassNotFoundException e) {
            System.err.println("Ошибка при десериализации запроса: " + e.getMessage());
            e.printStackTrace();
            closeClientConnection(clientChannel, key);
        }
    }

    private void writeToClient(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        Queue<Object> queue = clientResponseQueues.get(clientChannel);

        if (!queue.isEmpty()) {
            Object data = queue.poll();
            System.out.println("Ответ клиенту: " + data);
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.flush();
                oos.writeObject(data);

                ByteBuffer buffer = ByteBuffer.wrap(bos.toByteArray());
                clientChannel.write(buffer);

                System.out.println("Данные клиенту отправил");

                if (!queue.isEmpty()) {
                    key.interestOps(SelectionKey.OP_READ);
                }
                else {
                    closeClientConnection(clientChannel, key);
                }
            } catch (IOException e) {
                System.err.println("Ошибка при отправке ответа клиенту: " + e.getMessage());
                e.printStackTrace();
                closeClientConnection(clientChannel, key);
            }
        }
    }

    private void closeClientConnection(SocketChannel clientChannel, SelectionKey key) throws IOException {
        clientResponseQueues.remove(clientChannel);
        clientRequestBuffers.remove(clientChannel);
        System.out.println("Клиент отключился: " + clientChannel.getRemoteAddress());
        clientChannel.finishConnect();
        clientChannel.close();
        key.cancel();

    }

    /**
     * Метод, возвращающий используемый менеджер управления коллекцией.
     * */
    public CollectionManager<Dragon> getCollectionManager() {
        return collectionManager;
    }

    /**
     * Метод, возвращающий используемый менеджер управления командами.
     * */
    public IModelStorageManager getStorageManager() {
        return storageManager;
    }

    /**
     * Метод, используемый для прекращения работы приложения.
     */
    public void turnOffServer(){
        isServerRunning = false;
    }
}


