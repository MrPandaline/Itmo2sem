package ru.itmo.java.homoursus.laba5.exceptions;

/**
 * Класс исключения, бросаемого в случае, если команда не найдена.
 * @author Homoursus
 * @version 1.0
 */
public class CommandNotFound extends Exception {
    public String getMessage(){
        return "Неверный ввод. Команда с таким названием не найдена";
    }
}
