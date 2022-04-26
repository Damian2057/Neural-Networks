package Neu.Network.model.layer;

import Neu.Network.model.component.Neural;
import Neu.Network.model.flower.Irys;

import java.io.Serializable;
import java.util.ArrayList;

public class InputLayer implements Serializable, Layer {
    private ArrayList<Neural> neurals = new ArrayList<>();

    public InputLayer(int countOfWeights) {
        for (int i = 0; i < countOfWeights; i++) {
            neurals.add(new Neural(countOfWeights));
        }
    }

    public ArrayList<Double> calculate(Irys flower) {
        ArrayList<Double> resultSet = new ArrayList<>();
        for (int i = 0; i < neurals.size(); i++) {
            resultSet.add(neurals.get(i).calculate(flower));
        }
        return resultSet;
    }

    @Override
    public void updateWeight(int numberOfNeural, int numberOfWeight, double value) {
        neurals.get(numberOfNeural).updateWeight(numberOfWeight,value);
    }
}
