package laba5.common.commands;

import laba5.common.dataExchanging.Response;
import laba5.common.dataExchanging.ResponseClaster;
import laba5.common.model.Dragon;
import laba5.common.model.User;
import laba5.server.Server;
import laba5.server.logic.CollectionManager;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.stream.Collectors;

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
    public Response execute(String[] args, User user) {
        LinkedList<Dragon> linkedList = CollectionManager.getInstance().getCollection();
        linkedList.sort((a, b) -> {
            if (b.killer() != null && a.killer() != null) {
                return b.killer().compareTo(a.killer());
            } else {
                return a.name().compareTo(b.name());
            }
            });

        StringBuilder sb = new StringBuilder();
        for (Dragon dragon : linkedList) {
            if (dragon.killer() != null) {
                sb.append(dragon.killer().toString()).append("\n").append("\n");
            }
        }

        String result = linkedList.stream()
            .filter(dragon -> dragon.killer() != null)
            .map(dragon -> dragon.killer().toString())
            .collect(Collectors.joining("\n\n"));

        Collections.sort(linkedList);

        return new Response(200, new ResponseClaster(outInQuiteMode, sb.toString()));
    }
}
