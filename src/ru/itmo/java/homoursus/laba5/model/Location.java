package ru.itmo.java.homoursus.laba5.model;

public class Location {
    private float x;
    private double y;
    private Integer z; //Поле не может быть null
    
    public Location(float x, double y, Integer z) {
      this.x = x;
      this.y = y;
      this.z = z;
    }

    public double getY() {
        return y;
    }

    public float getX() {
        return x;
    }

    public Integer getZ() {
        return z;
    }
}