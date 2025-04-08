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

    private static final Set<String> executedScripts = new HashSet<>();

    @Override
    public String getDescription() {
        return "Позволяет считать и исполнить скрипт из указанного файла. \nТребует ввода названия .txt файла.";
    }

    @Override
    public void execute(Client client, String[] args) {
        IIOManager ioManager = client.getIoManager();
        ArrayList<String> commands = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                if (line.startsWith("execute_script")){
                    String[] parts = line.split(" ", 2);
                    if (parts.length > 1){
                        if (executedScripts.contains(parts[1])){
                            throw new RecursionDetected();
                        }
                       executedScripts.add(parts[1]);
                    }
                    executedScripts.add(line);
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
