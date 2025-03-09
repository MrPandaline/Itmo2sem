package laba5.commands;

import laba5.App;
import laba5.exceptions.CommandNotFound;
import laba5.input.IIOManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Класс команды, реализующей выполнение скрипта из файла. Название файла передаётся вместе с командой.
 * Расширение файла .txt
 * Каждая команда вместе с аргументами должна быть указана с новой строки.
 * @author Homoursus
 * @version 1.1
 */
public class ExecuteScript implements ICommand{

    @Override
    public String getDescription() {
        return "Позволяет считать и исполнить скрипт из указанного файла. \nТребует ввода названия .txt файла.";
    }

    @Override
    public void execute(App app, String[] args) {
        IIOManager ioManager = app.getIoManager();
        ArrayList<String> commands = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                commands.add(line);
            }
            ioManager.addCommandsToSimulator(commands);
        } catch (FileNotFoundException e) {
            app.getIoManager().writeMessage("Файл со скриптом не найден!", outInQuiteMode);
        } catch (IOException e) {
            app.getIoManager().writeMessage("Что-то пошло не так... Повторите ввод. \n", outInQuiteMode);
        }
    }
}
