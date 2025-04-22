package laba5.common.dataExchanging;

import laba5.common.commands.IServerSideCommand;
import laba5.common.model.User;

import java.io.Serializable;
import java.util.ArrayDeque;

public record Request(IServerSideCommand command, User user, String[] args, boolean haveAdditionalInformation,
                      ArrayDeque<Object> additionalInformation) implements Serializable {
}
