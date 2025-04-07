package laba5;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class ClientServerApp {

    // Главный метод для запуска приложения
    public static void main(String[] args) {
        // Запускаем сервер в отдельном потоке
        Thread serverThread = new Thread(Server::start);
        serverThread.start();

        // Даём серверу время на запуск
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Запускаем клиент в основном потоке
        Client.start();
    }

    // Класс сервера
    static class Server {
        public static void start() {
            try {
                // Создаем селектор
                Selector selector = Selector.open();

                // Создаем серверный канал
                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
                serverSocketChannel.bind(new InetSocketAddress("localhost", 8080));
                serverSocketChannel.configureBlocking(false);

                // Регистрируем серверный канал на селекторе для приема новых соединений
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                System.out.println("Сервер запущен. Ожидание клиентов...");

                while (true) {
                    // Ждем события на селекторе
                    selector.select();

                    // Получаем итератор ключей с событиями
                    Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                    while (keys.hasNext()) {
                        SelectionKey key = keys.next();
                        keys.remove();

                        if (key.isAcceptable()) {
                            // Принимаем новое соединение
                            SocketChannel clientChannel = serverSocketChannel.accept();
                            clientChannel.configureBlocking(false);
                            clientChannel.register(selector, SelectionKey.OP_READ);
                            System.out.println("Клиент подключен: " + clientChannel.getRemoteAddress());
                        } else if (key.isReadable()) {
                            // Читаем данные от клиента
                            SocketChannel clientChannel = (SocketChannel) key.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            int bytesRead = clientChannel.read(buffer);

                            if (bytesRead == -1) {
                                // Клиент закрыл соединение
                                System.out.println("Клиент отключился: " + clientChannel.getRemoteAddress());
                                clientChannel.close();
                            } else {
                                buffer.flip();
                                String message = new String(buffer.array(), 0, buffer.limit());
                                System.out.println("Получено сообщение от клиента: " + message);

                                // Отправляем ответ клиенту
                                ByteBuffer responseBuffer = ByteBuffer.wrap(("Echo: " + message).getBytes());
                                clientChannel.write(responseBuffer);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Класс клиента
    static class Client {
        public static void start() {
            try (Socket socket = new Socket("localhost", 8080)) {
                System.out.println("Клиент подключен к серверу.");

                // Поток для чтения данных от сервера
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Поток для отправки данных на сервер
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

                // Поток для чтения ввода пользователя
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

                // Цикл для отправки сообщений серверу
                while (true) {
                    System.out.print("Введите сообщение для сервера: ");
                    String message = userInput.readLine();

                    // Отправляем сообщение на сервер
                    writer.println(message);

                    // Читаем ответ от сервера
                    String response = reader.readLine();
                    System.out.println("Ответ от сервера: " + response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}