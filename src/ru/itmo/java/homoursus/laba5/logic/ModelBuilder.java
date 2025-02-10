package ru.itmo.java.homoursus.laba5.logic;

import ru.itmo.java.homoursus.laba5.input.IIOManager;
import ru.itmo.java.homoursus.laba5.model.Coordinates;
import ru.itmo.java.homoursus.laba5.model.Dragon;
import ru.itmo.java.homoursus.laba5.model.Location;
import ru.itmo.java.homoursus.laba5.model.Person;
import ru.itmo.java.homoursus.laba5.model.modelEnums.Color;
import ru.itmo.java.homoursus.laba5.model.modelEnums.Country;
import ru.itmo.java.homoursus.laba5.model.modelEnums.DragonCharacter;
import ru.itmo.java.homoursus.laba5.model.modelEnums.DragonType;

import java.util.ArrayList;


/**
 * Класс, создающий модель по вводу пользователя.
 * @author Homoursus
 * @version 1.0
 */
public class ModelBuilder {

    /** Ссылка на используемую реализацию менеджера ввода-вывода.*/
    private final IIOManager ioManager;

    /**
     * Конструктор класса.
     * @param ioManager Используемый менеджер ввода-вывода
     * */
    public ModelBuilder(IIOManager ioManager) {
        this.ioManager = ioManager;
    }

    /** Метод, создающий объект класса Dragon по вводу пользователя.*/
    public Dragon buildDragon() {
        ioManager.writeMessage("Введите имя дракона: ");
        String name = ioManager.getValidRawInput(a -> (a != null));
        ioManager.writeMessage("Введите координаты дракона: \n" );
        ioManager.getValidRawInput(a -> (a != null));
        Coordinates coordinates = buildCoordinates();
        ioManager.writeMessage("Введите возраст дракона: ");
        long age = ioManager.getValidDigit(Long::parseLong, a -> (a > 0));
        ioManager.writeMessage("Введите описание дракона: ");
        String description = ioManager.getRawInput();
        ioManager.writeMessage("Введите тип дракона: \n");
        DragonType dragonType = buildEnum(DragonType.FIRE, true);
        ioManager.writeMessage("Введите характер дракона: \n");
        DragonCharacter dragonCharacter = buildEnum(DragonCharacter.EVIL, false);
        ioManager.writeMessage("Введите убийцу дракона: \n");
        String nullKillerFlag = ioManager.getRawInput();
        Person killer;
        if (nullKillerFlag != null) {
            killer = buildPerson();
        }
        else {
            killer = null;
        }
        ioManager.writeMessage("Дракон успешно добавлен в коллекцию!\n");
        return new Dragon(name, coordinates, age, description, dragonType, dragonCharacter, killer);
    }

    /** Метод, создающий объект класса Location по вводу пользователя.*/
    public Location buildLocation() {
        ioManager.writeMessage("Введите координату x: ");
        float x = ioManager.getValidDigit(Float::parseFloat, a -> (a > -589));
        ioManager.writeMessage("Введите координату y: ");
        double y = ioManager.getDigit(Double::parseDouble);
        ioManager.writeMessage("Введите координату z: ");
        Integer z = ioManager.getDigit(Integer::parseInt);
        return new Location(x, y, z);
    }

    /** Метод, создающий объект класса Person по вводу пользователя.*/
    public Person buildPerson() {
        ioManager.writeMessage("Введите имя человека: ");
        String name = ioManager.getRawInput();
        ioManager.writeMessage("Введите рост человека: ");
        int height = ioManager.getValidDigit(Integer::parseInt, a -> (a > 0));
        ioManager.writeMessage("Введите цвет глаз человека: ");
        Color eyeColor = buildEnum(Color.BLACK, false);
        ioManager.writeMessage("Введите цвет волос человека: ");
        Color hairColor = buildEnum(Color.BLACK, false);
        ioManager.writeMessage("Введите национальность человека: ");
        Country nationality = buildEnum(Country.CHINA, false);
        ioManager.writeMessage("Введите локацию: ");
        Location location = buildLocation();

        return new Person(name, height, eyeColor, hairColor, nationality, location);

    }

    /** Метод, создающий объект класса Coordinates по вводу пользователя.*/
    public Coordinates buildCoordinates() {
        ioManager.writeMessage("Введите координату x: ");
        float x = ioManager.getValidDigit(Float::parseFloat, a -> (a > -589));
        ioManager.writeMessage("Введите координату y: ");
        int y = ioManager.getDigit(Integer::parseInt);
        return new Coordinates(x, y);
    }

    /** Параметризованный метод, получающий объект какого-либо Enum по вводу пользователя.*/
    private <T extends Enum<T>> T buildEnum(T enumClass, boolean canBeNull){
        ArrayList<String> constants = new ArrayList<>();
        for (Enum<?> enumeration : enumClass.getDeclaringClass().getEnumConstants()){
            ioManager.writeMessage(enumeration + " ");
            constants.add(enumeration.name());
        }
        ioManager.writeMessage("\n");
        String input = ioManager.getConstantString(constants, canBeNull);
        if (input != null) {
            return T.valueOf(enumClass.getDeclaringClass(), input);
        } else {
            return null;
        }

    }
}
