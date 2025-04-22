package laba5.common.dataExchanging;

import java.io.Serializable;

public record Response(long statusCode, ResponseClaster responseClaster) implements Serializable {
}
