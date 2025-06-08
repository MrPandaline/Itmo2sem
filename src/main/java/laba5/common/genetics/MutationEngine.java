package laba5.common.genetics;

import java.util.Random;

public class MutationEngine {
    private final double mutationProbability;
    private final Random random = new Random();

    public MutationEngine(double mutationProbability) {
        if (mutationProbability < 0 || mutationProbability > 1) {
            throw new IllegalArgumentException("Вероятность мутации должна быть между 0 и 1");
        }
        this.mutationProbability = mutationProbability;
    }

    /**
     * Мутирует одну аллель.
     */
    private char mutateAllele(char allele) {
        if (Character.isUpperCase(allele)) {
            return Character.toLowerCase(allele);
        } else {
            return Character.toUpperCase(allele);
        }
    }

    /**
     * Применяет мутацию к гену.
     * @param gene исходный ген
     * @return мутировавший ген (или оригинальный)
     */
    public Gene mutate(Gene gene) {
        char a1 = gene.allele1;
        char a2 = gene.allele2;

        if (random.nextDouble() < mutationProbability) {
            a1 = mutateAllele(a1);
        }

        if (random.nextDouble() < mutationProbability) {
            a2 = mutateAllele(a2);
        }

        return new Gene(a1, a2);
    }
}
