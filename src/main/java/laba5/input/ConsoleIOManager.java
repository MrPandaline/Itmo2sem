package laba5.input;

import laba5.storage.CommandsListStoragingManager;
import org.jline.reader.*;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Класс реализующий модуль ввода-вывода через консоль с поддержкой истории команд.
 * @author Homoursus
 * @version 2.0
 */
public class ConsoleIOManager implements IIOManager {
    private final LineReader lineReader;
    private final Terminal terminal;
    private final CommandsListStoragingManager commandStoraging;
    private final ArrayList<String> lastSessionUserInput;
    private ArrayList<String> emulatorBuffer;
    private boolean isUsingAutomatedInputNow;

    public ConsoleIOManager(BufferedReader reader, String emergencyFileName) {
        this.lastSessionUserInput = new ArrayList<>();
        this.emulatorBuffer = new ArrayList<>();
        this.commandStoraging = new CommandsListStoragingManager(emergencyFileName);
        
        try {
            this.terminal = TerminalBuilder.builder()
                    .system(true)
                    .build();
            
            this.lineReader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .variable(LineReader.HISTORY_FILE, System.getProperty("user.home") + "/.laba5_history")
                    .variable(LineReader.HISTORY_SIZE, 100)
                    .option(LineReader.Option.HISTORY_BEEP, false)
                    .option(LineReader.Option.HISTORY_IGNORE_SPACE, true)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Не удалось инициализировать терминал: " + e.getMessage());
        }
    }

    public String getRawInput() {
        String input = "";
        try {
            if (!emulatorBuffer.isEmpty()) {
                isUsingAutomatedInputNow = true;
                input = emulatorBuffer.remove(0);
            } else {
                isUsingAutomatedInputNow = false;
                input = lineReader.readLine("> ");
            }

            if (input != null && !input.trim().isEmpty()) {
                lastSessionUserInput.add(input);
                commandStoraging.writeToStorage(lastSessionUserInput, false);
            }

        } catch (UserInterruptException e) {
            // Ctrl+C
            return null;
        } catch (EndOfFileException e) {
            // Ctrl+D
            return null;
        }
        
        return input.isEmpty() ? null : input;
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
