package laba5.common.dataExchanging;

import laba5.common.model.Coordinates;
import laba5.common.model.Person;
import laba5.common.model.modelEnums.DragonCharacter;
import laba5.common.model.modelEnums.DragonType;

import java.io.Serializable;

public record AdditionalUserData(String name, long age, String description, DragonType dragonType,
                                 DragonCharacter dragonCharacter, Coordinates coord, Person person) implements Serializable {
}
