package laba5.client;

import laba5.common.Configuration;
import laba5.common.commands.IClientSideCommand;
import laba5.common.commands.ICommand;
import laba5.common.commands.IMultiLineCommand;
import laba5.common.commands.IServerSideCommand;
import laba5.common.dataExchanging.Request;
import laba5.common.dataExchanging.Response;
import laba5.common.exceptions.CommandNotFound;
import laba5.common.exceptions.UnplannedAppTermination;
import laba5.client.input.IIOManager;
import laba5.common.model.User;
import laba5.server.logic.CommandManager;
import laba5.server.storage.CommandsListStoragingManager;
import laba5.server.storage.OnCrashStorageWriter;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Класс, объединяющий все клиентские модули.
 * @author Homoursus
 * @version 1.1
 */
public class Client {
    public class UserAuthorizer{
        private final User user;
        private final Request userRequest;

        public UserAuthorizer(){
            ioManager.printMessage("Введите свой логин: ", false);

            String username = null;
            String password = null;
            while (username == null){
                String name = ioManager.getRawInput();
                if (!(name == null || name.isEmpty()) ){
                    username = name.split(" ")[0];
                }
            }

            while (password == null){
                String pass = ioManager.getPasswordHash();
                if (!(pass == null || pass.isEmpty()) ){
                    password = pass.split(" ")[0];
                }
            }
            this.user = new User(username, password);
            userRequest = new Request(null, user,null, false, null);
        }

        public Response getUserResponse(){
            try {
                return communicateWithServer(userRequest);
            } catch (IOException e){
                e.printStackTrace();
                return null;
            }
        }
        public User getUser(){
            return user;
        }
    }

    /**
     * Менеджер ввода вывода.
     * @see IIOManager
     * */
    private final IIOManager ioManager;

    /**
     * Менеджер управления команд.
     * @see CommandManager
     * */
    private final CommandManager commandManager;

    /**
     * ArrayList последних использованных команд.
     * */
    private final ArrayList<String> lastUsedCommands;

    /**
     * Флаг состояния, показывающий, жив ли клиент.
     * */
    private boolean isClientAlive = true;

    /**
     * Название файла, в котором хранятся последние использованные команды.
     * */
    private final String commandsFileName;

    /**
     *
     */
    private final CommandsListStoragingManager lastSessionUserInputStoragingManager;

    private static final int MAX_RECONNECT_ATTEMPTS = 5;
    private static final int RECONNECT_DELAY_MS = 5000;
    private static final int SOCKET_TIMEOUT_MS = 1000;

    /**
     * Конструктор клиентов. Инициализирует всех его менеджеров.
     * @param ioManager класс-реализация менеджера управления вводом-выводом.
     * @param commandsFileName название файла, в котором будут храниться последние 15 использованных команды.
     * */
    public Client(IIOManager ioManager, String commandsFileName,
               String emergencyFileName, CommandManager commandManager) {
        this.ioManager = ioManager;
        this.lastUsedCommands = new CommandsListStoragingManager(commandsFileName).readFromStorage(ioManager);
        this.commandsFileName = commandsFileName;
        this.lastSessionUserInputStoragingManager = new CommandsListStoragingManager(emergencyFileName);
        this.commandManager = commandManager;
    }

