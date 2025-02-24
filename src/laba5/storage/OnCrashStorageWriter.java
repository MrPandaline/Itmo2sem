package laba5.storage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;

/**
 * Класс, реализующий запись в краш-репорт.
 */
public class OnCrashStorageWriter {
    /**
     * Метод, реализующий запись краш-репорта при аварийном завершении программы.
     * @param e исключение, с которым завершилась программа.
     * @param lastUsedCommands список команд, которые были вызваны в последней сессии
     * */
    public static void write(ArrayList<String> lastUsedCommands, Exception e) throws IOException {
        ArrayList<String> crashLogArray = new ArrayList<>(lastUsedCommands);
        StringBuilder stackTraceBuilder = new StringBuilder();

        stackTraceBuilder.append(e).append("\n");

        for (StackTraceElement element : e.getStackTrace()) {
            stackTraceBuilder.append("\tat ").append(element).append("\n");
        }
        String stackTrace = stackTraceBuilder.toString();
        crashLogArray.add(stackTrace);
        String filename = "crash-report_" + ZonedDateTime.now().toString().substring(0, 19).replace(':', '_') +".txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for (String line : crashLogArray) {
            writer.write(line + "\n");
        }
        writer.close();
    }
}
