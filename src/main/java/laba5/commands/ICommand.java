package laba5.commands;

import laba5.App;

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

    boolean outInQuiteMode = true;

    String getDescription();

    /**
     * Метод, отвечающий за выполнение команды.
     * @param app ссылка объект приложения класса App
     * @see App
     * @param args дополнительные аргументы, введённые пользователем
     */
    void execute(App app, String[] args);
}
