package laba5.common.commands;

import laba5.client.input.IIOManager;

import java.util.ArrayDeque;

public interface IMultiLineCommand {
    ArrayDeque<Object> getAdditionalUserInput(IIOManager ioManager);

    void setAdditionalUserInput(ArrayDeque<Object> additionalUserInput);
}
