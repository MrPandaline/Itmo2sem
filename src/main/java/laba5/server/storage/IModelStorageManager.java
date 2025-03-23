package laba5.server.storage;
import laba5.client.input.IIOManager;
import laba5.common.model.Dragon;
import laba5.server.logging.IServerLogger;

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
     * @return коллекцию из варианта
     */
    LinkedList<Dragon> readFromStorage(IServerLogger logger);

    /**
     * Метод получения id элемента коллекции. Его необходимо назначить генератору id класса Dragon
     * @return следующий id. Возваращает -1 в случае если файл коллекции не найден.
     */
    long getNextID();
}
