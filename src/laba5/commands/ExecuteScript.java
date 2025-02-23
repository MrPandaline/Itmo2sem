package laba5.commands;

import laba5.App;
import laba5.exceptions.CommandNotFound;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Класс команды, реализующей выполнение скрипта из файла. Название файла передаётся вместе с командой.
 * Расширение файла .txt
 * Каждая команда вместе с аргументами должна быть указана с новой строки.
 * @author Homoursus
 * @version 1.0
 */
public class ExecuteScript implements ICommand{
    @Override
    public String getDescription() {
        return "Позволяет считать и исполнить скрипт из указанного файла. \nТребует ввода названия .txt файла.";
    }

    @Override
    public void execute(App app, String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader(args[1]))) {
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                String[] command = line.split(" ");
                app.getCommandManager().getCommandByName(command[0]).execute(app, command);
            }
        } catch (FileNotFoundException e) {
            app.getIoManager().writeMessage("Файл со скриптом не найден!");
        } catch (IOException e) {
            app.getIoManager().writeMessage("Что-то пошло не так... Повторите ввод. \n");
        } catch (CommandNotFound e) {
            app.getIoManager().writeMessage("Команда не найдена! Ошибка в файле скрипта.\n");
        } catch (StackOverflowError e) {
            app.getIoManager().writeMessage("Скрипт образует цикл! Ошибка в файле скрипта.");
        }
    }
}
