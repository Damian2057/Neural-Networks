package Neu.Network.model.flower;

import Neu.Network.model.exceptions.model.ShapeException;
import java.io.Serializable;

public class EncoderData implements Serializable {
    private final double dataOne;
    private final double dataSecond;
    private final double dataThird;
    private final double dataFourth;
    private final double[] matrixFigure = new double[5];
    private final double[][] pattern = new double[4][1];

    public EncoderData(String sepalLength, String sepalWidth, String petalLength, String petalWidth) {
        this.dataOne = Double.parseDouble(sepalLength);
        this.dataSecond = Double.parseDouble(sepalWidth);
        this.dataThird = Double.parseDouble(petalLength);
        this.dataFourth = Double.parseDouble(petalWidth);
        initIris();
        initPattern();
    }

    public int indexOfMax() {
        if(dataOne == 1) {
            return 0;
        } else if(dataSecond == 1) {
            return 1;
        } else if(dataThird == 1) {
            return 2;
        } else {
            return 3;
        }
    }

    public double getDataOne() {
        return dataOne;
    }

    public double getDataSecond() {
        return dataSecond;
    }

    public double getDataThird() {
        return dataThird;
    }

    public double getDataFourth() {
        return dataFourth;
    }

    private void initIris() {
        matrixFigure[0] = getDataFourth();
        matrixFigure[1] = getDataThird();
        matrixFigure[2] = getDataSecond();
        matrixFigure[3] = getDataOne();
    }

    private void initPattern() {
            pattern[0][0] = 1;
            pattern[1][0] = 0;
            pattern[2][0] = 0;
            pattern[3][0] = 0;
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