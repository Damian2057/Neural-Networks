package Neu.Network.model.components;

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
    private final Layer outPutNeurons;
    private final Layer hiddenBias;
    private final Layer outPutBias;
    private boolean bias;

    public NeuralNetwork(int numberOfInPuts, int numberOfHiddenNeurons, int numberOfOutPuts ,double learningFactor) {
        this.learningFactor = learningFactor;
        hiddenNeurons = new Layer(numberOfHiddenNeurons, numberOfInPuts);
        outPutNeurons = new Layer(numberOfOutPuts, numberOfHiddenNeurons);
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

    public void train(Iris flower, double momentumFactor) {
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

        Layer target = Layer.expectedTarget(flower);

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
            if(i % 100 == 0) {
                //StatisticGenerator.saveEpochStats(GlobalConfiguration.epochsToCollect,5.569);
            }
            train(dataOrder.pollFirst(),momentumFactor);
        }
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
            if(iterator % 100 == 0) {
                //StatisticGenerator.saveEpochStats(GlobalConfiguration.epochsToCollect,5.569);
            }
            train(dataOrder.pollFirst(),momentumFactor);
            iterator++;
        }
    }

    private void calculateError() {

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
