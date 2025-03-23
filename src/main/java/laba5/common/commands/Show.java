package laba5.common.commands;

import laba5.App;
import laba5.client.input.IIOManager;
import laba5.common.dataExchanging.Response;
import laba5.common.dataExchanging.ResponseClaster;
import laba5.common.model.Dragon;
import laba5.server.Server;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Класс команды, реализующий вывод всех элементов коллекции.
 * @author Homoursus
 * @version 1.0
 */
public class Show implements IServerSideCommand{
    @Override
    public String getDescription() {
        return "Вывод всех элементов коллекции";
    }

    @Override
    public Response execute(Server server, String[] args) {
        LinkedList<Dragon> collection = server.getCollectionManager().getCollection();
        StringBuilder sb = new StringBuilder();
        Collections.sort(collection);
        if (!collection.isEmpty()) {
            for (Object object : collection) {
                sb.append(object.toString()).append('\n').append('\n');
            }
        }
        else {
            sb.append("Коллекция пуста! Введите add для добавления нового элемента.");
        }
        return new Response(new ResponseClaster(outInQuiteMode, sb.toString()));
    }
}
