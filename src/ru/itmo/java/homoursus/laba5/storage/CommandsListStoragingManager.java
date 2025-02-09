package ru.itmo.java.homoursus.laba5.storage;

import ru.itmo.java.homoursus.laba5.input.IIOManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
public class CommandsListStoragingManager {
    public static void writeToStorage(ArrayList<String> list){
        try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream("latestUsedCommands.csv"), StandardCharsets.UTF_8)) {
            for (int i = Math.max(list.size()-16, 0); i < list.size(); i++) {
                osw.write(list.get(i) + ";");
            }
        } catch (IOException e) {
        e.printStackTrace();
        }
    }

    public static ArrayList<String> readFromStorage(IIOManager ioManager){
        ArrayList<String> arrayList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("latestUsedCommands.csv"))) {
            for (String s: br.readLine().split(";")) {
                arrayList.add(s);
            }
        } catch (FileNotFoundException e) {
            ioManager.writeMessage("Файл не найден!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return arrayList;
    }
}
