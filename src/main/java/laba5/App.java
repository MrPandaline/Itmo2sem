package laba5;

import laba5.commands.*;
import laba5.exceptions.CommandNotFound;
import laba5.input.IIOManager;
import laba5.logic.CollectionManager;
import laba5.logic.CommandManager;
import laba5.model.Dragon;
import laba5.storage.CommandsListStoragingManager;
import laba5.storage.IModelStorageManager;
import laba5.storage.OnCrashStorageWriter;

import java.io.IOException;
import java.util.*;
import java.awt.event.KeyListener;

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
    public App(IModelStorageManager storageManager, IIOManager ioManager, String commandsFileName, String emergencyFileName) {
        this.ioManager = ioManager;
        this.storageManager = storageManager;
        this.lastUsedCommands = new CommandsListStoragingManager(commandsFileName).readFromStorage(ioManager);
        this.commandsFileName = commandsFileName;
        this.lastSessionUserInputStoragingManager = new CommandsListStoragingManager(emergencyFileName);
        this.collectionManager = new CollectionManager<>(new LinkedList<>());

        Dragon.setIdGenerator(storageManager.getNextID(ioManager));
        collectionManager.setCollection(storageManager.readFromStorage(ioManager));
        Collections.sort(collectionManager.getCollection());
        this.commandManager = this.buildCommandManager();
    }

    /**
     * Метод, инициализирующий менеджер управления команд.
     * */
    private CommandManager buildCommandManager() {
        CommandManager commandManager = new CommandManager();
        commandManager.addCommand("help", new Help());
        commandManager.addCommand("info", new Info());
        commandManager.addCommand("show", new Show());
        commandManager.addCommand("add", new Add());
        commandManager.addCommand("update", new Update());
        commandManager.addCommand("remove_by_id", new RemoveById());
        commandManager.addCommand("clear", new Clear());
        commandManager.addCommand("save", new Save());
        commandManager.addCommand("execute_script", new ExecuteScript());
        commandManager.addCommand("exit", new Exit());
        commandManager.addCommand("remove_head", new RemoveHead());
        commandManager.addCommand("remove_greater", new RemoveGreater());
        commandManager.addCommand("history", new History());
        commandManager.addCommand("group_counting_by_name", new GroupCountingByName());
        commandManager.addCommand("filter_greater_than_type", new FilterGreaterThanType());
        commandManager.addCommand("print_field_descending_killer", new PrintFieldDescendingKiller());
        return commandManager;
    }

    /**
     * Метод, запускающий приложение.
     */
    public void run(){
        String userInput;
        ArrayList<String> lastSessionUserInput;
        ioManager.writeMessage("Введите help для получения списка доступных команд.\n", false);
        lastSessionUserInput = lastSessionUserInputStoragingManager.readFromStorage(ioManager);
        if (!lastSessionUserInput.isEmpty()){
            ioManager.writeMessage("Последняя сессия была завершена некорректно. Хотите вернуться к ней?\n"+
                    "да - вернуться к старой сессии\n"+ "какой-либо другой набор символов - запустить новую сессию\n", false);
            String answer = ioManager.getRawInput();
            if (answer != null && answer.equalsIgnoreCase("да")){
                ioManager.addCommandsToSimulator(lastSessionUserInput);
            }
        }
        while (isAppWorking) {
            try{
                lastSessionUserInput = ioManager.getLastSessionUserInput();
                String[] splittedInput;

                userInput = ioManager.getRawInput().toLowerCase();
                splittedInput = userInput.split(" ");

                //KeyListener keyListener = new KeyListener(

                String[] args = new String[splittedInput.length - 1];
                if( args.length != 0) {
                    System.arraycopy(splittedInput, 1, args, 0, args.length);
                }
                commandManager.execute(this ,splittedInput[0] ,args);
                this.lastUsedCommands.add(splittedInput[0]);
            }
            catch(CommandNotFound | NullPointerException e) {
                ioManager.writeMessage("Команда не найдена! Введите help для получения списка доступных команд.\n", false);
            }
            catch (NumberFormatException e) {
                ioManager.writeMessage("Неверный ввод числового параметра!\n", false);
            }
            catch (Exception e){
                ioManager.writeMessage("Произошла непредвиденная ошибка! Отправьте создателю файл краш-репорт!\n", false);
                try {
                    OnCrashStorageWriter.write(lastSessionUserInput, e);
                } catch (IOException e1) {
                    ioManager.writeMessage("Произошла ошибка при записи краш-репорта!\n", false);
                }
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

