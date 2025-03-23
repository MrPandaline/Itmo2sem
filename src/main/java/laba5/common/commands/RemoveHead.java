package laba5.common.commands;

import laba5.common.dataExchanging.Response;
import laba5.common.dataExchanging.ResponseClaster;
import laba5.common.model.Dragon;
import laba5.server.Server;

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
    public Response execute(Server server, String[] args) {
        LinkedList<Dragon> linkedList = server.getCollectionManager().getCollection();
        StringBuilder sb = new StringBuilder();
        Dragon dragon = linkedList.poll();
        if (dragon != null) {
            sb.append("Первый элемент коллекции: \n").append(dragon).append("\n");
        }
        else{
            sb.append("Коллекция пуста! Введите add для добавления нового элемента.");
        }
        return new Response(new ResponseClaster(outInQuiteMode, sb.toString()));
    }
}
