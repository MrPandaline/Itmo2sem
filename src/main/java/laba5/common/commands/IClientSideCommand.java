package laba5.common.commands;

import laba5.client.Client;

public interface IClientSideCommand extends ICommand{
    /**
     * Метод, отвечающий за выполнение клиентской команды.
     * @param client ссылка объект приложения класса App
     * @see Client
     * @param args дополнительные аргументы, введённые пользователем
     */
    void execute(Client client, String[] args);
}
