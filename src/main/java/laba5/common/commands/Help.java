package laba5.common.commands;

import laba5.client.Client;
import laba5.client.input.IIOManager;

/**
 * Класс команды, реализующий вывод информации по всем доступным командам.
 * @author Homoursus
 * @version 1.0
 */
public class Help implements IClientSideCommand{
    @Override
    public String getDescription() {
        return "Вывести справку по доступным командам";
    }
    @Override
    public String execute(Client client, String[] args) {

        StringBuilder sb = new StringBuilder();

        IIOManager ioManager = client.getIoManager();
        ICommand[] commands = client.getCommandManager().getCommands();
        final String SEPARATOR = "-----------------------------------------------------------------------\n";
        // Можно переписать с помощью forEach, но надо ли?
        for (ICommand command : commands) {
            sb.append(SEPARATOR);
            sb.append(command.getClass().getSimpleName().replaceAll("([a-z])([A-Z])",
                    "$1_$2").toLowerCase()).append("\n");
            sb.append(command.getDescription()).append("\n");
        }
        ioManager.printMessage(SEPARATOR, outInQuiteMode);

        return sb.toString();
    }
}
