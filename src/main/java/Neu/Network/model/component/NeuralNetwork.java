package Neu.Network.model.component;

import Neu.Network.model.flower.Irys;
import java.io.Serializable;
import java.util.ArrayList;

public class NeuralNetwork implements Serializable {
    private final double learningFactor;
    private final double momentumFactor;

    public NeuralNetwork(double learningFactor, double momentumFactor) {
        this.learningFactor = learningFactor;
        this.momentumFactor = momentumFactor;
    }

    public double getLearningFactor() {
        return learningFactor;
    }

    public double getMomentumFactor() {
        return momentumFactor;
    }
}
