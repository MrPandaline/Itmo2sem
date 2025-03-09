package laba5.input;

import laba5.storage.CommandsListStoragingManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Класс реализующий модуль ввода-вывода через консоль.
 * @author Homoursus
 * @version 1.0
 */
public class ConsoleIOManager implements IIOManager {
    BufferedReader reader;
    /**
     * Название файла, в котором будут храниться все использованные в течение последней сессии команды.
     * */
    CommandsListStoragingManager commandStoraging;

    /**
     * Список команд, введённых пользователем, за время работы приложения.
     * Необходимы для восстановления работы после экстренного закрытия приложения.
     * */
    private final ArrayList<String> lastSessionUserInput;

    private ArrayList<String> emulatorBuffer;

    private boolean isUsingAutomatedInputNow;

    {
        lastSessionUserInput = new ArrayList<>();
        emulatorBuffer = new ArrayList<>();
    }

    public ConsoleIOManager(BufferedReader reader, String emergencyFileName) {
        this.reader = reader;
        commandStoraging = new CommandsListStoragingManager(emergencyFileName);
    }
    public String getRawInput(){
        String input = "";
        try {
            if (!emulatorBuffer.isEmpty()) {
                isUsingAutomatedInputNow = true;
                input = emulatorBuffer.remove(0);
                //writeMessage(input + "\n");
            }
            else{
                isUsingAutomatedInputNow = false;
                input = this.reader.readLine();
            }

            lastSessionUserInput.add(input);

            if (input.isEmpty()) {
                input = null;
            }
        } catch (IOException e) {
            writeMessage("Что-то пошло не так...\n", false);
        }
        commandStoraging.writeToStorage(lastSessionUserInput, false);
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
                writeMessage("Некорректный ввод! Попробуйте ещё раз.\n", false);
            }
        }
        return input;
    }

    @Override
    public <T extends Number> T getValidDigit(Function<String, T> function, Predicate<T> condition) {
        T input = null;
        while(input == null){
            T testInput = getDigit(function);
            if(condition.test(testInput)){
                input = testInput;
            }
            else {
                writeMessage("Некорректный ввод! Попробуйте ещё раз.\n", false);
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
                writeMessage("Некорректный ввод! Попробуйте ещё раз.\n", false);
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
                writeMessage("Неверно введено число! Повторите ввод.\n", false);
            }
        }
        return result;
    }

    public void writeMessage(String message, boolean willBeInQuiteMode){
        if (!isUsingAutomatedInputNow || willBeInQuiteMode) {
            System.out.print(message);
        }
    }

    @Override
    public void addCommandsToSimulator(ArrayList<String> commands) {
        emulatorBuffer = commands;
    }

    @Override
    public ArrayList<String> getLastSessionUserInput(){
        return lastSessionUserInput;
    }

    public boolean isUsingAutomatedInputNow(){
        return isUsingAutomatedInputNow;
    }
}
