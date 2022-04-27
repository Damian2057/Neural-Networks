package Neu.Network.model.component;

import Neu.Network.model.flower.Iris;
import Neu.Network.model.math.Sigmoid;
import org.jetbrains.annotations.NotNull;
import java.io.Serializable;

public class Layer implements Serializable {

    private final int numberOfNeurons;
    private final int numberOfInputs;
    private final double[][] weights;

    public Layer(int numberOfNeurons, int numberOfInputs) {
        this.numberOfNeurons = numberOfNeurons;
        this.numberOfInputs = numberOfInputs;
        weights = new double[numberOfNeurons][numberOfInputs];
        initializeTheWeights();
    }

    private void initializeTheWeights() {
        for(int i = 0; i < numberOfNeurons; i++) {
            for(int j = 0; j < numberOfInputs; j++) {
                double value  = Math.sin(java.lang.Math.random())/2;
                if(value == 0) {
                    value = 0.01492;
                }
                weights[i][j] = value;
            }
        }
    }

    public double[][] getWeights() {
        return weights;
    }

}
