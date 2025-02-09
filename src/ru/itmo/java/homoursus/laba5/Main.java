package ru.itmo.java.homoursus.laba5;

import ru.itmo.java.homoursus.laba5.input.ConsoleIOManager;
import ru.itmo.java.homoursus.laba5.input.IIOManager;
import ru.itmo.java.homoursus.laba5.storage.ModelCSVStoragingManager;
import ru.itmo.java.homoursus.laba5.storage.IModelStorageManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

  public static void main(String[] args) {
      IIOManager ioManager = new ConsoleIOManager(new BufferedReader(new InputStreamReader(System.in)));
      IModelStorageManager storageManager = new ModelCSVStoragingManager(args[0]);
      App app = new App(storageManager, ioManager);

      app.run();
  }
}
