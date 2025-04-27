package laba5.common.commands;

import laba5.common.dataExchanging.Response;
import laba5.common.dataExchanging.ResponseClaster;
import laba5.common.model.Dragon;
import laba5.common.model.User;
import laba5.common.model.modelEnums.DragonType;
import laba5.server.Server;
import laba5.server.logic.CollectionManager;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * Класс команды, реализующий вывод элементов коллекции, значение поля type которых, передано в команду.
 * @author Homoursus
 * @version 1.0.1
 */
public class FilterGreaterThanType implements IServerSideCommand{
    @Override
    public String getDescription() {
        return "Вывести элементы, значение поля type которых, больше заданного\n" +
                "Требует ввода значения поля type";
    }

    @Override
    public Response execute(String[] args, User user) {

        LinkedList<Dragon> linkedList = CollectionManager.getInstance().getCollection();
        Response response;
        int responseCode = 200;
        if (args.length == 0) {
            response = annotate();
        } else {
            try{
                DragonType dragonType = DragonType.valueOf(args[0].toUpperCase());
                String result = linkedList.stream()
                        .sorted(Comparator.comparing(Dragon::dragonType))
                        .filter(dragon -> dragon.dragonType().compareTo(dragonType) > 0)
                        .map(Object::toString)
                        .collect(Collectors.joining("\n"));

                if (result.isEmpty()) {
                    result = "Нет элементов коллекции, чьё значение пол type превышает заданное!\n";
                    responseCode = 404;
                }
                Collections.sort(linkedList);
                response = new Response(responseCode, new ResponseClaster(true, result));
            }
            catch (IllegalArgumentException e){
               response = annotate();
            }
        }
        return response;
    }
    private Response annotate() {
        StringBuilder sb = new StringBuilder();
        sb.append("Вы не верно ввели значение поля type! \n");

        sb.append("Доступные типы: ");
        for (DragonType dragonType : DragonType.values()) {
            sb.append(dragonType.toString()).append(" ");
        }
        sb.append("\n");
        return new Response(401, new ResponseClaster(outInQuiteMode, sb.toString()));
    }
}
