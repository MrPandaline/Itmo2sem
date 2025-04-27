package laba5.server.logic;

import laba5.common.Configuration;
import laba5.common.model.Dragon;
import laba5.common.model.User;

import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Predicate;

/**
 * Обобщённый класс - менеджер управления коллекцией.
 * @author Homoursus
 * @version 1.0.1
 */
public class CollectionManager {

    /** Коллекция, данная по заданию.*/
    private LinkedList<Dragon> collection;

    /** Дата инициализации коллекции. Обновляется каждый раз при запуске приложения.*/
    private final java.time.ZonedDateTime initializationTime;

    {
        initializationTime = java.time.ZonedDateTime.now();
    }

    private DBManager dbManager;

    private HashMap<Dragon, User> dragonUserMap = new HashMap<>();

    private static CollectionManager instance;

    /**
     * Конструктор класса менеджера коллекции.
     * */
    private CollectionManager()
    {
        this.collection = new LinkedList<Dragon>();
        this.dbManager = DBManager.getInstance();
        load();
        Collections.sort(collection);
        instance = this;
    }

    public static CollectionManager getInstance(){
        if (instance == null) {
            instance = new CollectionManager();
        }
        return instance;
    }

    /** Метод получения используемой коллекции. */
    public LinkedList<Dragon> getCollection() {
        return collection;
    }

    /**Метод, позволяющий заменить используемую коллекцию.*/
    public void setCollection(LinkedList<Dragon> collection) {
        this.collection = collection;
    }

    /** Метод, возвращающий дату и время инициализации коллекции.*/
    public ZonedDateTime getCollectionInitializationTime() {
        return initializationTime;
    }

    /** Метод, возвращающий класс используемой коллекции. */
    public Class<?> getCollectionType() {
        return collection.getClass();
    }


    public boolean add(Dragon element){
        boolean flag = dbManager.insertDragon(element, element.creatorId());
        if (flag) {
            long id = dbManager.selectDragon().stream().filter(dragon -> dragon.name().equals(element.name()) &&
                    dragon.killer().equals(element.killer())).findFirst().get().id();
            element.id(id);
            collection.add(element);
        }
        return flag;
    }

    public boolean remove(Predicate<Dragon> predicate, User user){
        LinkedList<Dragon> removed = new LinkedList<>();
        boolean flag = true;
        for (Dragon dragon : collection){
            if (flag && predicate.test(dragon)){
                if (user.id() == dragon.creatorId()) {
                    flag = dbManager.removeDragon(dragon.id());
                }
                if (flag){
                    removed.add(dragon);
                }
            }
        }
        if (!flag){
            for (Dragon dragon : removed){
                dbManager.insertDragon(dragon, dragon.creatorId());
            }
        } else{
            flag = collection.removeIf(predicate);
        }
        return flag;
    }

    public Dragon poll(User user){
        Dragon dragon = collection.poll();
        if (dragon != null && user.id() == dragon.creatorId()) {
            boolean flag = dbManager.removeDragon(dragon.id());
            if (!flag) {
                collection.add(dragon);
                dragon = null;
            }
        }
        else{ dragon = null;}
        return dragon;
    }

    public boolean clear(User user){
        boolean flag = true;
        LinkedList<Dragon> removed = new LinkedList<>();
        for (Dragon dragon : collection){
            if (flag && user.id() == dragon.creatorId()) {
                flag = dbManager.removeDragon(dragon.id());
                removed.add(dragon);
            }
        }
        if (!flag){
            for (Dragon dragon : removed){
                dbManager.insertDragon(dragon, dragon.creatorId());
            }
        }
        else { collection.removeIf(dragon -> user.id() == dragon.creatorId()); ;}
        return flag;
    }

    public void load(){
        try {
            LinkedList<User> users = dbManager.selectUser();
            for (Dragon dragon : dbManager.selectDragon()) {
                collection.add(dragon);
                dragonUserMap.put(dragon, users.get((int) (dragon.creatorId()-1)));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }

    public boolean update(int id, Dragon element, User user){
        boolean flag = false;
        element.id(id);
        if (user.id() == element.creatorId()) {
            flag = dbManager.updateDragon(element);
            if (flag) {
                for (Dragon dragon : collection) {
                    if (dragon.id() == id) {
                        collection.set(collection.indexOf(dragon), element);
                    }
                }
            }
        }
        return flag;
    }
}
