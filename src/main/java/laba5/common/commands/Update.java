package laba5.common.commands;

import laba5.client.input.IIOManager;
import laba5.common.dataExchanging.Response;
import laba5.common.dataExchanging.ResponseClaster;
import laba5.server.Server;
import laba5.common.ModelBuilder;
import laba5.common.model.Dragon;

import java.util.ArrayDeque;
import java.util.LinkedList;

/**
 * Класс команды, реализующий обновление элемента коллекции по id.
 * @author Homoursus
 * @version 1.0
 */
public class Update implements IServerSideCommand, IMultiLineCommand{
    private final ArrayDeque<Object> additionalUserInput = new ArrayDeque<>();
    @Override
    public String getDescription() {
        return "Позволяет обновить значение элемента коллекции, id которого равен заданному \nТребует ввода id";
    }

    @Override
    public Response execute(Server server, String[] args) {
        StringBuilder sb = new StringBuilder();
        LinkedList<Dragon> linkedList = server.getCollectionManager().getCollection();
        if (args.length == 0) {
            sb.append("Вы не ввели id элемента коллекции!");
        }
        else{
            int id = Integer.parseInt(args[0]);
            boolean flag = false;
            for(Dragon dragon : linkedList){
                if (dragon.id() == id){
                    flag = true;
                    linkedList.set(linkedList.indexOf(dragon), (Dragon) additionalUserInput.remove());
                }
                //TODO: добавить вывод сообщения что элемент обновлён
            }
            if (!flag){
                sb.append("Элемент коллекции с таким id не найден! \n")
                        .append("Введите show, чтобы вывести список доступных элементов.\n");
            }

        }
        return new Response(new ResponseClaster(outInQuiteMode, sb.toString()));
    }

    @Override
    public void getAdditionalUserInput(IIOManager ioManager) {
        Dragon newDragon = new ModelBuilder(ioManager).buildDragon();
        additionalUserInput.push(newDragon);
    }
}
