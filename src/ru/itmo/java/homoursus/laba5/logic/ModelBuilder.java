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


//TODO: добававить обработку ошибок связанных с парсерами
//TODO: добавить валидатор модели
public class ModelBuilder {

    private final IIOManager ioManager;

    public ModelBuilder(IIOManager ioManager) {
        this.ioManager = ioManager;
    }

    public Dragon handleDragon() {
        ioManager.writeMessage("Введите имя дракона: ");
        String name = ioManager.getValidRawInput(a -> (a != null));
        ioManager.writeMessage("Введите координаты дракона: \n" );
        ioManager.getValidRawInput(a -> (a != null));
        Coordinates coordinates = handleCoordinates();
        ioManager.writeMessage("Введите возраст дракона: ");
        long age = ioManager.getValidDigit(Long::parseLong, a -> (a > 0));
        ioManager.writeMessage("Введите описание дракона: ");
        String description = ioManager.getRawInput();
        ioManager.writeMessage("Введите тип дракона: \n");
        DragonType dragonType = handleEnum(DragonType.FIRE, true);
        ioManager.writeMessage("Введите характер дракона: \n");
        DragonCharacter dragonCharacter = handleEnum(DragonCharacter.EVIL, false);
        ioManager.writeMessage("Введите убийцу дракона: \n");
        String nullKillerFlag = ioManager.getRawInput();
        Person killer;
        if (nullKillerFlag != null) {
            killer = handlePerson();
        }
        else {
            killer = null;
        }
        ioManager.writeMessage("Дракон успешно добавлен в коллекцию!\n");
        return new Dragon(name, coordinates, age, description, dragonType, dragonCharacter, killer);
    }

    public Location handleLocation() {
        ioManager.writeMessage("Введите координату x: ");
        float x = ioManager.getValidDigit(Float::parseFloat, a -> (a > -589));
        ioManager.writeMessage("Введите координату y: ");
        double y = ioManager.getDigit(Double::parseDouble);
        ioManager.writeMessage("Введите координату z: ");
        Integer z = ioManager.getDigit(Integer::parseInt);
        return new Location(x, y, z);
    }

    public Person handlePerson() {
        ioManager.writeMessage("Введите имя человека: ");
        String name = ioManager.getRawInput();
        ioManager.writeMessage("Введите рост человека: ");
        int height = ioManager.getValidDigit(Integer::parseInt, a -> (a > 0));
        ioManager.writeMessage("Введите цвет глаз человека: ");
        Color eyeColor = handleEnum(Color.BLACK, false);
        ioManager.writeMessage("Введите цвет волос человека: ");
        Color hairColor = handleEnum(Color.BLACK, false);
        ioManager.writeMessage("Введите национальность человека: ");
        Country nationality = handleEnum(Country.CHINA, false);
        ioManager.writeMessage("Введите локацию: ");
        Location location = handleLocation();

        return new Person(name, height, eyeColor, hairColor, nationality, location);

    }
    public Coordinates handleCoordinates() {
        ioManager.writeMessage("Введите координату x: ");
        float x = ioManager.getValidDigit(Float::parseFloat, a -> (a > -589));
        ioManager.writeMessage("Введите координату y: ");
        int y = ioManager.getDigit(Integer::parseInt);
        return new Coordinates(x, y);
    }

    private <T extends Enum<T>> T handleEnum(T enumClass, boolean canBeNull){
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