    private Response communicateWithServer(Request request) throws IOException {
        InetAddress host = InetAddress.getLocalHost();
        //InetAddress host = InetAddress.getByName("103.90.75.212");
        //InetAddress host = InetAddress.getByName("se.ifmo.ru");
        int port = Configuration.SERVER_PORT;
        int attempts = 0;

        while (attempts < MAX_RECONNECT_ATTEMPTS) {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(host, port), SOCKET_TIMEOUT_MS);
                socket.setSoTimeout(SOCKET_TIMEOUT_MS);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream sizeCheckBos = new ObjectOutputStream(bos);
                sizeCheckBos.writeObject(request);
                ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
                //System.out.println(Arrays.toString(bos.toByteArray()));
                int objectSize = bos.toByteArray().length;

                os.writeInt(objectSize);
                os.flush();

                os.writeObject(request);
                os.flush();

                //System.out.println("Объект отправлен: " + request);

                ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
                //System.out.println("Получил канал от сервера!");

                Response resp = (Response) is.readObject();

                //System.out.println(resp);
                attempts = MAX_RECONNECT_ATTEMPTS;
                return resp;
            } catch (ConnectException e) {
                attempts++;
                ioManager.printError(String.format("Сервер недоступен. Попытка %d из %d. Ожидание %d секунд...\n",
                        attempts, MAX_RECONNECT_ATTEMPTS, RECONNECT_DELAY_MS / 1000));
                try {
                    TimeUnit.MILLISECONDS.sleep(RECONNECT_DELAY_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Прерывание при ожидании переподключения", ie);
                }
            } catch (SocketTimeoutException e) {
                attempts++;
                ioManager.printError(String.format("Превышено время ожидания ответа от сервера. Попытка %d из %d\n",
                        attempts, MAX_RECONNECT_ATTEMPTS));
            } catch (ClassNotFoundException e) {
                ioManager.printError("Ошибка при десериализации ответа от сервера. Пожалуйста, повторите запрос.\n");
                throw new IOException("Ошибка десериализации", e);
            }
        }
        throw new IOException("Не удалось установить соединение с сервером после " + MAX_RECONNECT_ATTEMPTS + " попыток");
    }

    /**
     * Метод, запускающий клиент.
     */
    public void run(){
        try {
            String userInput;
            ArrayList<String> lastSessionUserInput;
            ioManager.printMessage("Введите help для получения списка доступных команд.\n", false);
            lastSessionUserInput = lastSessionUserInputStoragingManager.readFromStorage(ioManager);
            if (!lastSessionUserInput.isEmpty()) {
                ioManager.printMessage("Последняя сессия была завершена некорректно. Хотите вернуться к ней?\n" +
                        "да - вернуться к старой сессии \nкакой-либо другой набор символов - запустить новую сессию\n", false);
                String answer = ioManager.getRawInput();
                if (answer != null && answer.equalsIgnoreCase("да")) {
                    ioManager.addCommandsToSimulator(lastSessionUserInput);
                }
            }
            boolean isAutharized = false;
            UserAuthorizer userauth = new UserAuthorizer();
            while (!isAutharized) {
                Response userResp = userauth.getUserResponse();
                ioManager.printMessage(userResp.responseClaster().message(), false);

                if (!((userResp.statusCode() == -1) || (userResp.statusCode() == 404) || (userResp.statusCode() == 401))) {
                    //System.out.println(userResp.statusCode());
                    userauth.getUser().id(userResp.statusCode());
                    isAutharized = true;
                }
                else{
                    userauth = new UserAuthorizer();
                }
            }

            while (isClientAlive) {
                try {
                    lastSessionUserInput = ioManager.getLastSessionUserInput();
                    String[] splittedInput;

                    userInput = ioManager.getRawInput().toLowerCase();
                    splittedInput = userInput.split(" ");

                    String[] args = new String[splittedInput.length - 1];
                    if (args.length != 0) {
                        System.arraycopy(splittedInput, 1, args, 0, args.length);
                    }

                    ICommand command = commandManager.getCommandByName(splittedInput[0]);
                    ArrayDeque<Object> addInf = new ArrayDeque<>();
                    boolean haveAdditionalInf = false;

                    if (command instanceof IMultiLineCommand) {
                        ((IMultiLineCommand) command).getAdditionalUserInput(ioManager);
                        haveAdditionalInf = true;
                    }
                    if (command instanceof IClientSideCommand) {
                        ((IClientSideCommand) command).execute(this, args);
                    }
                    else if (command instanceof IServerSideCommand) {
                        try {
                            Response response = communicateWithServer(new Request((IServerSideCommand) command, userauth.getUser(), args, haveAdditionalInf, addInf));
                            ioManager.printMessage(response.responseClaster().message(), response.responseClaster().willBeInQuiteMode());
                        } catch (IOException e) {
                            ioManager.printError("Ошибка при общении с сервером: " + e.fillInStackTrace() + "\n");
                        }
                    }

                    this.lastUsedCommands.add(splittedInput[0]);
                } catch (CommandNotFound | NullPointerException e) {
                    ioManager.printError("Команда не найдена! Введите help для получения списка доступных команд.\n");
                } catch (NumberFormatException e) {
                    ioManager.printError("Неверный ввод числового параметра!\n");
                } catch (Exception e) {
                    ioManager.printError("Произошла непредвиденная ошибка! Отправьте создателю файл краш-репорт!\n");
                    try {
                        OnCrashStorageWriter.write(lastSessionUserInput, e);
                    } catch (IOException e1) {
                        ioManager.printError("Произошла ошибка при записи краш-репорта!\n");
                    }
                }
            }
        }
        catch (UnplannedAppTermination e) {
            ioManager.printMessage(e.getMessage(), false);
            turnOffClient();
        }
    }

    /**
     * Метод, возвращающий используемый менеджер управления командами.
     * */
    public CommandManager getCommandManager() {
        return commandManager;
    }

    /**
     * Метод, возвращающий используемый менеджер управления вводом-выводом.
     * */
    public IIOManager getIoManager() {
        return ioManager;
    }

    /**
     * Метод, возвращающий ArrayList последних использованных команд.
     */
    public ArrayList<String> getLastUsedCommands() {
        return lastUsedCommands;
    }

    /**
     * Метод, используемый для прекращения работы приложения.
     */
    public void turnOffClient(){
        new CommandsListStoragingManager(commandsFileName).writeToStorage(lastUsedCommands, true);
        lastSessionUserInputStoragingManager.writeToStorage(new ArrayList<>(), false);
        isClientAlive = false;
    }
}

