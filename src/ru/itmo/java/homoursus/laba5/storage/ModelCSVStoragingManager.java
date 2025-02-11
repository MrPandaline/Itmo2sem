package ru.itmo.java.homoursus.laba5.storage;

import ru.itmo.java.homoursus.laba5.input.IIOManager;
import ru.itmo.java.homoursus.laba5.model.Coordinates;
import ru.itmo.java.homoursus.laba5.model.Dragon;
import ru.itmo.java.homoursus.laba5.model.Location;
import ru.itmo.java.homoursus.laba5.model.Person;
import ru.itmo.java.homoursus.laba5.model.modelEnums.Color;
import ru.itmo.java.homoursus.laba5.model.modelEnums.Country;
import ru.itmo.java.homoursus.laba5.model.modelEnums.DragonCharacter;
import ru.itmo.java.homoursus.laba5.model.modelEnums.DragonType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

/**
 * Класс, управляющий записью (чтением) коллекции в .csv файл по пути <b>path</b> (из файла).
 * @author Homoursus
 * @version 1.0.1
 */
public class ModelCSVStoragingManager implements IModelStorageManager {
    /** Путь до файла, в который (из которого) будет происходить запись (чтение) коллекции.*/
    String path;

    /**
     * Конструктор менеджера записи (чтения) коллекции в (из) .csv файл(а).
     * @param path путь до .csv файла
     */
    public ModelCSVStoragingManager(String path) {
        this.path = path;
    }

    /** @see IModelStorageManager#writeToStorage */
    @Override
    public void writeToStorage( LinkedList<Dragon> collection) {
        final char SEPARATOR = ';';
        try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8)) {
            for (Dragon dragon : collection) {
                osw.write(dragon.name() + SEPARATOR);
                osw.write(String.valueOf(dragon.coordinates().x()) + SEPARATOR);
                osw.write(String.valueOf(dragon.coordinates().y()) + SEPARATOR);
                osw.write(String.valueOf(dragon.age()) + SEPARATOR);
                osw.write(dragon.description() + SEPARATOR);
                if (dragon.dragonType() != null) {
                    osw.write(dragon.dragonType().toString() + SEPARATOR);
                }
                else {
                    osw.write("null"+ SEPARATOR);
                }
                osw.write(dragon.dragonCharacter().toString() + SEPARATOR);
                if (dragon.killer() != null) {
                    osw.write(dragon.killer().name() + SEPARATOR);
                    osw.write(String.valueOf(dragon.killer().height()) + SEPARATOR);
                    osw.write(dragon.killer().eyeColor().toString() + SEPARATOR);
                    osw.write(dragon.killer().hairColor().toString() + SEPARATOR);
                    osw.write(dragon.killer().nationality().toString() + SEPARATOR);
                    osw.write(String.valueOf(dragon.killer().location().x()) + SEPARATOR);
                    osw.write(String.valueOf(dragon.killer().location().y()) + SEPARATOR);
                    osw.write(String.valueOf(dragon.killer().location().z()) + SEPARATOR);
                }
                else {
                    for (int i = 0; i < 8; i++) {
                        osw.write("null" + SEPARATOR);
                    }
                }
                osw.write(String.valueOf(dragon.id()) + SEPARATOR);
                osw.write(String.valueOf(dragon.creationDate()));
                osw.write("\n");
            }
        } catch (IOException e ) {
            e.printStackTrace();
        }
    }
    /** @see IModelStorageManager#readFromStorage  */
    @Override
    public LinkedList<Dragon> readFromStorage(IIOManager ioManager) {
        LinkedList<Dragon> collection = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            for (String l = br.readLine(); l != null; l = br.readLine()) {
                String[] line = l.split(";");
                String name = line[0];
                Coordinates coordinates = new Coordinates(Float.parseFloat(line[1]), Float.parseFloat(line[2]));
                long age = Long.parseLong(line[3]);
                String description;
                if (line[4].equals("null")) {
                    description = null;
                } else {
                 description = line[4];
                }
                DragonType dragonType;
                if (line[5].equals("null")) {
                    dragonType = null;
                } else {
                    dragonType = DragonType.valueOf(line[5]);
                }
                DragonCharacter dragonCharacter = DragonCharacter.valueOf(line[6]);
                Location location = new Location(Float.parseFloat(line[12]), Double.parseDouble(line[13]),
                        Integer.parseInt(line[14]));

                Person person;
                if (line[7].equals("null")) {
                    person = null;
                } else {
                    person = new Person(line[7], Integer.parseInt(line[8]), Color.valueOf(line[9]),
                            Color.valueOf(line[10]), Country.valueOf(line[11]), location);
                }
                long id = Long.parseLong(line[15]);
                java.time.ZonedDateTime creationDate = java.time.ZonedDateTime.parse(line[16]);
                Dragon dragon = new Dragon(name, coordinates, age, description, dragonType,
                        dragonCharacter, person, id, creationDate);
                collection.add(dragon);
            }
        } catch(FileNotFoundException e){
            ioManager.writeMessage("Файл коллекции не найден!\n");
        } catch(IOException e){
            throw new RuntimeException(e);
        }
        return collection;
    }

    /** @see IModelStorageManager#getNextID  */
    @Override
    public long getNextID(IIOManager ioManager) {
        long usedID = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            for (String l = br.readLine(); l != null; l = br.readLine()) {
                String[] line = l.split(";");
                if (usedID < Long.parseLong(line[15])) {
                    usedID = Long.parseLong(line[15]);
                }
            }
        } catch(FileNotFoundException e){
            ioManager.writeMessage("Файл коллекции не найден! Генерация id начнётся с 1.\n");
        } catch(IOException e){
            throw new RuntimeException(e);
        }
        return usedID + 1;
    }
}
