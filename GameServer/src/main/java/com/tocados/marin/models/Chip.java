package com.tocados.marin.models;

import java.util.Objects;

public class Chip {
    private Integer x;
    private Integer y;
    private Integer value;

    public Chip(Integer x, Integer y, Integer value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    /**
     * Getters and Setters:
     */
    public Integer getX() {
        return this.x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return this.y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getValue() {
        return this.value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    // Overrided methods:
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Chip)) {
            return false;
        }
        Chip chip = (Chip) o;
        return Objects.equals(x, chip.x) && Objects.equals(y, chip.y) && Objects.equals(value, chip.value);
    }

    @Override
    public String toString() {
        return "{" +
                " x='" + getX() + "'" +
                ", y='" + getY() + "'" +
                ", value='" + getValue() + "'" +
                "}";
    }
}
