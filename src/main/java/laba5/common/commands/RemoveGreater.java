package laba5.common.commands;

import laba5.client.input.IIOManager;
import laba5.common.dataExchanging.Response;
import laba5.common.dataExchanging.ResponseClaster;
import laba5.common.model.User;
import laba5.server.Server;
import laba5.common.ModelBuilder;
import laba5.common.model.Dragon;

import java.util.ArrayDeque;
import java.util.LinkedList;

/**
 * Класс команды, реализующий удаление элементов коллекции, превышающих заданный.
 * @author Homoursus
 * @version 1.0.1
 */
public class RemoveGreater implements IServerSideCommand, IMultiLineCommand{
    private final ArrayDeque<Object> additionalUserInput = new ArrayDeque<>();

    @Override
    public String getDescription() {
        return "Позволяет удалить из коллекции все элементы, превышающие заданный\n" +
                "Требует ввода нового элемента коллекции";
    }

    @Override
    public Response execute(Server server, String[] args, User user) {
        //TODO: вот тут надо дополнительный ввод пользователя получить и вписать вот этот интерфейс


        server.getCollectionManager().remove(dragon -> ((Dragon) additionalUserInput.remove()).compareTo(dragon) < 0, user);
        return new Response(200, new ResponseClaster( outInQuiteMode,"Элементы большие, чем введённый, удалены."));
    }

    @Override
    public void getAdditionalUserInput(IIOManager ioManager) {
        Dragon curDragon = new ModelBuilder(ioManager).buildDragon();
        additionalUserInput.push(curDragon);
    }
}
