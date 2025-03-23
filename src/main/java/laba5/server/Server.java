package laba5.server;

import laba5.common.Configuration;
import laba5.common.commands.IServerSideCommand;
import laba5.common.dataExchanging.Request;
import laba5.common.dataExchanging.Response;
import laba5.server.logging.IServerLogger;
import laba5.server.logic.CollectionManager;
import laba5.common.model.Dragon;
import laba5.server.storage.IModelStorageManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Класс, объединяющий все серверные модули.
 * @author Homoursus
 * @version 0.1
 */
public class Server {

    private static final Map<SocketChannel, Queue<Object>> clientQueues = new ConcurrentHashMap<>();
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
            selector.select(); // Блокируется до появления событий
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove();

                if (key.isAcceptable()) {
                    acceptClient(selector, serverChannel);
                } else if (key.isReadable()) {
                    readFromClient(key);
                } else if (key.isWritable()) {
                    writeToClient(key);
                }
            }
        }
    }

    private void acceptClient(Selector selector, ServerSocketChannel serverChannel) throws IOException {
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
        clientQueues.put(clientChannel, new LinkedList<>());
        System.out.println("Client connected.");
    }

    private void readFromClient(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = clientChannel.read(buffer);

        if (bytesRead == -1) { // Клиент отключился
            clientQueues.remove(clientChannel);
            clientChannel.close();
            key.cancel();
            System.out.println("Клиент отключился!");
            return;
        }

        buffer.flip();
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buffer.array()));
        try {
            Request request = (Request) ois.readObject();
            System.out.println("Получен запрос: " + request);

            Response response = ((IServerSideCommand) request.command()).execute(this, request.args());

            clientQueues.get(clientChannel).add(response);
            key.interestOps(SelectionKey.OP_WRITE); // Переключаемся на запись
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void writeToClient(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        Queue<Object> queue = clientQueues.get(clientChannel);

        if (!queue.isEmpty()) {
            Object data = queue.poll();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(data);
            oos.flush();

            ByteBuffer buffer = ByteBuffer.wrap(bos.toByteArray());
            clientChannel.write(buffer);

            if (queue.isEmpty()) {
                key.interestOps(SelectionKey.OP_READ); // Переключаемся обратно на чтение
            }
        }
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


