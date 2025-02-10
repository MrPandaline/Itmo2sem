package ru.itmo.java.homoursus.laba5.commands;

import ru.itmo.java.homoursus.laba5.App;

/**
 * Интерфейс, декларирующий методы, используемые для работы с командами.
 * @author Homoursus
 * @version 1.0
 */
public interface ICommand {
    /**
     * Метод получения описания команды
     * @return возвращает описание команды
     */
    String getDescription();

    /**
     *
     * @param app ссылка объект приложения класса App
     * @see App
     * @param args дополнительные аргументы, введённые пользователем
     */
    void execute(App app, String[] args);
}
