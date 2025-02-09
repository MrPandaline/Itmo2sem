package ru.itmo.java.homoursus.laba5.input;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Predicate;

public interface IIOManager {
    String getRawInput();

    String getValidRawInput(Predicate<String> condition);

    void writeMessage(String message);

    <T extends Number> T getDigit(Function<String, T> function);

    <T extends Number> T getValidDigit(Function<String, T> function, Predicate<T> condition);

    String getConstantString(ArrayList<String> constants, boolean canBeNull) ;
}

