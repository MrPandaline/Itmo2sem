package laba5.client;

import laba5.common.commands.*;
import laba5.client.input.ConsoleIOManager;
import laba5.client.input.IIOManager;
import laba5.server.logic.CommandManager;

//TODO: Надо попилить команды на несколько интерфейсов
// в зависимости от того какой именно она реализует определять где её выполнять (
// ClientSideCommand - исполнить чисто на клиенте, ничё не говорить серверу (exit
// (хотя, возможно, серверу нужно сообщить что клиент отвалился) а также execute_script)
// ServerSideCommand - обычные команды, которые требуют получения Response от сервера
// MultiLineCommand - команды, которые требуют дополнительного ввода от пользователя
// (сначала надо поговорить с пользователем, получить от него всё, что нужно, закинуть это в Request, скормить серверу
// и ждать Response). (сделано, но я это оставлю т.к. нао боту писать)
public class Main {
    public static void main(String[] args) {
        String emergencyCommands = "emergencyCommands.csv";
        String historyCommands = "commandsFileName.csv";
        CommandManager commandManager = new CommandManager();
        commandManager.addCommands(new Help(),new Info(), new Show(), new Add(),new Update(),
                new RemoveById(), new Clear(), new Save(), new ExecuteScript(),
                new Exit(), new RemoveHead(), new RemoveGreater(), new History(),
                new GroupCountingByName(), new FilterGreaterThanType(),
                new PrintFieldDescendingKiller());


        IIOManager ioManager = new ConsoleIOManager(commandManager.getCommandNames(), emergencyCommands);
        Client client = new Client(ioManager, historyCommands, emergencyCommands, commandManager);
        client.run();
    }
}
