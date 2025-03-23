package laba5.common.commands;

import laba5.App;
import laba5.client.input.IIOManager;
import laba5.common.dataExchanging.Response;
import laba5.common.dataExchanging.ResponseClaster;
import laba5.common.model.Dragon;
import laba5.server.Server;

import java.util.LinkedList;

/**
 * Класс команды, реализующий удаление элемента коллекции по его id.
 * @author Homoursus
 * @version 1.0
 */
public class RemoveById implements IServerSideCommand{
    @Override
    public String getDescription() {
        return "Позволят удалить элемент из коллекции по его id, \nТребует ввода id";
    }

    @Override
    public Response execute(Server server, String[] args) {
        LinkedList<Dragon> linkedList = server.getCollectionManager().getCollection();
        StringBuilder sb = new StringBuilder();
        boolean quiteMode = outInQuiteMode;
        if (args.length == 0) {
            sb.append("Вы не ввели id элемента коллекции!\n");
            quiteMode = false;
        } else {
            int id = Integer.parseInt(args[0]);
            if (linkedList.removeIf(dragon -> dragon.id() == id)) {
                sb.append("Элемент коллекции удалён!\n");
            }
            else {
                sb.append("Нет элементов коллекции с таким индексом!\n");
            }
        }
        return new Response(new ResponseClaster(quiteMode, sb.toString()));
    }
}
