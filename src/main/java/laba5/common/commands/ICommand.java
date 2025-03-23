package laba5.common.commands;

import laba5.App;

import java.io.Serializable;

/**
 * Интерфейс, декларирующий методы, используемые для работы с командами.
 * @author Homoursus
 * @version 1.0
 */
public interface ICommand extends Serializable {
    boolean outInQuiteMode = true;

    /**
     * Метод получения описания команды
     * @return возвращает описание команды
     */
    String getDescription();
}
