package laba5.common.commands;

import laba5.common.dataExchanging.Response;
import laba5.common.dataExchanging.ResponseClaster;
import laba5.common.model.Dragon;
import laba5.common.model.User;
import laba5.server.Server;
import laba5.server.logic.CollectionManager;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс команды, реализующий группировку элементов коллекции по имени дракона.
 * Выводит количество элементов в каждой группе.
 * @author Homoursus
 * @version 2.0
 */
public class GroupCountingByName implements IServerSideCommand{

    @Override
    public String getDescription() {
        return "Сгруппировать элементы коллекции по значению поля name, \nВывести количество элементов в каждой группе";
    }

    @Override
    public Response execute(String[] args, User user) {
        LinkedList<Dragon> linkedList = CollectionManager.getInstance().getCollection();
        linkedList.sort(Comparator.comparing(Dragon::name));

    String result = linkedList.stream()
            .collect(Collectors.groupingBy(Dragon::name, Collectors.counting())).entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .map(entry -> "Элементов коллекции с именем " + entry.getKey() + ": " + entry.getValue())
            .collect(Collectors.joining("\n"));

        return new Response(200, new ResponseClaster(outInQuiteMode, result));
    }
}
