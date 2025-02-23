package laba5.commands;

import laba5.App;

/**
 * Класс команды, реализующая завершение работы приложения.
 * @author Homoursus
 * @version 1.0
 */
public class Exit implements ICommand{
    @Override
    public String getDescription() {
        return "Завершить программу (без сохранения коллекции в файл)";
    }

    @Override
    public void execute(App app, String[] args) {
        app.turnOffApp();
    }
}
