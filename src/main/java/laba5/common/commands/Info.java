package laba5.common.commands;

import laba5.common.dataExchanging.Response;
import laba5.common.dataExchanging.ResponseClaster;
import laba5.server.Server;
import laba5.server.logic.CollectionManager;

import java.time.ZonedDateTime;

/**
 * Класс команды, реализующий вывод информации о коллекции.
 * Выводит тип, время инициализации, размер и класс, экземпляры которого содержатся в коллекции.
 * @author Homoursus
 * @version 1.0
 */
public class Info implements IServerSideCommand{
    @Override
    public String getDescription() {
        return "Вывод информации о коллекции";
    }

    @Override
    public Response execute(Server server, String[] args) {
        CollectionManager<?> collectionManager = server.getCollectionManager();
        ZonedDateTime time = collectionManager.getCollectionInitializationTime();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Тип коллекции: ").append(collectionManager.getCollectionType().getSimpleName()).append("\n");
        stringBuilder.append("Время инициализации коллекции: ").append(time.toString(), 0, 10).append(" ")
                .append(time.toString(), 11, 19).append("\n");
        stringBuilder.append("Размер коллекции: ").append(collectionManager.getCollection().size()).append("\n");
        if (!collectionManager.getCollection().isEmpty()) {
            stringBuilder.append("Класс, экземпляры которого содержатся в коллекции: ").append(collectionManager
                    .getCollection().iterator().next().getClass().getSimpleName()).append("\n");
        }
        return new Response(new ResponseClaster(outInQuiteMode, stringBuilder.toString()));
    }
}
