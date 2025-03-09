package laba5;

import laba5.input.ConsoleIOManager;
import laba5.input.IIOManager;
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
 
// TODO: Различать ввод пользователя и автоматический ввод (микро доп)
// TODO: Сделать так, чтобы можно было стрелочками выбирать предыдущие команды (без ограничений по числу команд), игнорировать автоматизированный ввод команд.
// TODO: Сделать особую запись ошибок в случае если работает автоматизированный вывод команд.
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
          IIOManager ioManager = new ConsoleIOManager(new BufferedReader(new InputStreamReader(System.in)), emergencyCommands);
          IModelStorageManager storageManager = new ModelCSVStoragingManager(args[0]);
          App app = new App(storageManager, ioManager, "commandsFileName.csv", emergencyCommands);
          app.run();
      }
  }
}
