package laba5.common.commands;

import laba5.client.Client;

import java.util.ArrayList;

/**
 * Класс команды, реализующий вывод последних 15 использованных команд.
 * @author Homoursus
 * @version 1.0
 */
public class History implements IClientSideCommand{
    @Override
    public String getDescription() {
        return "Вывести последние 15 команд";
    }

    @Override
    public void execute(Client client, String[] args) {
        ArrayList<String> history = client.getLastUsedCommands();
        for (int i = Math.max(history.size() - 15, 0); i < history.size(); i++) {
            client.getIoManager().printMessage("-" + history.get(i) + "\n", outInQuiteMode);
        }
    }
}
