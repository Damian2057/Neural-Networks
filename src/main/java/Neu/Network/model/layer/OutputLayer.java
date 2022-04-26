package Neu.Network.model.layer;

import Neu.Network.model.component.Neural;

import java.io.Serializable;
import java.util.ArrayList;

public class OutputLayer implements Serializable, Layer {
    private final ArrayList<Neural> neurals = new ArrayList<>();

    public OutputLayer(int countOfWeights, int countOfNeurals) {
        for (int i = 0; i < countOfNeurals; i++) {
            neurals.add(new Neural(countOfWeights));
        }
    }

    public ArrayList<Double> calculate(ArrayList<Double> data) {
        ArrayList<Double> resultSet = new ArrayList<>();
        for (Neural neural : neurals) {
            resultSet.add(neural.calculate(data.get(0), data.get(1)));
        }
        return resultSet;
    }

    @Override
    public void updateWeight(int numberOfNeural, int numberOfWeight, double value) {
        neurals.get(numberOfNeural).updateWeight(numberOfWeight,value);
    }
}
