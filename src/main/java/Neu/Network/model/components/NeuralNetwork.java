package Neu.Network.model.components;

import Neu.Network.charts.ChartGenerator;
import Neu.Network.charts.Cord;
import Neu.Network.model.dao.DataReader;
import Neu.Network.model.dao.StatisticsCollector;
import Neu.Network.model.flower.Iris;
import java.io.Serializable;
import java.util.*;

public class NeuralNetwork implements Serializable, Network {

    private final int numberOfHiddenNeurons;
    private final int numberOfOutPuts;
    private final double learningFactor;
    private final Layer hiddenNeurons;
    private final Layer prevHiddenNeurons;
    private final Layer outPutNeurons;
    private final Layer prevOutPutNeurons;
    private final Layer hiddenBias;
    private final Layer outPutBias;
    private Layer hiddenErrors;
    private Layer outputError;
    private double calculatedError = 0.0;
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

    public NeuralNetwork(int numberOfInPuts, int numberOfHiddenNeurons, int numberOfOutPuts ,double learningFactor) {
        this.numberOfHiddenNeurons = numberOfHiddenNeurons;
        this.numberOfOutPuts = numberOfOutPuts;
        this.learningFactor = learningFactor;
        hiddenNeurons = new Layer(numberOfHiddenNeurons, numberOfInPuts);
        prevHiddenNeurons = hiddenNeurons.clone();
        outPutNeurons = new Layer(numberOfOutPuts, numberOfHiddenNeurons);
        prevOutPutNeurons = outPutNeurons.clone();
        hiddenBias = new Layer(numberOfHiddenNeurons,1);
        outPutBias = new Layer(numberOfOutPuts,1);
        jumpEpoch = DataReader.getJump();
        saveFlag = DataReader.getFileSaveFlag();
    }

    @Override
    public void trainNetwork(ArrayList<Iris> trainingData) {
        if(stopConditionFlag) {
            trainByEpochs(trainingData);
        } else {
            trainByAccuracy(trainingData);
        }
        if(saveFlag) {
            StatisticsCollector.saveErrorFromWholeNetwork("ALL", errorList);
        }
        ChartGenerator chartGenerator = new ChartGenerator(String.valueOf(numberOfHiddenNeurons) ,errorList);
        chartGenerator.pack();
        chartGenerator.setVisible(true);
    }

    private void trainByEpochs(ArrayList<Iris> data) {
        for (int i = 0; i < epochs; i++) {
            if(typeOfSequence) {
                Collections.shuffle(data);
            }
            for (var sample : data) {
                train(sample);
            }
            if(i % jumpEpoch == 0) {
                printProgress(i);
                if(saveFlag) {
                    saveSingleNeurons(i);
                }
            }
        }
    }

    private void trainByAccuracy(ArrayList<Iris> data) {
        double prevError;
        int repeat = 0;
        int index = 0;
        do {
            if(typeOfSequence) {
                Collections.shuffle(data);
            }
            for (var sample : data) {
                train(sample);
            }
            prevError = calculatedError;
            if(prevError < calculatedError) {
                repeat++;
            }

            if(repeat > 1000 || index > 1000000) {
                System.out.println("Successive iterations do not reduce the error.\n");
                break;
            }

            if(index % jumpEpoch == 0) {
                printProgress(calculatedError);
                if(saveFlag) {
                    saveSingleNeurons(index);
                }
            }

            index++;
        }  while (accuracy < calculatedError);
        System.out.println("Number of iteration achieved: " + index);
    }

    private void train(Iris flower) {
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

        //calculate outputError
        //outputError = result(target Layer) - output
        outputError = Layer.substract(target, output);

        //calculate gradient
        //cradient = outputs * (1 - outputs);
        Layer gradient = output.dsigmoid();
        gradient.multiply(outputError);
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
        hiddenErrors = Layer.multiply(who_T, outputError);

        //calculate the hidden gradient
        Layer h_gradient = hidden.dsigmoid();
        h_gradient.multiply(hiddenErrors);
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

        calculateWholeNetworkError(outputError.getWeights(), hiddenErrors.getWeights());
    }

    @Override
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

    private void calculateWholeNetworkError(double[][] outErrors, double[][] hiddenError) {
        double avgFirst = 0.0;
        for (int i = 0; i < outErrors.length; i++) {
            for (int j = 0; j < outErrors[0].length; j++) {
                avgFirst += Math.abs(outErrors[i][j]) * Math.abs(outErrors[i][j]);
            }
        }
        avgFirst = Math.sqrt(avgFirst / ((numberOfHiddenNeurons + numberOfOutPuts) * numberOfOutPuts));

        double avgSecond = 0.0;
        for (int i = 0; i < hiddenError.length; i++) {
            for (int j = 0; j < hiddenError[0].length; j++) {
                avgSecond += Math.abs(hiddenError[i][j]) * Math.abs(hiddenError[i][j]);
            }
        }

        avgSecond = Math.sqrt(avgSecond / ((numberOfHiddenNeurons + numberOfOutPuts) * numberOfHiddenNeurons));

        this.calculatedError = (avgSecond + avgFirst);
    }

    public void saveWeights() {
        StatisticsCollector.saveWeight("HiddenNeurons", hiddenNeurons.getWeights());
        StatisticsCollector.saveWeight("outPutNeurons", hiddenNeurons.getWeights());
        StatisticsCollector.saveWeight("hiddenBias", hiddenBias.getWeights());
        StatisticsCollector.saveWeight("outPutBias", outPutBias.getWeights());
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
            System.out.println("Processing progress: " +  newProgress);
        }
    }

    private void printProgress(double sendError) {
        if(error != sendError) {
            error = sendError;
            System.out.println("Processing progress: " +  error);
        }
    }

    private void saveSingleNeurons(int i) {
        errorList.add(new Cord(i, calculatedError));
        for (int j = 0; j < hiddenErrors.getWeights().length; j++) {
            for (int k = 0; k < hiddenErrors.getWeights()[0].length; k++) {
                StatisticsCollector.saveErrorOnSingleNeuron("hidden", j, i, hiddenErrors.getWeights()[j][k]);
            }
        }
        for (int j = 0; j < outputError.getWeights().length; j++) {
            for (int k = 0; k < outputError.getWeights()[0].length; k++) {
                StatisticsCollector.saveErrorOnSingleNeuron("output", j, i, outputError.getWeights()[j][k]);
            }
        }
    }

    public void showInformation() {
        if(epochs > 0 ) {
            System.out.println("==================================================="
                    + "\nNumber of hidden Neurons: " + numberOfHiddenNeurons
                    + "\nLearning factor: " + getLearningFactor()
                    + "\nBias status: " + bias
                    + "\nMomentum factor: " + getMomentumFactor()
                    + "\nTaught on: " + getEpochs() + " epochs\n"
                    + "===================================================");
        } else {
            System.out.println("==================================================="
                    + "\nNumber of hidden Neurons: " + numberOfHiddenNeurons
                    + "\nLearning factor: " + getLearningFactor()
                    + "\nBias status: " + bias
                    + "\nMomentum factor: " + getMomentumFactor()
                    +"\nTaught with accuracy: "+ getAccuracy()
                    + "\n===================================================" );
        }
    }
}