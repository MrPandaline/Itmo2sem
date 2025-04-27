package laba5.common.commands;

import laba5.common.dataExchanging.Response;
import laba5.common.dataExchanging.ResponseClaster;
import laba5.common.model.Dragon;
import laba5.common.model.User;
import laba5.server.Server;
import laba5.server.logic.CollectionManager;
import laba5.server.storage.IModelStorageManager;

import java.util.LinkedList;

/**
 * Класс команды, реализующий сохранение элементов коллекции в хранилище.
 * @author Homoursus
 * @version 1.0
 */
public class Save implements IServerSideCommand{
    @Override
    public String getDescription() {
        return "Сохранить коллекцию в хранилище";
    }

    @Override
    public Response execute(String[] args, User user) {
        LinkedList<Dragon> collection = CollectionManager.getInstance().getCollection();
        //TODO: надо посмотреть нужен ли тут сейв вообще?
        return new Response(200, new ResponseClaster(outInQuiteMode, "Коллекция сохранена в файл! \n"));
    }
}
