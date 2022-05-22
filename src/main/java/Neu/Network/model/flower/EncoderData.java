package Neu.Network.model.flower;

import Neu.Network.model.exceptions.model.ShapeException;
import java.io.Serializable;

public class EncoderData implements Serializable {
    private final double dataOne;
    private final double dataSecond;
    private final double dataThird;
    private final double dataFourth;
    private final double[] matrixFigure = new double[4];

    public double[] getMatrixFigure() {
        return matrixFigure;
    }

    public EncoderData(String sepalLength, String sepalWidth, String petalLength, String petalWidth) {
        this.dataOne = Double.parseDouble(sepalLength);
        this.dataSecond = Double.parseDouble(sepalWidth);
        this.dataThird = Double.parseDouble(petalLength);
        this.dataFourth = Double.parseDouble(petalWidth);
        initIris();
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
        matrixFigure[0] = getDataOne();
        matrixFigure[1] = getDataSecond();
        matrixFigure[2] = getDataThird();
        matrixFigure[3] = getDataFourth();
    }

    public double getFeatures(int number) {
        if(number > matrixFigure.length) {
            throw new ShapeException("An index outside the characteristics of the flower");
        }
        return matrixFigure[number];
    }
}