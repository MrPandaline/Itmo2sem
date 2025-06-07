package laba5.common.dataExchanging;

import laba5.common.model.Dragon;

import java.io.Serializable;

public record ResponseClaster (boolean willBeInQuiteMode, String message, Dragon[] objects) implements Serializable {
    public ResponseClaster(boolean willBeInQuiteMode, String message){
        this(willBeInQuiteMode, message, null);
    }
}
