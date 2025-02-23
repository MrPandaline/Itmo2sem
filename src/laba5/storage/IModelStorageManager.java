package laba5.storage;
import laba5.input.IIOManager;
import laba5.model.Dragon;

import java.util.LinkedList;

/**
 * Интерфейс, декларирующий методы, используемые для сохранения (считывания) коллекции из хранилища (в хранилище).
 * @author Homoursus
 * @version 1.0
 */
public interface IModelStorageManager {
    /**
     * Метод, управляющий записью коллекции в хранилище.
     * @param collection коллекция, данная в варианте задания.
     */
    void writeToStorage(LinkedList<Dragon> collection);


    /**
     * Метод, управляющий чтением коллекции из хранилища.
     * @param ioManager реализация менеджера ввода-вывода.
     * @return коллекцию из варианта
     */
    LinkedList<Dragon> readFromStorage(IIOManager ioManager);

    /**
     * Метод получения id элемента коллекции. Его необходимо назначить генератору id класса Dragon
     * @param ioManager реализация менеджера ввода-вывода.
     * @return следующий id
     */
    long getNextID(IIOManager ioManager);
}
