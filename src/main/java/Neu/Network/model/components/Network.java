package Neu.Network.model.components;

import Neu.Network.model.flower.EncoderData;
import java.util.ArrayList;

public interface Network {
    void trainNetwork(ArrayList<EncoderData> trainingData);
    ArrayList<Double> calculate(EncoderData flower);
}
