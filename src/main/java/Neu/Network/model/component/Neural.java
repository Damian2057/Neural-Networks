package Neu.Network.model.component;

import Neu.Network.model.flower.Irys;
import Neu.Network.model.math.Math;
import java.io.Serializable;
import java.util.ArrayList;

public class Neural implements Serializable {

    private final int countOfWeights;
    private final Math functionality = new Math();
    private ArrayList<Double> weights = new ArrayList<>();

    public Neural(int countOfWeights) {
        this.countOfWeights = countOfWeights;
        initializeTheWeights();
    }

    private void initializeTheWeights() {
        for (int i = 0; i < countOfWeights; i++) {
            //(((java.lang.Math.random() % 1000000L) / 1700.0) - 9.8)*0.0015;
            weights.add(java.lang.Math.sin(java.lang.Math.random())/2);
        }
    }

    public int getCountOfWeights() {
        return countOfWeights;
    }

    public ArrayList<Double> getWeights() {
        return weights;
    }

    public void setWeights(ArrayList<Double> weights) {
        this.weights = weights;
    }

    public void updateWeight(int numberToUpdate, double value) {
        weights.set(numberToUpdate,value);
    }

    public double calculate(Irys flower) {
        double calculatedSum = 0.0;

        calculatedSum += flower.getPetalLength() * weights.get(0);
        calculatedSum += flower.getPetalWidth() * weights.get(1);
        calculatedSum += flower.getSepalLength() * weights.get(2);
        calculatedSum += flower.getSepalWidth() * weights.get(3);

        return functionality.sigmoid(calculatedSum);
    }

    public double calculate(double sum1, double sum2, double sum3 ,double sum4) {
        double calculatedSum = 0.0;

        calculatedSum += sum1 * weights.get(0);
        calculatedSum += sum2 * weights.get(1);
        calculatedSum += sum3 * weights.get(2);
        calculatedSum += sum4 * weights.get(3);

        return functionality.sigmoid(calculatedSum);
    }

    public double calculate(double sum1, double sum2) {
        double calculatedSum = 0.0;

        calculatedSum += sum1 * weights.get(0);
        calculatedSum += sum2 * weights.get(1);

        return functionality.sigmoid(calculatedSum);
    }
}
