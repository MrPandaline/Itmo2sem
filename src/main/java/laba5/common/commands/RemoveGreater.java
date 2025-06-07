package laba5.common.commands;

import laba5.common.ModelHandler;
import laba5.client.input.IIOManager;
import laba5.common.dataExchanging.Response;
import laba5.common.dataExchanging.ResponseClaster;
import laba5.common.dataExchanging.UnfinishedDragon;
import laba5.common.model.User;
import laba5.server.logic.CollectionManager;

import java.util.ArrayDeque;

/**
 * Класс команды, реализующий удаление элементов коллекции, превышающих заданный.
 * @author Homoursus
 * @version 1.0.1
 */
public class RemoveGreater implements IServerSideCommand, IMultiLineCommand{
    private ArrayDeque<Object> additionalUserInput = new ArrayDeque<>();

    @Override
    public String getDescription() {
        return "Позволяет удалить из коллекции все элементы, превышающие заданный\n" +
                "Требует ввода нового элемента коллекции";
    }

    @Override
    public Response execute(String[] args, User user) {
        boolean flag = CollectionManager.getInstance().remove(dragon ->
                (((UnfinishedDragon) additionalUserInput.remove())).buildDragon(user.id()).compareTo(dragon) < 0, user);
        String message;
        if (flag) {
            message = "Элементы коллекции, созданные вами и большие, чем введённый, удалены.\n";
        } else {
            message = "Какой-то из элементов удалить не удалось! Все изменения отменены";
        }
        return new Response(200, new ResponseClaster( outInQuiteMode, message));
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
