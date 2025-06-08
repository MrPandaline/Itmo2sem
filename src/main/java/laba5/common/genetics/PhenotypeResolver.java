package laba5.common.genetics;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class PhenotypeResolver {

    private final Map<String, Image> genotypeImages = new HashMap<>();

    public PhenotypeResolver(String basePath) {
        genotypeImages.put("HOMOZYGOUS_DOMINANT", new Image(basePath + "3.png"));
        genotypeImages.put("HETEROZYGOUS", new Image(basePath + "2.png"));
        genotypeImages.put("HOMOZYGOUS_RECESSIVE", new Image(basePath + "1.png"));
    }

    public Image resolve(Gene gene) {
        GenotypeType type = GenotypeType.fromGene(gene);
        return genotypeImages.get(type.name());
    }
}