package ru.itmo.java.homoursus.laba5.commands;

import ru.itmo.java.homoursus.laba5.App;
import ru.itmo.java.homoursus.laba5.input.IIOManager;

public class Help implements ICommand{
    @Override
    public String getDescription() {
        return "вывод справку по доступным командам";
    }

    @Override
    public void execute(App app, String[] args) {
        IIOManager ioManager = app.getInput();
        ICommand[] commands = app.getCommandManager().getCommands();
        String[] commandsNames = app.getCommandManager().getCommandNames();

        for (int i = 0; i < commands.length; i++){
            ioManager.writeMessage(commandsNames[i] + ": " + commands[i].getDescription() + "\n");
        }
    }
}
