package laba5.common.genetics;

import javafx.scene.image.Image;
import laba5.common.model.modelEnums.DragonType;

public class DragonTypeResolver {

    private final String basePath;

    public DragonTypeResolver(String basePath) {
        this.basePath = basePath;
    }

    public Image resolve(DragonType dragonType) {
        String filename = dragonType.name().toLowerCase() + ".png";
        return new Image(basePath + filename);
    }
}