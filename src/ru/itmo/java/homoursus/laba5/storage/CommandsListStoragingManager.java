package ru.itmo.java.homoursus.laba5.storage;

import ru.itmo.java.homoursus.laba5.input.IIOManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Класс, управляющий записью (чтением) последних 15 использованных команд в файл (из файла).
 * @author Homoursus
 * @version 1.1
 */
public class CommandsListStoragingManager {

    /** Название файла, в который (из которого) происходит запись (чтение) команд. */
    private final String fileName;

    /** Конструктор модуля
     * @param fileName название файла, из которого) происходит запись (чтение) команд.
     */
    public CommandsListStoragingManager(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Метод, отвечающий за запись команд в файл. Команды всегда записываются в файл <b>fileName</b>
     * @see CommandsListStoragingManager#fileName
     * @param list ArrayList использованных команд.
     */
    public void writeToStorage(ArrayList<String> list){
        try (OutputStreamWriter osw = new OutputStreamWriter(
                new FileOutputStream(fileName), StandardCharsets.UTF_8)) {
            for (int i = Math.max(list.size()-16, 0); i < list.size(); i++) {
                osw.write(list.get(i) + ";");
            }
        } catch (IOException e) {
        e.printStackTrace();
        }
    }

    /**
     * Метод, отвечающий за запись команд в файл. Команды всегда записываются в файл <b>fileName</b>
     * @see CommandsListStoragingManager#fileName
     * @param ioManager реализация менеджера ввода-вывода.
     */
    public ArrayList<String> readFromStorage(IIOManager ioManager){
        ArrayList<String> arrayList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            Collections.addAll(arrayList, br.readLine().split(";"));
        } catch (FileNotFoundException e) {
            ioManager.writeMessage("Файл, хранящий список последних команд, не найден!\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return arrayList;
    }
}
