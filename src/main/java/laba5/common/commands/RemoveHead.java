package laba5.common.commands;

import laba5.common.dataExchanging.Response;
import laba5.common.dataExchanging.ResponseClaster;
import laba5.common.model.Dragon;
import laba5.common.model.User;
import laba5.server.Server;
import laba5.server.logic.CollectionManager;

import java.util.LinkedList;

/**
 * Класс команды, реализующий вывод первого элемента коллекции и его удаление.
 * @author Homoursus
 * @version 1.0
 */
public class RemoveHead implements IServerSideCommand{
    @Override
    public String getDescription() {
        return "Вывести из коллекции первый элемент и удалить его";
    }

    @Override
    public Response execute(String[] args, User user) {
        StringBuilder sb = new StringBuilder();
        Dragon dragon = CollectionManager.getInstance().poll(user);
        int status;
        if (dragon != null) {
            sb.append("Первый элемент коллекции: \n").append(dragon).append("\n");
            status = 200;
        }
        else{
            sb.append("Коллекция пуста либо её первый элемент вам не принадлежит! Введите show для просмотра элементов коллекции");
            status = 404;
        }
        return new Response(status, new ResponseClaster(outInQuiteMode, sb.toString()));
    }
}
