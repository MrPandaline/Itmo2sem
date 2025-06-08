package laba5.common.genetics;

import java.util.Random;

/**
 * Генерирует геном дракона на основе заданного сида (id дракона).
 */
public class GenomeGenerator {

    // Возможные аллели для каждого гена
    private static final char[][] EYE_GENE_ALLELES = {{'E', 'e'}};           // E/e - cute/normal/stars
    private static final char[][] WING_SIZE_GENE_ALLELES = {{'W', 'w'}};       // W/w - large/medium/small
    private static final char[][] HORN_GENE_ALLELES = {{'H', 'h'}};           // H/h - large/medium/small
    private static final char[][] PATTERN_GENE_ALLELES = {{'P', 'p'}};        // S/s - spotted/lined/empty

    /**
     * Генерирует геном дракона на основе заданного сида.
     *
     * @param seed сид для детерминированной генерации (id дракона)
     * @return новый объект Genome
     */
    public Genome generate(long seed) {
        Random random = new Random(seed);

        Gene eyeGene = generateGene(random, EYE_GENE_ALLELES);
        Gene wingSizeGene = generateGene(random, WING_SIZE_GENE_ALLELES);
        Gene hornGene = generateGene(random, HORN_GENE_ALLELES);
        Gene patternGene = generateGene(random, PATTERN_GENE_ALLELES);

        return new Genome(eyeGene, wingSizeGene, hornGene, patternGene);
    }

    /**
     * Генерирует случайную пару аллелей для одного гена.
     *
     * @param random генератор случайных чисел
     * @param possibleAlleles возможные пары аллелей
     * @return новый объект Gene
     */
    private Gene generateGene(Random random, char[][] possibleAlleles) {
        char[] alleles = possibleAlleles[random.nextInt(possibleAlleles.length)];

        char a1 = random.nextBoolean() ? alleles[0] : alleles[1];
        char a2 = random.nextBoolean() ? alleles[0] : alleles[1];

        return new Gene(a1, a2);
    }
}