package laba5.common.dataExchanging;

import laba5.common.model.Coordinates;
import laba5.common.model.Dragon;
import laba5.common.model.Person;
import laba5.common.model.modelEnums.DragonCharacter;
import laba5.common.model.modelEnums.DragonType;

import java.time.ZonedDateTime;

public record UnfinishedDragon(String name, Coordinates coords, long age, String description,
                               DragonType dragonType, DragonCharacter dragonCharacter, Person killer) {

    public Dragon buildDragon(ZonedDateTime creationTime, long creatorId) {
        return new Dragon(name, coords, age, description, dragonType, dragonCharacter,
                killer, -1, creationTime, creatorId);
    }
    public Dragon buildDragon(long creatorId){
        return new Dragon(name, coords, age, description, dragonType, dragonCharacter, killer, creatorId);
    }
}

