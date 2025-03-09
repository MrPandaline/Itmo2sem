package laba5.model;

/**
 * Класс, хранящий локацию. Используется в модели.
 * @see Person
 * @version 1.1
 */
public class Location extends AbstractModel{
    /**
     * Координата x локации.
     */
    private float x;
    /**
     * Координата y локации.
     */
    private double y;
    /**
     * Координата z локации.
     * Значение не может быть null
     */
    private Integer z; //Поле не может быть null
    
    public Location(float x, double y, Integer z) {
      this.x = x;
      this.y = y;
      this.z = z;
    }

    public double y() {
        return y;
    }

    public float x() {
        return x;
    }

    public Integer z() {
        return z;
    }
}