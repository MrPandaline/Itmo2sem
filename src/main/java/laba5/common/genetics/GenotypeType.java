package laba5.common.genetics;

public enum GenotypeType {
    HOMOZYGOUS_DOMINANT,
    HETEROZYGOUS,
    HOMOZYGOUS_RECESSIVE;

    public static GenotypeType fromGene(Gene gene) {
        char a1 = gene.allele1;
        char a2 = gene.allele2;

        if (Character.isUpperCase(a1) && Character.isUpperCase(a2)) {
            return HOMOZYGOUS_DOMINANT;
        } else if ((Character.isUpperCase(a1) && Character.isLowerCase(a2)) ||
                (Character.isLowerCase(a1) && Character.isUpperCase(a2))) {
            return HETEROZYGOUS;
        } else {
            return HOMOZYGOUS_RECESSIVE;
        }
    }
}