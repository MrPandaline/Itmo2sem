package laba5.common.dataExchanging;

import java.io.Serializable;

public record ResponseClaster (boolean willBeInQuiteMode, String message) implements Serializable { }
