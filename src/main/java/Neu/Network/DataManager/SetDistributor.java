package Neu.Network.DataManager;

import Neu.Network.model.dao.DataOperation;
import Neu.Network.model.flower.Iris;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class SetDistributor {
    private final ArrayList<Iris> allData;
    @SerializedName("testData")
    private ArrayList<Iris> testData;
    @SerializedName("validationData")
    private ArrayList<Iris> validationData;
    @SerializedName("trainingData")
    private ArrayList<Iris> trainingData;


    public SetDistributor(ArrayList<Integer> percentageSet) {
        this.allData = DataOperation.readData("data.csv");
        splitSet(percentageSet);
    }

    private void splitSet(ArrayList<Integer> percentageSet) {

    }
}
