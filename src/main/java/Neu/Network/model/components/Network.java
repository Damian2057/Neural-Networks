package Neu.Network.model.components;

import Neu.Network.model.flower.Iris;
import java.util.ArrayList;

public interface Network {
    void trainNetwork(ArrayList<Iris> trainingData, ArrayList<Iris> validationData);
    ArrayList<Double> calculate(Iris flower);
}
