package ru.itmo.java.homoursus.laba5.input;

import java.io.BufferedReader;
import java.io.IOException;

public class ConsoleInputManager implements IInputManager {
    BufferedReader reader;

    public ConsoleInputManager(BufferedReader reader) {
        this.reader = reader;
    }
    public String getInput(){
        String input = "";
        try {
            input = this.reader.readLine().toLowerCase();
        }
        catch (IOException e) {e.printStackTrace();}
        return input;
    }
}
