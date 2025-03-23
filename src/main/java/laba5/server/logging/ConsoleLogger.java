package laba5.server.logging;

public class ConsoleLogger implements IServerLogger {

    @Override
    public void log(String message) {
        System.out.println(message);
    }
}
