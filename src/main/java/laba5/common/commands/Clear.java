package laba5.common.commands;

import laba5.common.dataExchanging.Response;
import laba5.common.dataExchanging.ResponseClaster;
import laba5.common.model.User;
import laba5.server.Server;
import laba5.server.logic.CollectionManager;

import java.util.LinkedList;

/**
 * Класс команды, реализующий очистку коллекции.
 * @author Homoursus
 * @version 2.0
 */
public class Clear implements IServerSideCommand{
    @Override
    public String getDescription() {
        return "Очистить коллекцию";
    }

    @Override
    public Response execute( String[] args, User user) {
        CollectionManager.getInstance().clear(user);
        String message = "Из коллекции удалены элементы, созданные вами!";
        return new Response(200, new ResponseClaster(outInQuiteMode,message));
    }
}
