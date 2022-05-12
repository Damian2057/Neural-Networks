package Neu.Network.model.components;

import Neu.Network.global.GlobalVariables;
import Neu.Network.model.dao.StatisticGenerator;
import Neu.Network.model.flower.Iris;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NeuralNetwork implements Serializable {
    private final double learningFactor;
    private double momentumFactor = 0;
    private int epochs = 0;
    private double accuracy = 0.0;
    private final Layer hiddenNeurons;
    private final Layer prevHiddenNeurons;
    private final Layer outPutNeurons;
    private final Layer prevOutPutNeurons;
    private final Layer hiddenBias;
    private final Layer outPutBias;

    private boolean bias;

    public NeuralNetwork(int numberOfInPuts, int numberOfHiddenNeurons, int numberOfOutPuts ,double learningFactor) {
        this.learningFactor = learningFactor;
        hiddenNeurons = new Layer(numberOfHiddenNeurons, numberOfInPuts);
        prevHiddenNeurons = hiddenNeurons.clone();
        outPutNeurons = new Layer(numberOfOutPuts, numberOfHiddenNeurons);
        prevOutPutNeurons = outPutNeurons.clone();
        hiddenBias = new Layer(numberOfHiddenNeurons,1);
        outPutBias = new Layer(numberOfOutPuts,1);
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

    public void train(Iris flower, double momentumFactor, int epochsNumber) {
        Layer inPut = Layer.toLayer(flower);
        Layer hidden = Layer.multiply(hiddenNeurons, inPut);
        hidden.add(hiddenBias);
        hidden.sigmoid();

        Layer output = Layer.multiply(outPutNeurons,hidden);
        output.add(outPutBias);
        output.sigmoid();

        Layer target = Layer.expectedTarget(flower);

        Layer error = Layer.substract(target, output);
        Layer gradient = output.dsigmoid();
        gradient.multiply(error);
        gradient.multiply(learningFactor);

        Layer hidden_T = Layer.transpose(hidden);
        Layer who_delta =  Layer.multiply(gradient, hidden_T);

        outPutNeurons.add(who_delta);
        if(momentumFactor != 0) {
            Layer prev = Layer.substract(outPutNeurons,prevOutPutNeurons);
            prev.multiply(momentumFactor);
            outPutNeurons.add(prev);
        }
        outPutBias.add(gradient);

        Layer who_T = Layer.transpose(outPutNeurons);
        Layer hidden_errors = Layer.multiply(who_T, error);

        Layer h_gradient = hidden.dsigmoid();
        h_gradient.multiply(hidden_errors);
        h_gradient.multiply(learningFactor);

        Layer i_T = Layer.transpose(inPut);
        Layer wih_delta = Layer.multiply(h_gradient, i_T);

        hiddenNeurons.add(wih_delta);
        if(momentumFactor != 0) {
            Layer prev = Layer.substract(hiddenNeurons,prevHiddenNeurons);
            prev.multiply(momentumFactor);
            hiddenNeurons.add(prev);
        }
        hiddenBias.add(h_gradient);

        if(epochsNumber % GlobalVariables.epochsToCollect == 0) {
            StatisticGenerator.saveEpochErrorStats("outputError",epochsNumber,calculateError(error.getWeights()));
            StatisticGenerator.saveEpochErrorStats("hiddenError",epochsNumber,calculateError(hidden_errors.getWeights()));
        }
    }

    public LinkedList<Iris> getSequencesData(ArrayList<Iris> data, boolean flag) {
        //true = Random/ false = sequentially
        LinkedList<Iris> temp;
        if(flag) {
            temp = new LinkedList<>();
            List<Integer> randList = IntStream.rangeClosed(0, data.size()-1).boxed().collect(Collectors.toList());
            for (int i = 0; i < data.size(); i++) {
                Random randomizer = new Random();
                int j = randList.get(randomizer.nextInt(randList.size()));
                randList.remove(Integer.valueOf(j));
                temp.add(data.get(j));
            }
        } else {
            temp = new LinkedList<>(data);
        }
        return temp;
    }

    public void trainByEpochs(ArrayList<Iris> data, int epochs, double momentumFactor, boolean method) {
        this.epochs = epochs;
        this.momentumFactor = momentumFactor;
        LinkedList<Iris> dataOrder = new LinkedList<>();
        for (int i = 0; i < epochs; i++) {
            if(dataOrder.isEmpty()) {
                dataOrder = getSequencesData(data,method);
            }
            train(dataOrder.pollFirst(),momentumFactor, i);
        }
        saveWeights();
    }

    public void trainByAccurany(ArrayList<Iris> data, double accuracy, double momentumFactor, boolean method) {
        this.accuracy = accuracy;
        this.momentumFactor = momentumFactor;
        LinkedList<Iris> dataOrder = new LinkedList<>();
        int test = 50;
        int iterator = 1;
        while (true) { //TODO: accuracy condition
            if(dataOrder.isEmpty()) {
                dataOrder = getSequencesData(data,method);
            }
            if(iterator % GlobalVariables.epochsToCollect == 0) {
                // StatisticGenerator.saveEpochErrorStats(i,calculateError());
            }
            train(dataOrder.pollFirst(),momentumFactor,iterator);
            iterator++;
        }
    }

    private void saveWeights() {
        StatisticGenerator.saveWeight("HiddenNeurons", hiddenNeurons.getWeights());
        StatisticGenerator.saveWeight("outPutNeurons", hiddenNeurons.getWeights());
        StatisticGenerator.saveWeight("hiddenBias", hiddenBias.getWeights());
        StatisticGenerator.saveWeight("outPutBias", outPutBias.getWeights());
    }

    private double calculateError(double[][] errors) {
        double avg = 0.0;
        for (int i = 0; i < errors.length; i++) {
            for (int j = 0; j < errors[0].length; j++) {
                avg += errors[i][j];
            }
        }
        return avg;
    }

    public int getEpochs() {
        return epochs;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void showInformation() {
        if(epochs > 0 ) {
            System.out.println("\nlearning factor: " + getLearningFactor()
                    + "\nmomentum factor: " + getMomentumFactor()
                    +"\nTaught on: " + getEpochs() + " epochs");
        } else {
            System.out.println("\nlearning factor: " + getLearningFactor()
                    + "\nmomentum factor: " + getMomentumFactor()
                    +"\nTaught with accuracy: "+ getAccuracy());
        }
    }
}
