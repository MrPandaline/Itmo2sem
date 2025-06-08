package laba5.common.genetics;

import java.io.Serializable;

public class Gene implements Serializable {
    public final char allele1;
    public final char allele2;

    public Gene(char a1, char a2) {
        this.allele1 = a1;
        this.allele2 = a2;
    }

    public char getDominantAllele() {
        return Character.isUpperCase(allele1) ? allele1 : allele2;
    }

    public boolean isHomozygous() {
        return allele1 == allele2;
    }

    public boolean isRecessive() {
        return !Character.isUpperCase(allele1) && !Character.isUpperCase(allele2);
    }

    public static Gene fromString(String geneStr) {
        if (geneStr == null || geneStr.length() != 2) {
            throw new IllegalArgumentException("Неправильная строка гена: " + geneStr);
        }
        return new Gene(geneStr.charAt(0), geneStr.charAt(1));
    }

    public String toString() {
        return "" + allele1 + allele2;
    }

}