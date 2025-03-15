package laba5;

import laba5.commands.*;
import laba5.input.ConsoleIOManager;
import laba5.input.IIOManager;
import laba5.logic.CommandManager;
import laba5.storage.ModelCSVStoragingManager;
import laba5.storage.IModelStorageManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;



/**
 * Требования:
 * Паттерн команда
 * Ссылка на гит, если хотите issues.
 * Mavel/Gradle
 * */

/**
 * Класс - точка входа в программу.
 * В классе возможна смена используемого менеджера ввода-вывода и менеджера сохранения коллекции в память.
 * @author Homoursus
 * @version 1.0
 */

// TODO: Добавить, проверку на то, является ли файл доступным для записи (для всех файлов)
// TODO: Сделать восстановление сессии через сериализацию модельки, без сохранения и воспроизведения последних команд.
// TODO: Разграничить IOManager и Validator, сделать валидацию модельки при чтении из файла.
// TODO: Придумать как сделать quite вывод нормально.

public class Main {
  public static void main(String[] args) {
      if (args.length == 0) {
          System.out.println("для Корректной работы добавьте название .csv файла при запуске приложения!");
      }
      else {
          String emergencyCommands = "emergencyCommands.csv";
          CommandManager commandManager = new CommandManager();
          commandManager.addCommands(new Help(),new Info(), new Show(), new Add(),new Update(),
                  new RemoveById(), new Clear(), new Save(), new ExecuteScript(),
                  new Exit(), new RemoveHead(), new RemoveGreater(), new History(),
                  new GroupCountingByName(), new FilterGreaterThanType(),
                  new PrintFieldDescendingKiller());
          
          IIOManager ioManager = new ConsoleIOManager(commandManager.getCommandNames(), emergencyCommands);
          IModelStorageManager storageManager = new ModelCSVStoragingManager(args[0]);
          App app = new App(storageManager, ioManager, "commandsFileName.csv", emergencyCommands, commandManager);
          app.run();
      }
  }
}
