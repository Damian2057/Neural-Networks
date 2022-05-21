package Neu.Network.model.components;

import Neu.Network.model.exceptions.dao.CloneException;
import Neu.Network.model.exceptions.model.ShapeException;
import Neu.Network.model.flower.Iris;
import Neu.Network.model.math.Sigmoid;
import org.jetbrains.annotations.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Layer implements Serializable, Cloneable {

    private final int numberOfNeurons;
    private final int numberOfInputs;
    private double[][] weights;

    public Layer(int numberOfNeurons, int numberOfInputs) {
        this.numberOfNeurons = numberOfNeurons;
        this.numberOfInputs = numberOfInputs;
        weights = new double[numberOfNeurons][numberOfInputs];
        initializeTheWeights();
    }

    private void initializeTheWeights() {
        for(int i = 0; i < numberOfNeurons; i++) {
            for(int j = 0; j < numberOfInputs; j++) {
                double value  = drawWeights();
                if(value == 0) {
                    value = 0.01492;
                }
                this.weights[i][j] = value / Math.sqrt(numberOfInputs);
            }
        }
    }

    private double drawWeights() {
        int minValue = -100, maxValue = 100;
        Random theRandom = new Random();
        double theRandomValue;
        theRandomValue = minValue + (maxValue - minValue) * theRandom.nextDouble();
        return Math.sin(theRandomValue);
    }

    public double[][] getVector() {
        return weights;
    }

    public int getNumberOfNeurons() {
        return numberOfNeurons;
    }

    public int getNumberOfInputs() {
        return numberOfInputs;
    }

    public void setWeights(double[][] weights) {
        this.weights = weights;
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

    @Override
    public Layer clone() {
        try {
            Layer clone = (Layer) super.clone();
            double[][] weightClone = new double[numberOfNeurons][numberOfInputs];
            for (int i = 0; i < numberOfNeurons; i++) {
                System.arraycopy(this.weights[i],0, weightClone[i],0,numberOfInputs);
            }
            clone.setWeights(weightClone);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new CloneException("Error creating clone");
        }
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
                this.weights[i][j] += layer.getVector()[i][j];
            }
        }
    }

    public static Layer toLayer(@NotNull Iris flower) {
        Layer temp = new Layer(4,1);
        for (int i = 0; i < 4; i++) {
            temp.getVector()[i][0] = flower.getFeatures(i);
        }
        return temp;
    }

    public static Layer expectedTarget(@NotNull Iris flower, int outPut) {
        Layer temp = new Layer(outPut,1);
        for (int i = 0; i < outPut; i++) {
            temp.getVector()[i][0] = flower.getPattern()[i][0];
        }
        return temp;
    }

    public ArrayList<Double> toArray() {
        ArrayList<Double> temp = new ArrayList<>();
        for (int i = 0; i < numberOfNeurons; i++) {
            for (int j = 0; j < numberOfInputs; j++) {
                temp.add(weights[i][j]);
            }
        }
        return temp;
    }

    public static Layer substract(@NotNull Layer a, @NotNull Layer b) {
        Layer temp = new Layer(a.getNumberOfNeurons(), a.getNumberOfInputs());
        for (int i = 0; i < a.getNumberOfNeurons(); i++) {
            for (int j = 0; j < a.getNumberOfInputs(); j++) {
                temp.getVector()[i][j] = a.getVector()[i][j] - b.getVector()[i][j];
            }
        }
        return temp;
    }

    public static Layer calculateError(@NotNull Layer a, @NotNull Layer b) {
        Layer result = new Layer(a.getNumberOfNeurons(), a.getNumberOfInputs());
        for (int i = 0; i < a.getNumberOfNeurons(); i++) {
            for (int j = 0; j < a.getNumberOfInputs(); j++) {
                result.getVector()[i][j] = ((a.getVector()[i][j] - b.getVector()[i][j])*(a.getVector()[i][j] - b.getVector()[i][j]))/2.0;
            }
        }
        return result;
    }

    public static Layer transpose(@NotNull Layer a) {
        Layer temp = new Layer(a.getNumberOfInputs(), a.getNumberOfNeurons());
        for(int i = 0; i < a.getNumberOfNeurons(); i++) {
            for(int j = 0; j < a.getNumberOfInputs(); j++) {
                temp.getVector()[j][i] = a.getVector()[i][j];
            }
        }
        return temp;
    }

    public static Layer multiply(@NotNull Layer a, @NotNull Layer b) {
        Layer temp = new Layer(a.getNumberOfNeurons() ,b.getNumberOfInputs());
        for(int i = 0; i < temp.getNumberOfNeurons(); i++) {
            for(int j = 0; j < temp.getNumberOfInputs(); j++) {
                double sum = 0;
                for(int k = 0; k < a.getNumberOfInputs(); k++) {
                    sum += a.getVector()[i][k] * b.getVector()[k][j];
                }
                temp.getVector()[i][j] = sum;
            }
        }
        return temp;
    }

    public void multiply(@NotNull Layer a) {
        for(int i = 0; i < a.getNumberOfNeurons(); i++) {
            for(int j = 0; j < a.getNumberOfInputs(); j++) {
                this.weights[i][j] *= a.getVector()[i][j];
            }
        }
    }

    public void multiply(double a) {
        for(int i = 0; i < getNumberOfNeurons(); i++) {
            for(int j = 0; j < getNumberOfInputs(); j++) {
                this.weights[i][j] *= a;
            }
        }
    }

    public void sigmoid() {
        for(int i = 0; i < getNumberOfNeurons(); i++) {
            for(int j = 0; j < getNumberOfInputs(); j++)
                this.weights[i][j] = Sigmoid.sigmoid(this.weights[i][j]);
        }

    }

    public Layer dsigmoid() {
        Layer temp = new Layer(getNumberOfNeurons(), getNumberOfInputs());
        for(int i = 0; i < getNumberOfNeurons(); i++) {
            for(int j = 0;j < getNumberOfInputs(); j++)
                temp.weights[i][j] = Sigmoid.dsigmoid(this.weights[i][j]);
        }
        return temp;
    }
}