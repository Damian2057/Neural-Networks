package Neu.Network.model.components;

import java.util.ArrayList;

public class LayerContainer {
    private ArrayList<Layer> hiddenList = new ArrayList<>();
    private ArrayList<Layer> biasList = new ArrayList<>();
    private final int numberOfHiddenNeurons;
    private final int numberOfHiddenLayers;
    private final int numberOfInPuts;
    private boolean bias;

    public LayerContainer(int numberOfHiddenNeurons, int numberOfHiddenLayers, int numberOfInPuts) {
        this.numberOfHiddenNeurons = numberOfHiddenNeurons;
        this.numberOfHiddenLayers = numberOfHiddenLayers;
        this.numberOfInPuts = numberOfInPuts;
        for (int i = 0; i < this.numberOfHiddenLayers; i++) {
            hiddenList.add(new Layer(this.numberOfHiddenNeurons, this.numberOfInPuts));
            biasList.add(new Layer(this.numberOfHiddenNeurons,1));
        }
    }

    public Layer calculate(Layer inPut) {
        Layer hiddenOutPut = Layer.multiply(hiddenList.get(0), inPut);
        if(bias) {
            hiddenOutPut.add(biasList.get(0));
        }
        hiddenOutPut.sigmoid();
        for (int i = 1; i < numberOfHiddenLayers; i++) {
            hiddenOutPut = Layer.multiply(hiddenList.get(0), hiddenOutPut);
            if(bias) {
                hiddenOutPut.add(biasList.get(i));
            }
            hiddenOutPut.sigmoid();
        }
        return hiddenOutPut;
    }

    public double getWeightFromNeuron(int LayerNumber, int neuronNumber, int weightNumber) {
        return hiddenList.get(LayerNumber).getWeights()[neuronNumber][weightNumber];
    }

    public boolean isBias() {
        return bias;
    }

    public void setBias(boolean bias) {
        this.bias = bias;
    }
}
