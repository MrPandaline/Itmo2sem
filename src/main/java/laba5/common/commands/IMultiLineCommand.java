package laba5.common.commands;

import laba5.client.input.IIOManager;
import laba5.common.ModelHandler;
import laba5.common.dataExchanging.UnfinishedDragon;

import java.util.ArrayDeque;
import java.util.ArrayList;

public interface IMultiLineCommand {
    void getAdditionalUserInput(IIOManager ioManager, ModelHandler handle);
    void setAdditionalUserInput(ArrayDeque<Object> additionalUnformaion);

}
