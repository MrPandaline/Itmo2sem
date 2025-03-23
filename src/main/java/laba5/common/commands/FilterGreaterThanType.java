package laba5.common.commands;

import laba5.common.dataExchanging.Response;
import laba5.common.dataExchanging.ResponseClaster;
import laba5.common.model.Dragon;
import laba5.common.model.modelEnums.DragonType;
import laba5.server.Server;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

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
    public Response execute(Server server, String[] args) {

        LinkedList<Dragon> linkedList = server.getCollectionManager().getCollection();
        Response response;
        if (args.length == 0) {
            response = annotate();
        } else {
            try{
                StringBuilder sb = new StringBuilder();
                DragonType dragonType = DragonType.valueOf(args[0].toUpperCase());
                linkedList.sort(Comparator.comparing(Dragon::dragonType));
                boolean flag = false;
                for (Dragon dragon : linkedList) {
                    if (dragon.dragonType().compareTo(dragonType) > 0) {
                        flag = true;
                        sb.append(dragon).append("\n");
                    }
                }
                if (!flag) {
                    sb.append("Нет элементов коллекции, чьё значение пол type превышает заданное!\n");
                }
                Collections.sort(linkedList);
                response = new Response(new ResponseClaster(true, sb.toString()));
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
        return new Response(new ResponseClaster(outInQuiteMode, sb.toString()));
    }
}
