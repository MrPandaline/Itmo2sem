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
import java.util.HashSet;
import java.util.Set;

/**
 * Класс команды, реализующей выполнение скрипта из файла. Название файла передаётся вместе с командой.
 * Расширение файла .txt
 * Каждая команда вместе с аргументами должна быть указана с новой строки.
 * @author Homoursus
 * @version 1.2
 */
public class ExecuteScript implements IClientSideCommand{

    private static final HashMap<String, Integer> executedScripts = new HashMap<>();
    private int recursionLimit = 1;

    @Override
    public String getDescription() {
        return "Позволяет считать и исполнить скрипт из указанного файла. \nТребует ввода названия .txt файла. " +
                "После названия файла можно ввести число - максимальную глубину рекурсии.";
    }

    @Override
    public void execute(Client client, String[] args) {
        IIOManager ioManager = client.getIoManager();
        ArrayList<String> commands = new ArrayList<>();

        if (args.length == 0) {
            client.getIoManager().printError("Ошибка! Вы не передали название скрипта!");
        }
        else {
            if (recursionLimit == 1 && args.length > 1) {
                recursionLimit = Integer.parseInt(args[1]);
            }
            try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    if (line.startsWith("execute_script")) {
                        String[] parts = line.split(" ", 4);
                        if (parts.length > 1) {
                            if (!executedScripts.containsKey(parts[1])) {
                                executedScripts.put(parts[1], 0);
                            }
                            if (executedScripts.get(parts[1]) > recursionLimit) {
                                executedScripts.clear();
                                throw new RecursionDetected();
                            } else {
                                executedScripts.put(parts[1], executedScripts.get(parts[1]) + 1);
                            }
                        }
                    }
                    commands.add(line);
                }
                ioManager.addCommandsToSimulator(commands);

            } catch (FileNotFoundException e) {
                client.getIoManager().printError("Файл со скриптом не найден!");
            } catch (IOException e) {
                client.getIoManager().printError("Что-то пошло не так... Повторите ввод. \n");
            }
        }
    }
}
