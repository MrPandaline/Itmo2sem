package laba5.model;

/**
 * Класс, хранящий координаты. Используется в модели.
 * @see Dragon
 * @version 1.1
 */
public class Coordinates extends AbstractModel{
    /**
     * Координата x
     * Значение должно быть больше -589
     */
    private float x;
    /** Координата y*///
    private int y;


    public Coordinates(float x, int y) {
      this.x = x;
      this.y = y;
    }

    public float x() {
        return x;
    }

    public int y() {
        return y;
    }
}
