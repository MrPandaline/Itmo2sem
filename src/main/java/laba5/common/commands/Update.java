package laba5.common.commands;

import laba5.client.input.IIOManager;
import laba5.common.ModelHandler;
import laba5.common.dataExchanging.Response;
import laba5.common.dataExchanging.ResponseClaster;
import laba5.common.dataExchanging.UnfinishedDragon;
import laba5.common.model.User;
import laba5.common.model.Dragon;
import laba5.server.logic.CollectionManager;

import java.time.ZonedDateTime;
import java.util.ArrayDeque;

/**
 * Класс команды, реализующий обновление элемента коллекции по id.
 * @author Homoursus
 * @version 1.0
 */
public class Update implements IServerSideCommand, IMultiLineCommand{
    private ArrayDeque<Object> additionalUserInput = new ArrayDeque<>();
    @Override
    public String getDescription() {
        return "Позволяет обновить значение элемента коллекции, id которого равен заданному \nТребует ввода id";
    }

    @Override
    public Response execute(String[] args, User user) {
        StringBuilder sb = new StringBuilder();

        int status;

        if (args.length == 0) {
            sb.append("Вы не ввели id элемента коллекции!");
            status = 401;
        }
        else{
            int id = Integer.parseInt(args[0]);
            ZonedDateTime time = ZonedDateTime.now();

            Dragon drag = ((UnfinishedDragon) additionalUserInput.remove()).buildDragon(time, user.id());
            boolean flag = CollectionManager.getInstance().update(id, drag, user);
            if (!flag){
                sb.append("Элемент коллекции с таким id не найден либо вам не принадлежит! \n")
                        .append("Введите show, чтобы вывести список доступных элементов.\n");
                status = 404;
            }
            else {
                sb.append("Элемент коллекции обновлён!\n");
                status = 200;
            }


        }
        return new Response(status, new ResponseClaster(outInQuiteMode, sb.toString()));
    }

    @Override
    public void getAdditionalUserInput(IIOManager ioManager, ModelHandler handler) {
        UnfinishedDragon dragon = null;
        while (dragon == null) {
            dragon = handler.handleDragon();
        }
        additionalUserInput.push(dragon);
    }

    @Override
    public void setAdditionalUserInput(ArrayDeque<Object> additionalUnformaion) {
        additionalUserInput = additionalUnformaion;
    }
}
