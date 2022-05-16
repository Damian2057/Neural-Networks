package Neu.Network.charts;

import java.io.Serializable;

public class Cord implements Serializable {
    private final double x;
    private final double y;

    public Cord(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
