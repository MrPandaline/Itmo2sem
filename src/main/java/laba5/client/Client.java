package laba5.client;

import laba5.common.Configuration;
import laba5.common.commands.IClientSideCommand;
import laba5.common.commands.ICommand;
import laba5.common.commands.IMultiLineCommand;
import laba5.common.commands.IServerSideCommand;
import laba5.common.dataExchanging.Request;
import laba5.common.exceptions.CommandNotFound;
import laba5.common.exceptions.UnplannedAppTermination;
import laba5.client.input.IIOManager;
import laba5.server.logic.CommandManager;
import laba5.server.storage.CommandsListStoragingManager;
import laba5.server.storage.OnCrashStorageWriter;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Класс, объединяющий все клиентские модули.
 * @author Homoursus
 * @version 0.1
 */
public class Client {
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

    private Object communicateWithServer(Request request) throws IOException {
        InetAddress host = InetAddress.getLocalHost();
        int port = Configuration.SERVER_PORT;
        while (true){
            try (Socket socket = new Socket(host, port)){
                ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
                os.writeObject(request);
                return is.readObject();
            } catch (ConnectException e) {
                ioManager.printError("Сервер недоступен. Пытаюсь переподключиться через 5 секунд.\n");
            } catch (ClassNotFoundException e) {
                ioManager.printError("Сервер втирает нам какую-то дичь. " +
                        "Пожалуйста не вините его и повторите последний ввод\n");
            } try {
                Thread.sleep(5000); // TODO: Понять чего он ругается.
            } catch (InterruptedException ex) {
                ioManager.printError("Не получается установить задержку между переподключениями.");
            }
        }
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
                        addInf = ((IMultiLineCommand) command).getAdditionalUserInput(ioManager);
                        haveAdditionalInf = true;
                    }
                    if (command instanceof IClientSideCommand) {
                        ((IClientSideCommand) command).execute(this, args);
                    }
                    if (command instanceof IServerSideCommand) {
                        Object response = communicateWithServer(new Request(command, args, haveAdditionalInf, addInf));
                        ioManager.printMessage((String) response, true);
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

