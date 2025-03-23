package laba5.common.dataExchanging;

import laba5.common.commands.ICommand;

import java.io.Serializable;
import java.util.ArrayDeque;

public record Request(ICommand command, String[] args, boolean haveAdditionalInformation,
                      ArrayDeque<Object> additionalInformation) implements Serializable {
}
