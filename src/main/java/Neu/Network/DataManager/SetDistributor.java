package Neu.Network.DataManager;

import Neu.Network.model.dao.DataOperation;
import Neu.Network.model.flower.Iris;
import java.util.ArrayList;

public class SetDistributor {
    private ArrayList<Iris> allData;
    private ArrayList<Iris> testData;
    private ArrayList<Iris> validationData;
    private ArrayList<Iris> trainingData;


    public SetDistributor(ArrayList<Integer> percentageSet) {
        this.allData = DataOperation.readData("data.csv");
        splitSet(percentageSet);
    }


    private void splitSet(ArrayList<Integer> percentageSet) {

    }
}
