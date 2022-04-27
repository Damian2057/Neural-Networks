package Neu.Network.model.components;

import Neu.Network.model.exceptions.model.ShapeException;
import Neu.Network.model.flower.Iris;
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
                this.weights[i][j] = value;
            }
        }
    }

    public double[][] getWeights() {
        return weights;
    }


    public int getNumberOfNeurons() {
        return numberOfNeurons;
    }

    public int getNumberOfInputs() {
        return numberOfInputs;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < numberOfNeurons; i++)
        {
            for(int j = 0 ; j < numberOfInputs; j++)
            {
                stringBuilder.append(this.weights[i][j]).append(" ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public void add(int scalar) {
        for(int i = 0; i < numberOfNeurons; i++) {
            for(int j = 0; j < numberOfInputs; j++) {
                this.weights[i][j] += scalar;
            }
        }
    }

    public void add(@NotNull Layer layer) {
        if(numberOfNeurons != layer.getNumberOfNeurons() ||  numberOfInputs != layer.getNumberOfInputs()) {
            throw new ShapeException("Attempt to add matrices with incorrect sizes");
        }

        for(int i = 0; i < numberOfNeurons; i++) {
            for(int j = 0; j < numberOfInputs; j++) {
                this.weights[i][j] += layer.getWeights()[i][j];
            }
        }
    }

    public static Layer toLayer(Iris flower)
    {
        Layer temp = new Layer(4,1);
        temp.getWeights()[0][0] = flower.getPetalLength();
        temp.getWeights()[1][0] = flower.getPetalWidth();
        temp.getWeights()[2][0] = flower.getSepalLength();
        temp.getWeights()[3][0] = flower.getSepalWidth();

        return temp;
    }



}
