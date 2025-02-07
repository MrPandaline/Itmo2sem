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

public class ModelHandler {

    private IIOManager ioManager;

    public ModelHandler(IIOManager ioManager) {
        this.ioManager = ioManager;
    }

    public Dragon handleDragon() {
        ioManager.writeMessage("Введите имя дракона: ");
        String name = ioManager.getInput();
        ioManager.writeMessage("Введите координаты дракона: ");
        Coordinates coordinates = handleCoordinates();
        ioManager.writeMessage("Введите возраст дракона: ");
        long age = Long.parseLong(ioManager.getInput());
        ioManager.writeMessage("Введите описание дракона: ");
        String description = ioManager.getInput();
        ioManager.writeMessage("Введите тип дракона: ");
        DragonType dragonType = handleEnum(DragonType.FIRE);
        ioManager.writeMessage("Введите характер дракона: ");
        DragonCharacter dragonCharacter = handleEnum(DragonCharacter.EVIL);
        ioManager.writeMessage("Введите убийцу дракона: ");
        Person killer = handlePerson();
        return new Dragon(name, coordinates, age, description, dragonType, dragonCharacter, killer);
    }

    public Location handleLocation() {
        ioManager.writeMessage("Введите координату x: ");
        float x = Float.parseFloat(ioManager.getInput());
        ioManager.writeMessage("Введите координату y: ");
        double y = Double.parseDouble(ioManager.getInput());
        ioManager.writeMessage("Введите координату z: ");
        Integer z = Integer.parseInt(ioManager.getInput());
        return new Location(x, y, z);
    }

    public Person handlePerson() {
        ioManager.writeMessage("Введите имя человека: ");
        String name = ioManager.getInput();
        ioManager.writeMessage("Введите рост человека: ");
        int height = Integer.parseInt(ioManager.getInput());
        ioManager.writeMessage("Введите цвет глаз человека: ");
        Color eyeColor = handleEnum(Color.BLACK);
        ioManager.writeMessage("Введите цвет волос человека: ");
        Color hairColor = handleEnum(Color.BLACK);
        ioManager.writeMessage("Введите национальность человека: ");
        Country nationality = handleEnum(Country.CHINA);
        ioManager.writeMessage("Введите локацию: ");
        Location location = handleLocation();

        return new Person(name, height, eyeColor, hairColor, nationality, location);

    }
    public Coordinates handleCoordinates() {
        ioManager.writeMessage("Введите координату x: ");
        float x = Float.parseFloat(ioManager.getInput());
        ioManager.writeMessage("Введите координату y: ");
        int y = Integer.parseInt(ioManager.getInput());
        return new Coordinates(x, y);
    }

    //TODO: Разобраться как сделать обобщённый метод, чтобы ловить все перечисления
    private <T extends Enum<T>> T handleEnum(T enumClass){
        for (Enum enumeration : enumClass.getDeclaringClass().getEnumConstants()){
            ioManager.writeMessage(enumeration + " ");
        }
        return T.valueOf(enumClass.getDeclaringClass() ,ioManager.getInput().toUpperCase());
    }
}
