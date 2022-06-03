package Neu.Network.model.components;

import Neu.Network.charts.ChartGenerator;
import Neu.Network.charts.Cord;
import Neu.Network.model.dao.JsonReader;
import Neu.Network.model.dao.StatisticsCollector;
import Neu.Network.model.flower.Iris;
import Neu.Network.summary.SummaryCalculator;
import java.io.Serializable;
import java.util.*;

public class NeuralNetwork implements Serializable, Network {

    private final int numberOfHiddenNeurons;
    private final int numberOfOutPuts;
    private final double learningFactor;
    private final Layer hiddenNeurons;
    private Layer prevHiddenNeurons;
    private final Layer outPutNeurons;
    private Layer prevOutPutNeurons;
    private final Layer hiddenBias;
    private final Layer outPutBias;
    private Layer hiddenErrors;
    private Layer outputError;
    private double sumFromAllData = 0.0;
    private boolean bias;
    private boolean stopConditionFlag;
    private int epochs = 0;
    private double accuracy = 0.0;
    private double momentumFactor = 0.0;
    private boolean typeOfSequence;
    private final ArrayList<Cord> errorList = new ArrayList<>();
    private final int jumpEpoch;
    private final boolean saveFlag;
    private long progress;
    private double error;
    private final int showDisplay = JsonReader.getJumpOnDisplay();
    private final boolean validationFlag = JsonReader.getValidationSetFlag();
    private double dataSize;
    private Layer bestHiddenNeurons;
    private Layer bestOutNeurons;
    private Layer bestHiddenBias;
    private Layer bestOutBias;

    public NeuralNetwork(int numberOfInPuts, int numberOfHiddenNeurons, int numberOfOutPuts, double learningFactor) {
        this.numberOfHiddenNeurons = numberOfHiddenNeurons;
        this.numberOfOutPuts = numberOfOutPuts;
        this.learningFactor = learningFactor;
        hiddenNeurons = new Layer(numberOfHiddenNeurons, numberOfInPuts);
        prevHiddenNeurons = hiddenNeurons.clone();
        outPutNeurons = new Layer(numberOfOutPuts, numberOfHiddenNeurons);
        prevOutPutNeurons = outPutNeurons.clone();
        hiddenBias = new Layer(numberOfHiddenNeurons,1);
        outPutBias = new Layer(numberOfOutPuts,1);
        jumpEpoch = JsonReader.getJump();
        saveFlag = JsonReader.getFileSaveFlag();

        bestHiddenNeurons = hiddenNeurons.clone();
        bestOutNeurons = outPutNeurons.clone();
        bestHiddenBias = hiddenBias.clone();
        bestOutBias = outPutBias.clone();
    }

    @Override
    public void trainNetwork(ArrayList<Iris> trainingData, ArrayList<Iris> validationData) {
        dataSize = trainingData.size();
        if(stopConditionFlag) {
            trainByEpochs(trainingData, validationData);
        } else {
            trainByAccuracy(trainingData, validationData);
        }
        if(saveFlag) {
            StatisticsCollector.saveErrorFromWholeNetwork("ALL", errorList);
            ChartGenerator chartGenerator = new ChartGenerator(String.valueOf(numberOfHiddenNeurons) ,errorList);
            chartGenerator.pack();
            chartGenerator.setVisible(true);
        }
    }

    private void trainByEpochs(ArrayList<Iris> data, ArrayList<Iris> validationData) {
        for (int i = 0; i < epochs; i++) {
            if(typeOfSequence) {
                Collections.shuffle(data);
            }
            for (var sample : data) {
                train(sample, i);
            }

            if(i % jumpEpoch == 0) {
                printProgress(i);
                if(saveFlag) {
                    saveStatsOnNeuron(i);
                }
            }

            if(validationFlag) {
                validateWeight(validationData, i);
            }

            sumFromAllData = 0.0;
        }
    }

