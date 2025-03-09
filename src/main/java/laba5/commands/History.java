package laba5.commands;

import laba5.App;

import java.util.ArrayList;

/**
 * Класс команды, реализующий вывод последних 15 использованных команд.
 * @author Homoursus
 * @version 1.0
 */
public class History implements ICommand{
    @Override
    public String getDescription() {
        return "Вывести последние 15 команд";
    }

    @Override
    public void execute(App app, String[] args) {
        ArrayList<String> history = app.getLastUsedCommands();
        for (int i = Math.max(history.size() - 15, 0); i < history.size(); i++) {
            app.getIoManager().writeMessage("-" + history.get(i) + "\n", outInQuiteMode);
        }
    }
}
