package laba5.common.commands;

import laba5.client.input.IIOManager;
import laba5.common.dataExchanging.Response;
import laba5.common.dataExchanging.ResponseClaster;
import laba5.common.model.User;
import laba5.server.Server;
import laba5.common.ModelBuilder;
import laba5.common.model.Dragon;

import java.util.ArrayDeque;

/**
 * Класс команды, реализующий добавление элементов в коллекцию.
 * @author Homoursus
 * @version 2.0
 */
public class Add implements IServerSideCommand, IMultiLineCommand {

    private final ArrayDeque<Object> additionalUserInput = new ArrayDeque<>();

    @Override
    public String getDescription() {
        return "Добавить новый элемент в коллекцию";
    }

    @Override
    public Response execute(Server server, String[] args, User user) {
        Dragon drag = (Dragon) additionalUserInput.remove();
        System.out.println(drag.id());
        drag.resetID();
        System.out.println(drag.id());
        server.getCollectionManager().add(drag);
        return new Response(200, new ResponseClaster(outInQuiteMode, "Элемент добавлен в коллекцию!"));
    }

    @Override
    public void getAdditionalUserInput(IIOManager ioManager) {
        //TODO: надо исправить ModelBuilder (не собирать дракона а передать поля на сервер, на котором его будут собирать)
        ModelBuilder handler = new ModelBuilder(ioManager);
        Dragon dragon = handler.buildDragon();
        additionalUserInput.push(dragon);
    }

}
