package ru.itmo.java.homoursus.laba5.model.classes;

public class Coordinates {
    private float x; //Значение поля должно быть больше -589
    private int y;
    
    public Coordinates(float x, int y) {
      this.x = x;
      this.y = y;
    }

    public float getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
