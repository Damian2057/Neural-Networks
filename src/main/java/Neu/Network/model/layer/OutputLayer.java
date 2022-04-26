package Neu.Network.model.layer;

import Neu.Network.model.component.Neural;

import java.io.Serializable;
import java.util.ArrayList;

public class OutputLayer implements Serializable {
    private ArrayList<Neural> neurals = new ArrayList<>();

    public OutputLayer(int countOfWeights, int countOfNeurals) {
        for (int i = 0; i < countOfNeurals; i++) {
            neurals.add(new Neural(countOfWeights));
        }
    }


    public ArrayList<Double> calculate(ArrayList<Double> data) {
        ArrayList<Double> resultSet = new ArrayList<>();
        for (int i = 0; i < neurals.size(); i++) {
            resultSet.add(neurals.get(i).calculate(data.get(0),data.get(1)));
        }
        return resultSet;
    }




}
