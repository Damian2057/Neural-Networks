package Neu.Network.model.components;

import Neu.Network.charts.ChartGenerator;
import Neu.Network.charts.Cord;
import Neu.Network.model.dao.DataReader;
import Neu.Network.model.dao.StatisticGenerator;
import Neu.Network.model.exceptions.model.LogicException;
import Neu.Network.model.flower.Iris;
import org.json.simple.parser.ParseException;
import java.io.IOException;
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
        try {
            jumpEpoch = DataReader.readEpochJump();
            saveFlag = DataReader.readFileSaveFlag();
        } catch (IOException | ParseException e) {
            throw new LogicException("Configuration error");
        }
    }

    @Override
    public void trainNetwork(ArrayList<Iris> trainingData) {
        if(stopConditionFlag) {
            trainByEpochs(trainingData);
        } else {
            trainByAccurany(trainingData);
        }
        if(saveFlag) {
            StatisticGenerator.saveError("ALL",-1, errorList);
        }

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
                errorList.add(new Cord(i,calculatedError));
            }
        }
        ChartGenerator chartGenerator = new ChartGenerator(String.valueOf(numberOfHiddenNeurons) ,errorList);
        chartGenerator.pack();
        chartGenerator.setVisible(true);
    }

    private void trainByAccurany(ArrayList<Iris> data) {
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
            } else {
                repeat = 0;
            }

            if(repeat == 3) {
                System.out.println("Successive iterations do not reduce the error.\n");
                break;
            }
            if(index % jumpEpoch == 0) {
                errorList.add(new Cord(index,calculatedError));
            }
            index++;
        }  while (accuracy < calculatedError);
        System.out.println("Number of iteration achieved: " + index);
        ChartGenerator chartGenerator = new ChartGenerator(String.valueOf(numberOfHiddenNeurons) ,errorList);
        chartGenerator.pack();
        chartGenerator.setVisible(true);
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
        Layer outputError = Layer.substract(target, output);

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
        Layer hidden_errors = Layer.multiply(who_T, outputError);

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

        calculateWholeNetworkError(outputError.getWeights(), hidden_errors.getWeights());
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
        StatisticGenerator.saveWeight("HiddenNeurons" + StatisticGenerator.getCurrentTime()
                , hiddenNeurons.getWeights());
        StatisticGenerator.saveWeight("outPutNeurons" + StatisticGenerator.getCurrentTime()
                , hiddenNeurons.getWeights());
        StatisticGenerator.saveWeight("hiddenBias" + StatisticGenerator.getCurrentTime()
                , hiddenBias.getWeights());
        StatisticGenerator.saveWeight("outPutBias" + StatisticGenerator.getCurrentTime()
                , outPutBias.getWeights());
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

    public void setMomentumFlag(boolean momentumFlag) {
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