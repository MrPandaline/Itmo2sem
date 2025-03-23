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
    public void execute(Client client, String[] args) {
        IIOManager ioManager = client.getIoManager();
        ICommand[] commands = client.getCommandManager().getCommands();
        final String SEPARATOR = "-----------------------------------------------------------------------\n";
        for (ICommand command : commands) {
            ioManager.printMessage(SEPARATOR, outInQuiteMode);
            ioManager.printMessage(command.getClass().getSimpleName().replaceAll("([a-z])([A-Z])",
                    "$1_$2").toLowerCase() + "\n", outInQuiteMode);
            ioManager.printMessage(command.getDescription() + "\n", outInQuiteMode);
        }
        ioManager.printMessage(SEPARATOR, outInQuiteMode);
    }
}
