package Neu.Network.model.components;

import Neu.Network.model.flower.Iris;
import java.util.ArrayList;

public interface Network {
    public void trainNetwork(ArrayList<Iris> trainingData);
    public ArrayList<Double> calculate(Iris flower);
}
