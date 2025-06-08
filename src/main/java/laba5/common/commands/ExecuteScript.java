package laba5.common.commands;

import laba5.client.Client;
import laba5.client.input.IIOManager;
import laba5.common.exceptions.RecursionDetected;
import laba5.common.exceptions.UnplannedAppTermination;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Класс команды, реализующей выполнение скрипта из файла. Название файла передаётся вместе с командой.
 * Расширение файла .txt
 * Каждая команда вместе с аргументами должна быть указана с новой строки.
 * @author Homoursus
 * @version 1.2
 */
public class ExecuteScript implements IClientSideCommand {

    private static final HashMap<String, Integer> executedScripts = new HashMap<>();
    private int recursionLimit = 1;

    @Override
    public String getDescription() {
        return "Позволяет считать и исполнить скрипт из указанного файла. \n" +
                "Требует ввода названия .txt файла. После названия файла можно ввести число — максимальную глубину рекурсии.";
    }

    @Override
    public String execute(Client client, String[] args) {
        IIOManager ioManager = client.getIoManager();
        ArrayList<String> commands = new ArrayList<>();

        if (args.length == 0) {
            return "Ошибка: вы не передали название скрипта!";
        }

        String filename = args[0];

        // Проверка рекурсивного вызова
        if (recursionLimit == 1 && args.length > 1) {
            try {
                recursionLimit = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                return "Ошибка: глубина рекурсии должна быть целым числом.";
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                if (line.startsWith("execute_script")) {
                    String[] parts = line.split(" ", 4);
                    if (parts.length > 1) {
                        String scriptName = parts[1];
                        if (!executedScripts.containsKey(scriptName)) {
                            executedScripts.put(scriptName, 0);
                        }
                        if (executedScripts.get(scriptName) > recursionLimit) {
                            executedScripts.clear();
                            throw new RecursionDetected();
                        } else {
                            executedScripts.put(scriptName, executedScripts.get(scriptName) + 1);
                        }
                    }
                }

                commands.add(line);
            }

            ioManager.addCommandsToSimulator(commands);
            return "Скрипт успешно загружен. Команды добавлены в очередь.";

        } catch (FileNotFoundException e) {
            return "Файл со скриптом не найден: " + filename;

        } catch (IOException e) {
            return "Ошибка при чтении файла: " + e.getMessage();

        } catch (Exception e) {
            return "Произошла ошибка: " + e.getMessage();
        }
    }
}