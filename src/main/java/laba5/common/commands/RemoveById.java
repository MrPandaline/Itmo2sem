package laba5.common.commands;

import laba5.common.dataExchanging.Response;
import laba5.common.dataExchanging.ResponseClaster;
import laba5.common.model.Dragon;
import laba5.common.model.User;
import laba5.server.Server;
import laba5.server.logic.CollectionManager;

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
    public Response execute( String[] args, User user) {
        StringBuilder sb = new StringBuilder();
        boolean quiteMode = outInQuiteMode;
        int status;
        if (args.length == 0) {
            sb.append("Вы не ввели id элемента коллекции!\n");
            quiteMode = false;
            status = 401;
        } else {
            int id = Integer.parseInt(args[0]);
            // Можно тоже под stream API переделать, но зачем?
            if (CollectionManager.getInstance().remove(dragon -> dragon.id() == id, user)) {
                sb.append("Элемент коллекции удалён!\n");
                status = 200;
            }
            else {
                sb.append("Нет элементов коллекции с таким индексом либо его создали не вы!\n");
                status = 404;
            }
        }
        return new Response(status, new ResponseClaster(quiteMode, sb.toString()));
    }
}
