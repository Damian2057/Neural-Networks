package Neu.Network.model.components;

import Neu.Network.model.exceptions.model.ShapeException;
import Neu.Network.model.flower.Iris;
import Neu.Network.model.math.Sigmoid;
import org.jetbrains.annotations.NotNull;
import java.io.Serializable;
import java.util.ArrayList;

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

    public static Layer toLayer(@NotNull Iris flower)
    {
        Layer temp = new Layer(4,1);
        for (int i = 0; i < 4; i++) {
            temp.getWeights()[i][0] = flower.getFeatures(i);
        }
        return temp;
    }

    public static Layer expectedTarget(@NotNull Iris flower) {
        Layer temp = new Layer(4,1);
        if(flower.getType() == 0) {
            temp.getWeights()[0][1] = 1;
            temp.getWeights()[1][1] = 0;
            temp.getWeights()[2][1] = 0;
            temp.getWeights()[3][1] = 0;
        } else if(flower.getType() == 1) {
            temp.getWeights()[0][1] = 0;
            temp.getWeights()[1][1] = 1;
            temp.getWeights()[2][1] = 0;
            temp.getWeights()[3][1] = 0;
        } else {
            temp.getWeights()[0][1] = 0;
            temp.getWeights()[1][1] = 0;
            temp.getWeights()[2][1] = 1;
            temp.getWeights()[3][1] = 0;
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
                temp.getWeights()[i][j] = a.getWeights()[i][j] - b.getWeights()[i][j];
            }
        }
        return temp;
    }

    public static Layer transpose(@NotNull Layer a) {
        Layer temp = new Layer(a.getNumberOfInputs(), a.getNumberOfNeurons());
        for(int i = 0; i < a.getNumberOfNeurons(); i++)
        {
            for(int j = 0; j < a.getNumberOfInputs(); j++)
            {
                temp.getWeights()[j][i] = a.getWeights()[i][j];
            }
        }
        return temp;
    }

    public static Layer multiply(@NotNull Layer a, @NotNull Layer b) {
        Layer temp = new Layer(a.getNumberOfNeurons() ,b.getNumberOfInputs());
        for(int i = 0; i < temp.getNumberOfNeurons() ;i++)
        {
            for(int j = 0; j < temp.getNumberOfInputs(); j++)
            {
                double sum = 0;
                for(int k = 0; k < a.getNumberOfInputs() ;k++)
                {
                    sum += a.getWeights()[i][k] * b.getWeights()[k][j];
                }
                temp.getWeights()[i][j] = sum;
            }
        }
        return temp;
    }

    public void multiply(@NotNull Layer a) {
        for(int i = 0; i < a.getNumberOfNeurons(); i++)
        {
            for(int j = 0; j < a.getNumberOfInputs(); j++)
            {
                this.weights[i][j] *= a.getWeights()[i][j];
            }
        }
    }

    public void multiply(double a) {
        for(int i = 0; i < getNumberOfNeurons(); i++)
        {
            for(int j = 0; j < getNumberOfInputs(); j++)
            {
                this.weights[i][j] *= a;
            }
        }
    }

    public void sigmoid() {
        for(int i = 0; i < getNumberOfNeurons(); i++)
        {
            for(int j = 0; j < getNumberOfInputs(); j++)
                this.weights[i][j] = Sigmoid.sigmoid(this.weights[i][j]);
        }

    }

    public Layer dsigmoid() {
        Layer temp = new Layer(getNumberOfNeurons(), getNumberOfInputs());
        for(int i = 0; i < getNumberOfNeurons(); i++)
        {
            for(int j = 0;j < getNumberOfInputs(); j++)
                temp.weights[i][j] = Sigmoid.dsigmoid(this.weights[i][j]);
        }
        return temp;
    }
}
