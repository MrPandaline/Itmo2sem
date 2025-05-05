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
    private final List<Dragon> collection;

    /** Дата инициализации коллекции. Обновляется каждый раз при запуске приложения.*/
    private final java.time.ZonedDateTime initializationTime;

    {
        initializationTime = java.time.ZonedDateTime.now();
    }

    private final DBManager dbManager;

    private final HashMap<Dragon, User> dragonUserMap = new HashMap<>();

    private static CollectionManager instance;

    /**
     * Конструктор класса менеджера коллекции.
     * */
    private CollectionManager()
    {
        this.collection = Collections.synchronizedList(new LinkedList<>());
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
    public List<Dragon> getCollection() {
        return collection;
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
        if (dragonUserMap.containsKey(element)) {
            return false;
        }

        boolean flag = dbManager.insertDragon(element, element.creatorId());
        if (flag) {
            LinkedList<Dragon> dragons = dbManager.selectDragon(element.name());
            long id;
            if (element.killer() != null) {
                System.out.println(element.killer().name() + " "+ element.name());
                id = dragons.stream()
                        .filter(dragon -> dragon.name().equals(element.name()))
                        .filter(dragon -> dragon.killer().name().equals(element.killer().name()))
                        .findFirst().get().id();
            }
            else {
                id = dragons.stream()
                        .filter(dragon -> dragon.name().equals(element.name()))
                        .filter(dragon -> dragon.age() == element.age())
                        .filter(dragon -> dragon.dragonType().equals(element.dragonType()))
                        .findFirst().get().id();
            }
            element.id(id);
            collection.add(element);
        }
        return flag;
    }

    public boolean remove(Predicate<Dragon> predicate, User user){
        LinkedList<Dragon> removed = new LinkedList<>();
        boolean flag = false;
        for (Dragon dragon : collection){
            if (!flag && predicate.test(dragon)){
                if (user.id() == dragon.creatorId()) {
                    flag = dbManager.removeDragon(dragon.id());
                }
                if (flag){
                    removed.add(dragon);
                }
            }
        }
        if (!flag){
            synchronized (collection) {
                for (Dragon dragon : removed) {
                    dbManager.insertDragon(dragon, dragon.creatorId());
                }
            }
        } else{
            synchronized (collection) {
                flag = collection.removeIf(predicate);
            }
        }
        return flag;
    }

    public Dragon poll(User user){
        Dragon dragon = collection.get(0);
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
        else { synchronized (collection){collection.removeIf(dragon -> user.id() == dragon.creatorId()); }}
        return flag;
    }

    public void load(){
        System.out.println("я тута (load в CollectionManager)");
        try {
            HashMap<Integer, User> users = dbManager.selectUser();
            for (Dragon dragon : dbManager.selectDragon()) {
                System.out.println("зашёл в цикл: "+ dragon.name());
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
                synchronized (collection) {
                    for (Dragon dragon : collection) {
                        if (dragon.id() == id) {
                            collection.set(collection.indexOf(dragon), element);
                        }
                    }
                }
            }
        }
        return flag;
    }
}
