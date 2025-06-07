package laba5.common.commands;

import laba5.common.dataExchanging.Response;
import laba5.common.dataExchanging.ResponseClaster;
import laba5.common.model.Dragon;
import laba5.common.model.User;
import laba5.server.logic.CollectionManager;

import java.util.ArrayList;
import java.util.Collections;
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
        ArrayList<Dragon> dragons = new ArrayList<>();
        List<Dragon> collection = CollectionManager.getInstance().getCollection();
        StringBuilder sb = new StringBuilder();
        Collections.sort(collection);
        int status;
        int quantityOfDragons = 10;
        int index = 0;
        try {
            if (args.length > 0) {
                quantityOfDragons = Integer.parseInt(args[0]);
            }
            if (args.length > 1) {
                index = Integer.parseInt(args[1]);
            }
        } catch (NumberFormatException ignored){}

        if (!collection.isEmpty()) {
            status = 200;
            synchronized (collection) {
                try {
                    for (int i = index; i < quantityOfDragons; i++) {
                        sb.append(collection.get(i).toString()).append('\n').append('\n');
                        dragons.add(collection.get(i));
                    }
                } catch (IndexOutOfBoundsException ignored){ }
            }
        }

        else {
            sb.append("Коллекция пуста! Введите add для добавления нового элемента.");
            status = 404;
        }
        return new Response(status, new ResponseClaster(outInQuiteMode, sb.toString(),  dragons.toArray(new Dragon[0])));
    }
}
