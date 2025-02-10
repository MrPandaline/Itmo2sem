package ru.itmo.java.homoursus.laba5.input;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Интерфейс, декларирующий методы, модуля ввода-вывода.
 * @author Homoursus
 * @version 1.0
 */
public interface IIOManager {

    /** @return получает необработанную строку, введённую пользователем*/
    String getRawInput();

    /**
     * Метод, получающий строку, введённую пользователем, удовлетворяющую определённому условию.
     * Выполнение метода не должно прекращаться до момента получения валидного ввода от пользователя.
     * @param condition Условие, по которому будет происходить валидация ввода. Задаётся с помощью предиката.
     */
    String getValidRawInput(Predicate<String> condition);

    /** Вывод сообщения*/
    void writeMessage(String message);

    /**
     * Параметризованный метод, получающий число, введённое пользователем.
     * Выполнение метода не должно прекращаться до момента получения валидного ввода от пользователя.
     * @param <T> Любой класс-наследник Number. Классы-обёртки в частности.
     * @param function Ссылка на метод-парсер, получающий число из строки. Имеет вид <T>.parse<T>()
     */
    <T extends Number> T getDigit(Function<String, T> function);

    /**
     * Параметризованный метод, получающий число, введённое пользователем, которое удовлетворяет определённому условию.
     * Выполнение метода не должно прекращаться до момента получения валидного ввода от пользователя.
     * @param <T> Любой класс-наследник Number. Классы-обёртки в частности.
     * @param function Ссылка на метод-парсер, получающий число из строки. Имеет вид <T>.parse<T>()
     * @param condition Условие, по которому будет происходить валидация ввода. Задаётся с помощью предиката.
     */
    <T extends Number> T getValidDigit(Function<String, T> function, Predicate<T> condition);

    /**
     * Метод, получающий одну из констант из списка.
     * Выполнение метода не должно прекращаться до момента получения валидного ввода от пользователя.
     * @param constants список констант.
     * @param canBeNull Флаг, показывающий, может ли константа быть null.
     * @return одна из констант.
     */
    String getConstantString(ArrayList<String> constants, boolean canBeNull) ;
}