    private void trainByAccuracy(ArrayList<Iris> data, ArrayList<Iris> validationData) {
        double prevError;
        int repeat = 0;
        int index = 0;
        do {
            if(typeOfSequence) {
                Collections.shuffle(data);
            }
            for (var sample : data) {
                train(sample, index);
            }
            prevError = sumFromAllData;
            if(prevError < sumFromAllData) {
                repeat++;
            }

            if(repeat > 1000 || index > 1000000) {
                System.out.println("Successive iterations do not reduce the error.\n");
                break;
            }

            if(index % jumpEpoch == 0) {
                printProgress(sumFromAllData);
                if(saveFlag) {
                    saveStatsOnNeuron(index);
                }
            }

            if(validationFlag) {
                validateWeight(validationData, index);
            }

            sumFromAllData = 0.0;

            index++;
        }  while (accuracy < prevError);
        System.out.println("Number of iteration achieved: " + index);
    }

    private void train(Iris flower, int iterator) {

        prevOutPutNeurons = outPutNeurons.clone();
        prevHiddenNeurons = hiddenNeurons.clone();

        Layer inPut = Layer.toLayer(flower);
        Layer hidden = Layer.multiply(hiddenNeurons, inPut); //HiddenNet
        if(bias) {
            hidden.add(hiddenBias);
        }
        hidden.sigmoid(); //HiddenOut

        Layer output = Layer.multiply(outPutNeurons,hidden); //OutNet
        if(bias) {
            output.add(outPutBias);
        }
        output.sigmoid(); //OutOut

        Layer target = Layer.expectedTarget(flower,numberOfOutPuts);

        saveTargetOutPutVectors(iterator,target,output);

        outputError = Layer.calcError(target, output); //E = 0.5*(t-o)^2
        calculateTotalError(outputError.getVector());

        Layer gradient = output.dsigmoid(); //output * (1-output)

        Layer targetDivOut = Layer.substract(target,output);   //
        targetDivOut.multiply(-1);                          //-(target-out)
        gradient.multiply(targetDivOut);

        Layer eTotalNetOut = gradient.clone();

        Layer hidden_T = Layer.transpose(hidden);
        Layer who_delta =  Layer.multiply(gradient, hidden_T);
        who_delta.multiply(learningFactor);
        who_delta.multiply(-1);

        outPutNeurons.add(who_delta);
        if(momentumFactor != 0) {
            Layer prev = Layer.substract(outPutNeurons,prevOutPutNeurons);
            prev.multiply(momentumFactor);
            outPutNeurons.add(prev);
        }
        if(bias) {
            gradient.multiply(-1);
            outPutBias.add(gradient);
        }

        //calculate the hidden layer errors
        Layer who_T = Layer.transpose(prevOutPutNeurons);
        hiddenErrors = Layer.multiply(who_T, eTotalNetOut);

        //calculate the hidden gradient
        Layer h_gradient = hidden.dsigmoid();
        h_gradient.multiply(hiddenErrors);

        //calcuate input => hidden deltas
        Layer i_T = Layer.transpose(inPut);
        Layer wih_delta = Layer.multiply(h_gradient, i_T);
        wih_delta.multiply(learningFactor);
        wih_delta.multiply(-1);

        //adjust the weights by deltas
        hiddenNeurons.add(wih_delta);
        if(momentumFactor != 0) {
            Layer prev = Layer.substract(hiddenNeurons,prevHiddenNeurons);
            prev.multiply(momentumFactor);
            hiddenNeurons.add(prev);
        }

        if(bias) {
            h_gradient.multiply(-1);
            hiddenBias.add(h_gradient);
        }
    }

    @Override
    public ArrayList<Double> calculate(Iris flower) {
        if(validationFlag) {
            return calculateByBestWeight(flower);
        } else {
            return calculateByNewWeight(flower);
        }
    }

    private ArrayList<Double> calculateByBestWeight(Iris flower) {
        Layer inPut = Layer.toLayer(flower);
        Layer hiddenOutPut = Layer.multiply(bestHiddenNeurons,inPut);
        if(bias) {
            hiddenOutPut.add(bestHiddenBias);
        }
        hiddenOutPut.sigmoid();

        Layer outPut = Layer.multiply(bestOutNeurons,hiddenOutPut);
        if(bias) {
            outPut.add(bestOutBias);
        }
        outPut.sigmoid();
        return outPut.toArray();
    }

