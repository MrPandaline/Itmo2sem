package ru.itmo.java.homoursus.laba5.exceptions;

public class CommandNotFound extends Exception {
    public String getMessage(){
        return "Неверный ввод. Команда с таким названием не найдена";
    }
}
