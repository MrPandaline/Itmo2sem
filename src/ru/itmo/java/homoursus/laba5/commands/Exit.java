package ru.itmo.java.homoursus.laba5.commands;

import ru.itmo.java.homoursus.laba5.App;

/**
 * Класс команды, реализующая завершение работы приложения.
 * @author Homoursus
 * @version 1.0
 */
public class Exit implements ICommand{
    @Override
    public String getDescription() {
        return "завершить программу (без сохранения в файл)";
    }

    @Override
    public void execute(App app, String[] args) {
        app.turnOffApp();
    }
}
