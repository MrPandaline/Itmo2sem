package laba5.logic;

import laba5.input.IIOManager;
import laba5.model.Coordinates;
import laba5.model.Dragon;
import laba5.model.Location;
import laba5.model.Person;
import laba5.model.modelEnums.Color;
import laba5.model.modelEnums.Country;
import laba5.model.modelEnums.DragonCharacter;
import laba5.model.modelEnums.DragonType;

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

    boolean inQuiteMode = false;

    /** Метод, создающий объект класса Dragon по вводу пользователя.*/
    public Dragon buildDragon() {
        ioManager.writeMessage("Введите имя дракона: ", inQuiteMode);
        String name = ioManager.getValidRawInput(a -> (a != null));
        ioManager.writeMessage("Введите координаты дракона: \n", inQuiteMode);
        Coordinates coordinates = buildCoordinates();
        ioManager.writeMessage("Введите возраст дракона (целое число)\nЗначение должно быть в диапазоне (0; 10^6): ", inQuiteMode);
        long age = ioManager.getValidDigit(Long::parseLong, a -> (a > 0 && a < 1000000 ));
        ioManager.writeMessage("Введите описание дракона (можно оставить пустым): ", inQuiteMode);
        String description = ioManager.getRawInput();
        ioManager.writeMessage("Выберите тип дракона (можно оставить пусты): \n", inQuiteMode);
        DragonType dragonType = buildEnum(DragonType.FIRE, true);
        ioManager.writeMessage("Выберите характер дракона: \n", inQuiteMode);
        DragonCharacter dragonCharacter = buildEnum(DragonCharacter.EVIL, false);
        ioManager.writeMessage(
                "Вы хотите добавить убийцу дракона?\n"+
                "да - перейти к добавлению,\n"+
                "какой-либо другой набор символов - пропустить добавление\n"
                        ,inQuiteMode);
        String nullKillerFlag = ioManager.getRawInput();
        Person killer;
        if (nullKillerFlag != null && nullKillerFlag.equals("да")) {
            killer = buildPerson();
        }
        else {
            killer = null;
        }
        ioManager.writeMessage("Дракон успешно добавлен в коллекцию!\n", true);
        return new Dragon(name, coordinates, age, description, dragonType, dragonCharacter, killer);
    }

    /** Метод, создающий объект класса Location по вводу пользователя.*/
    public Location buildLocation() {
        ioManager.writeMessage("Введите координату x (дробное число). Значение должно быть больше -589: ", inQuiteMode);
        float x = ioManager.getValidDigit(Float::parseFloat, a -> (a > -589));
        ioManager.writeMessage("Введите координату y (дробное число): ", inQuiteMode);
        double y = ioManager.getDigit(Double::parseDouble);
        ioManager.writeMessage("Введите координату z (целое число): ", inQuiteMode);
        Integer z = ioManager.getDigit(Integer::parseInt);
        return new Location(x, y, z);
    }

    /** Метод, создающий объект класса Person по вводу пользователя.*/
    public Person buildPerson() {
        ioManager.writeMessage("Введите имя человека: ", inQuiteMode);
        String name = ioManager.getRawInput();
        ioManager.writeMessage("Введите рост человека в мм (целое число): ", inQuiteMode);
        int height = ioManager.getValidDigit(Integer::parseInt, a -> (a > 0 && a < 3000));
        ioManager.writeMessage("Введите цвет глаз человека: ", inQuiteMode);
        Color eyeColor = buildEnum(Color.BLACK, false);
        ioManager.writeMessage("Введите цвет волос человека: ", inQuiteMode);
        Color hairColor = buildEnum(Color.BLACK, false);
        ioManager.writeMessage("Введите национальность человека: ", inQuiteMode);
        Country nationality = buildEnum(Country.CHINA, false);
        ioManager.writeMessage("Введите локацию: \n", inQuiteMode);
        Location location = buildLocation();

        return new Person(name, height, eyeColor, hairColor, nationality, location);

    }

    /** Метод, создающий объект класса Coordinates по вводу пользователя.*/
    public Coordinates buildCoordinates() {
        ioManager.writeMessage("Введите координату x (дробное число). Значение должно быть больше -589: ", inQuiteMode);
        float x = ioManager.getValidDigit(Float::parseFloat, a -> (a > -589));
        ioManager.writeMessage("Введите координату y (целое число): ", inQuiteMode);
        int y = ioManager.getDigit(Integer::parseInt);
        return new Coordinates(x, y);
    }

    /** Параметризованный метод, получающий объект какого-либо Enum по вводу пользователя.*/
    private <T extends Enum<T>> T buildEnum(T enumClass, boolean canBeNull){
        ArrayList<String> constants = new ArrayList<>();
        for (Enum<?> enumeration : enumClass.getDeclaringClass().getEnumConstants()){
            ioManager.writeMessage(enumeration + " ", inQuiteMode);
            constants.add(enumeration.name());
        }
        ioManager.writeMessage("\n", inQuiteMode);
        String input = ioManager.getConstantString(constants, canBeNull);
        if (input != null) {
            return T.valueOf(enumClass.getDeclaringClass(), input);
        } else {
            return null;
        }

    }
}
