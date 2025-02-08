package ru.itmo.java.homoursus.laba5.input;

import java.io.BufferedReader;
import java.io.IOException;


public class ConsoleIOManager implements IIOManager {
    BufferedReader reader;

    public ConsoleIOManager(BufferedReader reader) {
        this.reader = reader;
    }
    public String getInput(){
        String input = "";
        try {
            input = this.reader.readLine().toLowerCase();
            if(input.isEmpty()){
                input = null;
            }
        }
        catch (IOException e) {e.printStackTrace();}
        return input;
    }

    public void writeMessage(String message){
        System.out.print(message);
    }
}
