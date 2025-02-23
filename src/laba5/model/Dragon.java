package laba5.model;

import laba5.model.modelEnums.DragonCharacter;
import laba5.model.modelEnums.DragonType;

/**
 * Класс, хранящий дракона. Он содержится в коллекции. Реализует интерфейс Comparable.
 * @version 1.1
 */
public class Dragon extends AbstractModel implements Comparable<Dragon> {
    /**
     * Генератор id генерирует просто последовательные числа, начиная с последнего использованного.
     */
    static private long idGenerator = 1;

    /**
     * Идентификатор дракона.
     * Значение должно быть больше 0, Значение должно быть уникальным, Значение должно генерироваться автоматически.
     */
    private long id;
    /**
     * Имя дракона.
     * Не может быть null, Строка не может быть пустой.
     */
    private String name; //Поле не может быть null, Строка не может быть пустой
    /**
     * Координаты дракона.
     * Не может быть null.
     */
    private Coordinates coordinates;
    /**
     * Дата создания дракона.
     * Не может быть null, Значение этого поля должно генерироваться автоматически.
     */
    private java.time.ZonedDateTime creationDate;//Поле не может быть null, Значение этого поля должно генерироваться автоматически
    /**
     * Возраст дракона.
     * Значение должно быть больше 0.
     */
    private long age;
    /**
     * Описание дракона.
     * Может быть null.
     */
    private String description;
    /**
     * Тип дракона.
     * Может быть null
     */
    private DragonType type;
    /**
     * Характер дракона.
     * Не может быть null.
     */
    private DragonCharacter character;

    /**
     * Убийца дракона.
     * Может быть null.
     */
    private Person killer; //Поле может быть null
    
    { 
        this.id = idGenerator++;
        this.creationDate = java.time.ZonedDateTime.now();
    }
    
    public Dragon(String name, Coordinates coordinates, long age, String description,
                  DragonType type, DragonCharacter character, Person killer) {
        this.name = name;
        this.coordinates = coordinates;
        this.age = age;
        this.description = description;
        this.type = type;
        this.character = character;
        this.killer = killer;
    }

    public Dragon(String name, Coordinates coordinates, long age, String description,
                  DragonType type, DragonCharacter character, Person killer, long id, java.time.ZonedDateTime creationDate) {
        this.name = name;
        this.coordinates = coordinates;
        this.age = age;
        this.description = description;
        this.type = type;
        this.character = character;
        this.killer = killer;
        this.id = id;
        this.creationDate = creationDate;
    }

    public DragonCharacter dragonCharacter() {
        return character;
    }

    public DragonType dragonType() {
        return type;
    }

    public Coordinates coordinates() {
        return coordinates;
    }

    public long age() {
        return age;
    }

    public long id() {
        return id;
    }

    public Person killer() {
        return killer;
    }

    public String description() {
        return description;
    }

    public String name() {
        return name;
    }

    public java.time.ZonedDateTime creationDate() { return creationDate; }

    /** Устанавливает значения, начиная с которого будут генерироваться id элементов коллекции*/
    public static void setIdGenerator(long idGenerator) {
        Dragon.idGenerator = idGenerator;
    }

    @Override
    public int compareTo(Dragon o) {
        return Long.compare(o.id(), id);
    }
}