package laba5.common.genetics;

import laba5.common.dataExchanging.UnfinishedDragon;
import laba5.common.model.Dragon;
import laba5.common.model.modelEnums.DragonCharacter;
import laba5.common.model.modelEnums.DragonType;

public class DragonBreeder {

    private static final InheritanceInterpreter interpreter = new InheritanceInterpreter();
    private static final MutationEngine MUTATION_ENGINE = new MutationEngine(0.1);

    static {
        interpreter.addRule("random");
    }

    public static Gene breedGene(Gene mom, Gene dad) {
        char childAllele1 = interpreter.evaluate(mom, dad);
        char childAllele2 = interpreter.evaluate(dad, mom);
        Gene rawGene = new Gene(childAllele1, childAllele2);
        return MUTATION_ENGINE.mutate(rawGene);
    }

    public static Genome breedGenome(Genome mom, Genome dad) {
        return new Genome(
                breedGene(mom.getEyeGene(), dad.getEyeGene()),
                breedGene(mom.getWingSizeGene(), dad.getWingSizeGene()),
                breedGene(mom.getHornGene(), dad.getHornGene()),
                breedGene(mom.getPatternGene(), dad.getPatternGene())
        );
    }

    public static UnfinishedDragon breed(Dragon mother, Dragon father) {
        if (!mother.isAlive() || !father.isAlive()) {
            throw new IllegalArgumentException("Оба дракона должны быть живыми");
        }

        Genome childGenome = breedGenome(mother.genome(), father.genome());

        UnfinishedDragon child = new UnfinishedDragon(
                "Baby of " + mother.name() + " and " + father.name(),
                mother.coordinates(),
                1L,
                "Newborn dragon from breeding",
                combineTypes(mother.dragonType(), father.dragonType()),
                combineCharacters(mother.dragonCharacter(), father.dragonCharacter()),
                null,
                childGenome
        );
        return child;
    }

    private static DragonType combineTypes(DragonType t1, DragonType t2) {
        return Math.random() < 0.5 ? t1 : t2;
    }

    private static DragonCharacter combineCharacters(DragonCharacter c1, DragonCharacter c2) {
        return Math.random() < 0.5 ? c1 : c2;
    }
}