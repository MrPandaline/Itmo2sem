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
        final String SEPARATOR = "-----------------------------------------------------------------------\n";
        for (int i = 0; i < commands.length; i++){
            ioManager.printMessage(SEPARATOR, outInQuiteMode);
            ioManager.printMessage(commands[i].getClass().getSimpleName().replaceAll("([a-z])([A-Z])",
                    "$1_$2").toLowerCase()+"\n", outInQuiteMode);
            ioManager.printMessage( commands[i].getDescription() + "\n", outInQuiteMode);
        }
        ioManager.printMessage(SEPARATOR, outInQuiteMode);
    }
}
