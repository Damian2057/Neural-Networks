package Neu.Network.model.flower;

import java.io.Serializable;

public class Irys implements Serializable {
    private double sepalLength;
    private double sepalWidth;
    private double petalLength;
    private double petalWidth;
    private int type;

    public Irys(double sepalLength, double sepalWidth, double petalLength, double petalWidth, int type) {
        this.sepalLength = sepalLength;
        this.sepalWidth = sepalWidth;
        this.petalLength = petalLength;
        this.petalWidth = petalWidth;
        this.type = type;
    }

    public double getSepalLength() {
        return sepalLength;
    }

    public void setSepalLength(double sepalLength) {
        this.sepalLength = sepalLength;
    }

    public double getSepalWidth() {
        return sepalWidth;
    }

    public void setSepalWidth(double sepalWidth) {
        this.sepalWidth = sepalWidth;
    }

    public double getPetalLength() {
        return petalLength;
    }

    public void setPetalLength(double petalLength) {
        this.petalLength = petalLength;
    }

    public double getPetalWidth() {
        return petalWidth;
    }

    public void setPetalWidth(double petalWidth) {
        this.petalWidth = petalWidth;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
