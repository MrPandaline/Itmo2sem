package ru.itmo.java.homoursus.laba5;

import ru.itmo.java.homoursus.laba5.input.ConsoleIOManager;
import ru.itmo.java.homoursus.laba5.input.IIOManager;
import ru.itmo.java.homoursus.laba5.storage.ModelCSVStoragingManager;
import ru.itmo.java.homoursus.laba5.storage.IModelStorageManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Класс - точка входа в программу.
 * В классе возможна смена используемого менеджера ввода-вывода и менеджена сохранения коллекции в память
 * @author Homoursus
 * @version 1.0
 */
public class Main {

  public static void main(String[] args) {
      if (args.length == 0) {
          System.out.println("для Корректной работы добавьте название .csv файла при запуске приложения!");
      }
      else {
          IIOManager ioManager = new ConsoleIOManager(new BufferedReader(new InputStreamReader(System.in)));
          IModelStorageManager storageManager = new ModelCSVStoragingManager(args[0]);
          App app = new App(storageManager, ioManager, "commandsFileName.csv");

          app.run();
      }
  }
}
