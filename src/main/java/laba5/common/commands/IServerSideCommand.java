package laba5.common.commands;

import laba5.common.dataExchanging.Response;
import laba5.common.model.User;
import laba5.server.Server;

public interface IServerSideCommand extends ICommand {
    /**
     * Метод, отвечающий за выполнение серверной команды.
     * @see Server
     * @param args дополнительные аргументы, введённые пользователем
     */
    Response execute(Server server, String[] args, User user);
}
