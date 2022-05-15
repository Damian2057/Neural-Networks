package Neu.Network.model.flower;

import Neu.Network.model.exceptions.model.ShapeException;
import java.io.Serializable;

public class Iris implements Serializable {
    private final double sepalLength;
    private final double sepalWidth;
    private final double petalLength;
    private final double petalWidth;
    private final int type;
    private final double[] matrixFigure = new double[5];
    private final double[][] pattern = new double[4][1];

    public Iris(String sepalLength, String sepalWidth, String petalLength, String petalWidth, String type) {
        this.sepalLength = Double.parseDouble(sepalLength);
        this.sepalWidth = Double.parseDouble(sepalWidth);
        this.petalLength = Double.parseDouble(petalLength);
        this.petalWidth = Double.parseDouble(petalWidth);
        this.type = Integer.parseInt(type);
        initIris();
        initPattern();
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

    private void initIris() {
        matrixFigure[0] = getPetalWidth();
        matrixFigure[1] = getPetalLength();
        matrixFigure[2] = getSepalWidth();
        matrixFigure[3] = getSepalLength();
        matrixFigure[4] = getType();
    }

    private void initPattern() {
        if(getType() == 0) {
            pattern[0][0] = 1;
            pattern[1][0] = 0;
            pattern[2][0] = 0;
            pattern[3][0] = 0;
        } else if(getType() == 1) {
            pattern[0][0] = 0;
            pattern[1][0] = 1;
            pattern[2][0] = 0;
            pattern[3][0] = 0;
        } else {
            pattern[0][0] = 0;
            pattern[1][0] = 0;
            pattern[2][0] = 1;
            pattern[3][0] = 0;
        }
    }

    public double getFeatures(int number) {
        if(number > matrixFigure.length) {
            throw new ShapeException("An index outside the characteristics of the flower");
        }
        return matrixFigure[number];
    }

    public double[][] getPattern() {
        return pattern;
    }
}