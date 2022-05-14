package Neu.Network.model.components;

import Neu.Network.model.dao.StatisticGenerator;
import Neu.Network.model.flower.Iris;
import java.io.Serializable;
import java.util.*;

public class NeuralNetwork implements Serializable {
    private final int numberOfHiddenNeurons;
    private final double learningFactor;
    private final Layer hiddenNeurons;
    private final Layer prevHiddenNeurons;
    private final Layer outPutNeurons;
    private final Layer prevOutPutNeurons;
    private final Layer hiddenBias;
    private final Layer outPutBias;

    private boolean bias;
    private boolean stopConditionFlag;
    private int epochs = 0;
    private double accuracy = 0.0;
    private boolean momentumFlag;
    private double momentumFactor = 0.0;
    private boolean typeOfSequence;


    public NeuralNetwork(int numberOfInPuts, int numberOfHiddenNeurons, int numberOfOutPuts ,double learningFactor) {
        this.numberOfHiddenNeurons = numberOfHiddenNeurons;
        this.learningFactor = learningFactor;
        hiddenNeurons = new Layer(numberOfHiddenNeurons, numberOfInPuts);
        prevHiddenNeurons = hiddenNeurons.clone();
        outPutNeurons = new Layer(numberOfOutPuts, numberOfHiddenNeurons);
        prevOutPutNeurons = outPutNeurons.clone();
        hiddenBias = new Layer(numberOfHiddenNeurons,1);
        outPutBias = new Layer(numberOfOutPuts,1);
    }

    public void trainNetwork(ArrayList<Iris> trainingData) {
        if(stopConditionFlag) {
            trainByEpochs(trainingData);
        } else {
            trainByAccurany(trainingData);
        }
    }

    public void trainByEpochs(ArrayList<Iris> data) {
        for (int i = 0; i < epochs; i++) {
            if(typeOfSequence) {
                Collections.shuffle(data);
            }
            for (var sample : data) {
                train(sample);
            }
        }
        // saveWeights();
    }

    public void trainByAccurany(ArrayList<Iris> data) {
    /*    this.accuracy = accuracy;
        this.momentumFactor = momentumFactor;
        LinkedList<Iris> dataOrder = new LinkedList<>();
        int test = 50;
        int iterator = 1;
        while (true) { //TODO: accuracy condition
            if(dataOrder.isEmpty()) {
                dataOrder = getSequencesData(data,method);
            }
//            if(iterator % GlobalVariables.epochsToCollect == 0) {
//                // StatisticGenerator.saveEpochErrorStats(i,calculateError());
//            }
            train(dataOrder.pollFirst(),momentumFactor);
            iterator++;
        }*/
    }

    public ArrayList<Double> calculate(Iris flower) {
        Layer inPut = Layer.toLayer(flower);
        Layer hiddenOutPut = Layer.multiply(hiddenNeurons,inPut);
        if(bias) {
            hiddenOutPut.add(hiddenBias);
        }
        hiddenOutPut.sigmoid();

        Layer outPut = Layer.multiply(outPutNeurons,hiddenOutPut);
        if(bias) {
            outPut.add(outPutBias);
        }
        outPut.sigmoid();
        return outPut.toArray();
    }

