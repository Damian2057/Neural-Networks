package Neu.Network.model.components;

import Neu.Network.model.flower.Iris;
import java.util.ArrayList;

public interface Network {
    void trainNetwork(ArrayList<Iris> trainingData);
    ArrayList<Double> calculate(Iris flower);
}
