package laba5;

import laba5.commands.*;
import laba5.exceptions.CommandNotFound;
import laba5.exceptions.UnplannedAppTermination;
import laba5.input.IIOManager;
import laba5.logic.CollectionManager;
import laba5.logic.CommandManager;
import laba5.model.Dragon;
import laba5.storage.CommandsListStoragingManager;
import laba5.storage.IModelStorageManager;
import laba5.storage.OnCrashStorageWriter;

import java.io.IOException;
import java.util.*;

/**
 * Класс, объединяющий все модули приложения.
 * @author Homoursus
 * @version 1.1
 */
public class App {
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
     * ArrayList последних использованных команд.
     * */
    private final ArrayList<String> lastUsedCommands;

    /**
     * Флаг состояния, показывающий, включено ли приложение.
     * */
    private boolean isAppWorking;

    /**
     * Название файла, в котором хранятся последние использованные команды.
     * */
    private final String commandsFileName;

    /**
     *
     */
    private final CommandsListStoragingManager lastSessionUserInputStoragingManager;

    {
        isAppWorking = true;
    }

    /**
     * Конструктор приложения. Инициализирует всех менеджеров приложения.
     * @param ioManager класс-реализация менеджера управления вводом-выводом.
     * @param storageManager класс-реализация менеджера управления хранилищем.
     * @param commandsFileName название файла, в котором будут храниться последние 15 использованных команды.
     * */
    public App(IModelStorageManager storageManager, IIOManager ioManager, String commandsFileName,
               String emergencyFileName, CommandManager commandManager) {
        this.ioManager = ioManager;
        this.storageManager = storageManager;
        this.lastUsedCommands = new CommandsListStoragingManager(commandsFileName).readFromStorage(ioManager);
        this.commandsFileName = commandsFileName;
        this.lastSessionUserInputStoragingManager = new CommandsListStoragingManager(emergencyFileName);
        this.collectionManager = new CollectionManager<>(new LinkedList<>());

        Dragon.setIdGenerator(storageManager.getNextID(ioManager));
        collectionManager.setCollection(storageManager.readFromStorage(ioManager));
        Collections.sort(collectionManager.getCollection());
        this.commandManager = commandManager;
    }


    /**
     * Метод, запускающий приложение.
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
            while (isAppWorking) {
                try {
                    lastSessionUserInput = ioManager.getLastSessionUserInput();
                    String[] splittedInput;

                    userInput = ioManager.getRawInput().toLowerCase();
                    splittedInput = userInput.split(" ");

                    String[] args = new String[splittedInput.length - 1];
                    if (args.length != 0) {
                        System.arraycopy(splittedInput, 1, args, 0, args.length);
                    }
                    commandManager.execute(this, splittedInput[0], args);
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
            turnOffApp();
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
     * Метод, возвращающий используемый менеджер управления командами.
     * */
    public IModelStorageManager getStorageManager() {
        return storageManager;
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
    public void turnOffApp(){
        new CommandsListStoragingManager(commandsFileName).writeToStorage(lastUsedCommands, true);
        lastSessionUserInputStoragingManager.writeToStorage(new ArrayList<>(), false);
        isAppWorking = false;
    }
}

