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
import java.util.List;

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
    public Response execute(String[] args, User user) {
        List<Dragon> collection = CollectionManager.getInstance().getCollection();
        StringBuilder sb = new StringBuilder();
        Collections.sort(collection);
        int status;

        if (!collection.isEmpty()) {
            status = 200;
            //TODO: надо сделать ограничение по числу выводимых элементов коллекции т.к. если пользователю вылетит полторы тыщи элементов, то будет так себе
            synchronized (collection) {
                for (Object object : collection) {
                    sb.append(object.toString()).append('\n').append('\n');
                }
            }
        }
        else {
            sb.append("Коллекция пуста! Введите add для добавления нового элемента.");
            status = 404;
        }
        return new Response(status, new ResponseClaster(outInQuiteMode, sb.toString()));
    }
}