    public void train(Iris flower) {
        Layer inPut = Layer.toLayer(flower);
        //generating the Hidden Layer Output
        Layer hidden = Layer.multiply(hiddenNeurons, inPut);
        if(bias) {
            hidden.add(hiddenBias);
        }
        //activation function
        hidden.sigmoid();

        //generating the output
        Layer output = Layer.multiply(outPutNeurons,hidden);
        if(bias) {
            output.add(outPutBias);
        }
        output.sigmoid();

        //get Layer with expected pattern
        Layer target = Layer.expectedTarget(flower);

        //calculate error
        //error = result(target Layer) - output
        Layer error = Layer.substract(target, output);

        //calculate gradient
        //cradient = outputs * (1 - outputs);
        Layer gradient = output.dsigmoid();
        gradient.multiply(error);
        gradient.multiply(learningFactor);

        //calculate deltas
        Layer hidden_T = Layer.transpose(hidden);
        Layer who_delta =  Layer.multiply(gradient, hidden_T);

        //adjust the weights by deltas
        outPutNeurons.add(who_delta);
        if(momentumFactor != 0) {
            Layer prev = Layer.substract(outPutNeurons,prevOutPutNeurons);
            prev.multiply(momentumFactor);
            outPutNeurons.add(prev);
        }
        if(bias) {
            outPutBias.add(gradient);
        }

        //calculate the hidden layer errors
        Layer who_T = Layer.transpose(outPutNeurons);
        Layer hidden_errors = Layer.multiply(who_T, error);

        //calculate the hidden gradient
        Layer h_gradient = hidden.dsigmoid();
        h_gradient.multiply(hidden_errors);
        h_gradient.multiply(learningFactor);

        //calcuate input => hidden deltas
        Layer i_T = Layer.transpose(inPut);
        Layer wih_delta = Layer.multiply(h_gradient, i_T);

        //adjust the weights by deltas
        hiddenNeurons.add(wih_delta);
        if(momentumFactor != 0) {
            Layer prev = Layer.substract(hiddenNeurons,prevHiddenNeurons);
            prev.multiply(momentumFactor);
            hiddenNeurons.add(prev);
        }

        if(bias) {
            hiddenBias.add(h_gradient);
        }

//        if(epochsNumber % GlobalVariables.epochsToCollect == 0) {
//            StatisticGenerator.saveEpochErrorStats("outputError",epochsNumber,calculateError(error.getWeights()));
//            StatisticGenerator.saveEpochErrorStats("hiddenError",epochsNumber,calculateError(hidden_errors.getWeights()));
//        }
    }

    private double calculateError(double[][] errors) {
        double avg = 0.0;
        for (int i = 0; i < errors.length; i++) {
            for (int j = 0; j < errors[0].length; j++) {
                avg += Math.abs(errors[i][j]);
            }
        }
        return avg;
    }

    private void saveWeights() {
        StatisticGenerator.saveWeight("HiddenNeurons", hiddenNeurons.getWeights());
        StatisticGenerator.saveWeight("outPutNeurons", hiddenNeurons.getWeights());
        StatisticGenerator.saveWeight("hiddenBias", hiddenBias.getWeights());
        StatisticGenerator.saveWeight("outPutBias", outPutBias.getWeights());
    }

    public int getEpochs() {
        return epochs;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public boolean isStopConditionFlag() {
        return stopConditionFlag;
    }

    public void setStopConditionFlag(boolean stopConditionFlag) {
        this.stopConditionFlag = stopConditionFlag;
    }

    public void setEpochs(int epochs) {
        this.epochs = epochs;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public boolean isMomentumFlag() {
        return momentumFlag;
    }

    public void setMomentumFlag(boolean momentumFlag) {
        this.momentumFlag = momentumFlag;
    }

    public void setMomentumFactor(double momentumFactor) {
        this.momentumFactor = momentumFactor;
    }

    public boolean isTypeOfSequence() {
        return typeOfSequence;
    }

    public void setTypeOfSequence(boolean typeOfSequence) {
        this.typeOfSequence = typeOfSequence;
    }

    public double getLearningFactor() {
        return learningFactor;
    }

    public double getMomentumFactor() {
        return momentumFactor;
    }

    public boolean isBias() {
        return bias;
    }

    public void setBias(boolean bias) {
        this.bias = bias;
    }

    public void showInformation() {
        if(epochs > 0 ) {
            System.out.println("Number of hidden Neurons: " + numberOfHiddenNeurons
                    +"\nlearning factor: " + getLearningFactor()
                    + "\nmomentum factor: " + getMomentumFactor()
                    +"\nTaught on: " + getEpochs() + " epochs");
        } else {
            System.out.println("\nlearning factor: " + getLearningFactor()
                    + "\nmomentum factor: " + getMomentumFactor()
                    +"\nTaught with accuracy: "+ getAccuracy());
        }
    }
}