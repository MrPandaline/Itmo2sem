package laba5.common.genetics;

import java.io.Serializable;

public class Genome implements Serializable {
    private final Gene eyeGene;
    private final Gene wingSizeGene;
    private final Gene hornGene;
    private final Gene patternGene;

    public Genome(Gene eyeColorGene, Gene wingSizeGene, Gene hornGene, Gene scalePatternGene){
        this.eyeGene = eyeColorGene;
        this.wingSizeGene = wingSizeGene;
        this.hornGene = hornGene;
        this.patternGene = scalePatternGene;
    }

    public Gene getEyeGene() { return eyeGene; }
    public Gene getWingSizeGene() { return wingSizeGene; }
    public Gene getHornGene() { return hornGene; }
    public Gene getPatternGene() { return patternGene; }
}
