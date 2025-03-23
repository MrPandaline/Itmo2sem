package laba5.common.commands;

import laba5.client.Client;

/**
 * Класс команды, реализующая завершение работы приложения.
 * @author Homoursus
 * @version 1.0
 */
public class Exit implements IClientSideCommand{

    @Override
    public String getDescription() {
        return "Завершить программу (без сохранения коллекции в файл)";
    }

    @Override
    public void execute(Client client, String[] args) {
        client.turnOffClient();
    }
}