    private ArrayList<Double> calculateByNewWeight(Iris flower) {
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

    private void calculateTotalError(double[][] outErrors) {
        double sum = 0.0;
        for (int i = 0; i < outErrors.length; i++) {
            for (int j = 0; j < outErrors[0].length; j++) {
                sum += outErrors[i][j];
            }
        }

        this.sumFromAllData += sum;
    }

    private void saveStatsOnNeuron(int i) {
        errorList.add(new Cord(i, sumFromAllData /dataSize));
        sumFromAllData = 0.0;
        for (int j = 0; j < hiddenErrors.getVector().length; j++) {
            for (int k = 0; k < hiddenErrors.getVector()[0].length; k++) {
                StatisticsCollector.saveErrorOnSingleNeuron("hidden", j, i, hiddenErrors.getVector()[j][k]/dataSize);
            }
        }
        for (int j = 0; j < outputError.getVector().length; j++) {
            for (int k = 0; k < outputError.getVector()[0].length; k++) {
                StatisticsCollector.saveErrorOnSingleNeuron("output", j, i, outputError.getVector()[j][k]/dataSize);
            }
        }
    }

    public void saveWeights() {
        StatisticsCollector.saveWeight("HiddenNeurons", bestHiddenNeurons.getVector());
        StatisticsCollector.saveWeight("outPutNeurons", bestOutNeurons.getVector());
        StatisticsCollector.saveWeight("hiddenBias", bestHiddenBias.getVector());
        StatisticsCollector.saveWeight("outPutBias", bestOutBias.getVector());
    }

    public int getEpochs() {
        return epochs;
    }

    public double getAccuracy() {
        return accuracy;
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

    public void setMomentumFactor(double momentumFactor) {
        this.momentumFactor = momentumFactor;
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

    public void setBias(boolean bias) {
        this.bias = bias;
    }

    private void printProgress(int i) {
        long newProgress = Math.round(( (double) i/epochs* 100.0));
        if(progress != newProgress) {
            progress = newProgress;
            System.out.println("Processing progress: " +  newProgress + " %");
        }
    }

    private void printProgress(double sendError) {
        if(error != sendError) {
            error = sendError;
            System.out.println("Processing progress: " +  error);
        }
    }

    private void saveTargetOutPutVectors(int index, Layer target, Layer outOutPut) {
        if(index % showDisplay == 0) {
            StatisticsCollector.targetOutputSample(index, target, outOutPut);
        }
    }

    private void validateWeight(ArrayList<Iris> validationData, int epoch) {
        SummaryCalculator first = new SummaryCalculator();
        SummaryCalculator second = new SummaryCalculator();
        for (var sample : validationData) {
            first.summarize(calculateByNewWeight(sample), sample);
            second.summarize(calculateByBestWeight(sample), sample);
        }

        if(first.getPositives() > second.getPositives()) {
            System.out.println("Epoch: " + epoch + " Best Weights update");
            bestHiddenNeurons = hiddenNeurons.clone();
            bestOutNeurons = outPutNeurons.clone();
            bestHiddenBias = hiddenBias.clone();
            bestOutBias = outPutBias.clone();
        }
    }

    public void showInformation() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("===================================================")
                .append("\nNumber of hidden Neurons: " + numberOfHiddenNeurons)
                .append("\nLearning factor: " + getLearningFactor())
                .append("\nBias status: " + bias)
                .append("\nMomentum factor: " + getMomentumFactor());

        if(epochs > 0 ) {
            stringBuilder.append("\nTaught on: " + getEpochs() + " epochs");
        } else {
            stringBuilder.append("\nTaught with accuracy: "+ getAccuracy());
        }
        stringBuilder.append("\n===================================================");
        System.out.println(stringBuilder);
    }
}