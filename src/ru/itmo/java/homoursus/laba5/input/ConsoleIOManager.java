package ru.itmo.java.homoursus.laba5.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Predicate;


public class ConsoleIOManager implements IIOManager {
    BufferedReader reader;

    public ConsoleIOManager(BufferedReader reader) {
        this.reader = reader;
    }
    public String getRawInput(){
        String input = "";
        try {
            input = this.reader.readLine();
            if(input.isEmpty()){
                input = null;
            }
        }
        catch (IOException e) {
            writeMessage("Что-то пошло не так...\n");
        }
        return input;
    }

    @Override
    public String getValidRawInput(Predicate<String> condition) {
        String input = "";
        while(input.isEmpty()){
            String testInput = getRawInput();
            if(condition.test(testInput)){
                input = testInput;
            }
            else {
                writeMessage("Некорректный ввод! Попробуйте ещё раз.\n");
            }
        }
        return input;
    }

    @Override
    public <T extends Number> T getValidDigit(Function<String, T> function, Predicate<T> condition) {
        T input = null;
        while(input == null){;
            T testInput = getDigit(function);
            if(condition.test(testInput)){
                input = testInput;
            }
            else {
                writeMessage("Некорректный ввод! Попробуйте ещё раз.\n");
            }
        }
        return input;
    }

    @Override
    public String getConstantString(ArrayList<String> constants, boolean canBeNull) {
        String input = "";
        mark:
        while(input.isEmpty()){
            String testInput = getRawInput();
            for (String constant : constants){
                if(testInput == null){
                    if (canBeNull) {
                        input = null;
                        break mark;
                    }
                    else{
                        break;
                    }
                }
                if(testInput.equals(constant)){
                    input = constant;
                }
            }
            if (input.isEmpty()){
                writeMessage("Некорректный ввод! Попробуйте ещё раз.\n");
            }
        }
        return input;
    }

    @Override
    public <T extends Number> T getDigit(Function<String, T> function) {
        T result = null;
        while(result == null){
            try {
                result = function.apply(getRawInput());
            } catch (NumberFormatException | NullPointerException e) {
                writeMessage("Неверно введено число! Повторите ввод.\n");
            }
        }
        return result;
    }

    public void writeMessage(String message){
        System.out.print(message);
    }
}
