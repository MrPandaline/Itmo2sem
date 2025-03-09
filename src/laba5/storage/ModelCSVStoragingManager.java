package laba5.storage;

import laba5.input.IIOManager;
import laba5.model.Coordinates;
import laba5.model.Dragon;
import laba5.model.Location;
import laba5.model.Person;
import laba5.model.modelEnums.Color;
import laba5.model.modelEnums.Country;
import laba5.model.modelEnums.DragonCharacter;
import laba5.model.modelEnums.DragonType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

/**
 * Класс, управляющий записью (чтением) коллекции в .csv файл по пути <b>path</b> (из файла).
 * @author Homoursus
 * @version 1.1
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
        boolean haveIncorrectLines = false;
        LinkedList<Dragon> collection = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            for (String l = br.readLine(); l != null; l = br.readLine()) {
                String[] line = l.split(";");
                if (line.length == 17) {
                    String name = line[0];
                    Coordinates coordinates = new Coordinates(Float.parseFloat(line[1]), Integer.parseInt(line[2]));
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
                    Location location;
                    if (line[7].equals("null")) {
                        location = null;
                    } else {
                        location = new Location(Float.parseFloat(line[12]), Double.parseDouble(line[13]),
                                Integer.parseInt(line[14]));
                    }
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
                else{
                    haveIncorrectLines = true;
                }
            }
        } catch(FileNotFoundException e){
            ioManager.writeMessage("Файл коллекции не найден!\n", false);
        } catch(IOException e){
            ioManager.writeMessage("Что-то пошло не так... файл коллекции не был считан!\n", false);
        }
        if (haveIncorrectLines) {
            ioManager.writeMessage("Часть элементов коллекции не были считаны! Они исключены из коллекции.\n", false);
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
                if (line.length == 17 && usedID < Long.parseLong(line[15]) ) {
                    usedID = Long.parseLong(line[15]);
                }
            }
        } catch(FileNotFoundException e){
            ioManager.writeMessage("Файл коллекции не найден! Генерация id начнётся с 1.\n", false);
        } catch(IOException e){
            throw new RuntimeException(e);
        }
        return usedID + 1;
    }
}
