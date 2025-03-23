package laba5.common.commands;

import laba5.common.dataExchanging.Response;
import laba5.common.dataExchanging.ResponseClaster;
import laba5.common.model.Dragon;
import laba5.server.Server;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

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
    public Response execute(Server server, String[] args) {
        LinkedList<Dragon> linkedList = server.getCollectionManager().getCollection();
        linkedList.sort(Comparator.comparing(Dragon::name));

        HashMap<String, Integer> countMap = new HashMap<>();

        StringBuilder sb = new StringBuilder();

        for (Dragon dragon : linkedList) {
            if (countMap.containsKey(dragon.name())) {
                countMap.put(dragon.name(), countMap.get(dragon.name()) + 1);
            }
            else{
                countMap.put(dragon.name(), 1);
            }
        }
        for (String name : countMap.keySet()) {
            sb.append("Элементов коллекции с именем ").append(name).append(": ")
                    .append(countMap.get(name)).append("\n");
        }
        return new Response(new ResponseClaster(outInQuiteMode, sb.toString()));
    }
}
