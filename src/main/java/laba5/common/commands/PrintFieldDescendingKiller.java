package laba5.common.commands;

import laba5.common.dataExchanging.Response;
import laba5.common.dataExchanging.ResponseClaster;
import laba5.common.model.Dragon;
import laba5.server.Server;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Класс команды, реализующий вывод элементов коллекции в порядке убывания значения поля killer.
 * @author Homoursus
 * @version 1.1
 */
public class PrintFieldDescendingKiller implements IServerSideCommand {
    @Override
    public String getDescription() {
        return "Вывести значения поля killer всех элементов в порядке убывания";
    }



    @Override
    public Response execute(Server server, String[] args) {
        LinkedList<Dragon> collection = server.getCollectionManager().getCollection();
        collection.sort((a, b) -> {
            if (b.killer() != null && a.killer() != null) {
                return b.killer().compareTo(a.killer());
            }
            else {
                return a.name().compareTo(b.name());
            }
            });

        StringBuilder sb = new StringBuilder();
        for (Dragon dragon : collection) {
            if (dragon.killer() != null) {
                sb.append(dragon.killer().toString()).append("\n").append("\n");
            }
        }
        Collections.sort(collection);

        return new Response( new ResponseClaster(outInQuiteMode, sb.toString()));
    }
}
