package laba5.server.logic;

import laba5.common.Configuration;
import laba5.common.genetics.Genome;
import laba5.common.genetics.GenomeGenerator;
import laba5.common.model.Dragon;
import laba5.common.model.User;
import laba5.server.db.DragonGenomeDBProcessor;

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
    private List<Dragon> collection;

    /** Дата инициализации коллекции. Обновляется каждый раз при запуске приложения.*/
    private final java.time.ZonedDateTime initializationTime;

    {
        initializationTime = java.time.ZonedDateTime.now();
    }

    private final DBManager dbManager;
    private final DragonGenomeDBProcessor dgDBp;

    private final HashMap<Dragon, User> dragonUserMap = new HashMap<>();

    private static CollectionManager instance;

    /**
     * Конструктор класса менеджера коллекции.
     * */
    private CollectionManager()
    {
        this.collection = Collections.synchronizedList(new LinkedList<>());
        this.dbManager = DBManager.getInstance();
        this.dgDBp = new DragonGenomeDBProcessor(dbManager);
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


    public boolean add(Dragon element, User user){
        boolean flag = dbManager.insertDragon(element, element.creatorId());

        if (flag){
            System.out.println("Added Dragon " + element.creatorId());
        }
        else {
            System.out.println("Failed to add Dragon " + element.creatorId());
        }

        load();

        Optional idOptional = collection.stream()
                .filter(Objects::nonNull)
                .filter(d -> Objects.equals(d.name(), element.name()))
                .filter(d -> Objects.equals(d.age(), element.age()))
                .filter(d -> Objects.equals(d.dragonType(), element.dragonType()))
                .filter(d -> Objects.equals(d.dragonCharacter(), element.dragonCharacter()))
                .map(Dragon::id)
                .findFirst();

        long id = (long) idOptional.get();

        if (idOptional.isEmpty()) {

        }

        Genome dragonGenome = element.genome();

        if (dragonGenome == null) {
            GenomeGenerator gg = new GenomeGenerator();
            Genome genome = gg.generate(id);
            element.genome(genome);
            dragonGenome = genome;
        }

        try{
            dgDBp.insert(id, dragonGenome);
        } catch (SQLException e){
            e.printStackTrace();
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
        System.out.println(collection);
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
        collection = Collections.synchronizedList(new LinkedList<>());
        try {
            HashMap<Integer, User> users = dbManager.selectUser();
            for (Dragon dragon : dbManager.selectDragon()) {
                collection.add(dragon);
                Genome genome = dgDBp.select(dragon.id());

                if (genome == null){
                    GenomeGenerator gg = new GenomeGenerator();
                    genome =  gg.generate(dragon.id());
                    dgDBp.insert(dragon.id(), genome);
                }
                dragon.genome(genome);
                dragonUserMap.put(dragon, users.get((int) (dragon.creatorId()-1)));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }

    public boolean update(int id, Dragon element, User user){
        boolean flag = false;

        if (element.genome() == null) {
            GenomeGenerator gg = new GenomeGenerator();
            Genome genome = gg.generate(id);
            element.genome(genome);
        }
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
            try {
                dgDBp.insert(element.id(), element.genome());
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        return flag;
    }
}
