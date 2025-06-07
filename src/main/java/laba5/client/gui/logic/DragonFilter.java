package laba5.client.gui.logic;

import laba5.common.model.Coordinates;
import laba5.common.model.Dragon;
import laba5.common.model.Location;
import laba5.common.model.Person;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DragonFilter {

    private String nameFilter = "";
    private String ageFilter = "";
    private String coordsFilter = "";
    private String dateFilter = "";
    private String typeFilter = "";
    private String characterFilter = "";
    private String killerFilter = "";
    private String locationFilter = "";

    private Comparator<Dragon> currentComparator = Comparator.comparing(Dragon::id);

    public void setNameFilter(String filter) { this.nameFilter = filter.trim().toLowerCase(); }
    public void setAgeFilter(String filter) { this.ageFilter = filter.trim(); }
    public void setCoordsFilter(String filter) { this.coordsFilter = filter.trim().toLowerCase(); }
    public void setDateFilter(String filter) { this.dateFilter = filter.trim().toLowerCase(); }
    public void setTypeFilter(String filter) { this.typeFilter = filter.trim().toLowerCase(); }
    public void setCharacterFilter(String filter) { this.characterFilter = filter.trim().toLowerCase(); }
    public void setKillerFilter(String filter) { this.killerFilter = filter.trim().toLowerCase(); }
    public void setLocationFilter(String filter) { this.locationFilter = filter.trim().toLowerCase(); }

    public void setCurrentComparator(Comparator<Dragon> comparator) {
        this.currentComparator = comparator;
    }

    public List<Dragon> apply(List<Dragon> dragons) {
        return dragons.stream()
                .filter(byName())
                .filter(byAge())
                .filter(byCoordinates())
                .filter(byCreationDate())
                .filter(byType())
                .filter(byCharacter())
                .filter(byKiller())
                .filter(byLocation())
                .sorted(currentComparator)
                .collect(Collectors.toList());
    }

    private Predicate<Dragon> byName() {
        return d -> nameFilter.isEmpty() || d.name().toLowerCase().contains(nameFilter);
    }

    private Predicate<Dragon> byAge() {
        if (ageFilter.isEmpty()) return d -> true;
        try {
            long age = Long.parseLong(ageFilter);
            return d -> d.age() == age;
        } catch (NumberFormatException ex) {
            return d -> false;
        }
    }

    private Predicate<Dragon> byCoordinates() {
        return d -> {
            if (coordsFilter.isEmpty()) return true;
            Coordinates c = d.coordinates();
            if (c == null) return false;
            return ("X=" + c.x() + ", Y=" + c.y()).toLowerCase().contains(coordsFilter);
        };
    }

    private Predicate<Dragon> byCreationDate() {
        return d -> dateFilter.isEmpty() || d.creationDate().toString().toLowerCase().contains(dateFilter);
    }

    private Predicate<Dragon> byType() {
        return d -> typeFilter.isEmpty() || d.dragonType().toString().toLowerCase().contains(typeFilter);
    }

    private Predicate<Dragon> byCharacter() {
        return d -> characterFilter.isEmpty() || d.dragonCharacter().toString().toLowerCase().contains(characterFilter);
    }

    private Predicate<Dragon> byKiller() {
        return d -> {
            if (killerFilter.isEmpty()) return true;
            Person k = d.killer();
            return k != null && k.name().toLowerCase().contains(killerFilter);
        };
    }

    private Predicate<Dragon> byLocation() {
        return d -> {
            if (locationFilter.isEmpty()) return true;
            Person k = d.killer();
            if (k == null || k.location() == null) return false;

            Location l = k.location();
            String locStr = "X=" + l.x() + ", Y=" + l.y() + ", Z=" + l.z();
            return locStr.toLowerCase().contains(locationFilter);
        };
    }
}