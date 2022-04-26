package Neu.Network.model.flower;

import java.io.Serializable;

public class Iris implements Serializable {
    private final double sepalLength;
    private final double sepalWidth;
    private final double petalLength;
    private final double petalWidth;
    private final int type;

    public Iris(String sepalLength, String sepalWidth, String petalLength, String petalWidth, String type) {
        this.sepalLength = Double.parseDouble(sepalLength);
        this.sepalWidth = Double.parseDouble(sepalWidth);
        this.petalLength = Double.parseDouble(petalLength);
        this.petalWidth = Double.parseDouble(petalWidth);
        this.type = Integer.parseInt(type);
    }

    public double getSepalLength() {
        return sepalLength;
    }

    public double getSepalWidth() {
        return sepalWidth;
    }

    public double getPetalLength() {
        return petalLength;
    }

    public double getPetalWidth() {
        return petalWidth;
    }

    public int getType() {
        return type;
    }

}
