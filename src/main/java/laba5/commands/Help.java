package laba5.commands;

import laba5.App;
import laba5.input.IIOManager;

/**
 * Класс команды, реализующий вывод информации по всем доступным командам.
 * @author Homoursus
 * @version 1.0
 */
public class Help implements ICommand{
    @Override
    public String getDescription() {
        return "Вывести справку по доступным командам";
    }
    @Override
    public void execute(App app, String[] args) {
        IIOManager ioManager = app.getIoManager();
        ICommand[] commands = app.getCommandManager().getCommands();
        String[] commandsNames = app.getCommandManager().getCommandNames();
        final String SEPARATOR = "-----------------------------------------------------------------------\n";
        for (int i = 0; i < commands.length; i++){
            ioManager.writeMessage(SEPARATOR, outInQuiteMode);
            ioManager.writeMessage(commandsNames[i] + "\n"+ commands[i].getDescription() + "\n", outInQuiteMode);
        }
        ioManager.writeMessage(SEPARATOR, outInQuiteMode);
    }
}
