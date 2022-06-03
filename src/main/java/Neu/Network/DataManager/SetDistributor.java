package Neu.Network.DataManager;

import Neu.Network.model.dao.DataOperation;
import Neu.Network.model.exceptions.argument.ArgumentException;
import Neu.Network.model.flower.Iris;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class SetDistributor {
    private final ArrayList<Iris> allData;
    @SerializedName("testData")
    private ArrayList<Iris> testData = new ArrayList<>();
    @SerializedName("validationData")
    private ArrayList<Iris> validationData = new ArrayList<>();
    @SerializedName("trainingData")
    private ArrayList<Iris> trainingData = new ArrayList<>();


    public SetDistributor(int[] table) {
        checkSet(table);
        this.allData = DataOperation.readData("data.csv");
        initDataSets(table);
    }

    private void initDataSets(int[] table) {
        ArrayList<Iris> dest = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            dest.add(allData.get(i));
        }
        DataSet firstSpec = new DataSet(dest);
        dest.clear();

        for (int i = 50; i < 100; i++) {
            dest.add(allData.get(i));
        }
        DataSet secondSpec = new DataSet(dest);
        dest.clear();

        for (int i = 100; i < 150; i++) {
            dest.add(allData.get(i));
        }
        DataSet thirdSpec = new DataSet(dest);
        dest.clear();

        for (int i = 0; i < (allData.size()/3.0)*(table[0]*0.01); i++) {
            trainingData.add(firstSpec.getRandomIris());
            trainingData.add(secondSpec.getRandomIris());
            trainingData.add(thirdSpec.getRandomIris());
        }

        for (int i = 0; i < (allData.size()/3.0)*(table[1]*0.01); i++) {
            validationData.add(firstSpec.getRandomIris());
            validationData.add(secondSpec.getRandomIris());
            validationData.add(thirdSpec.getRandomIris());
        }

        for (int i = 0; i < (allData.size()/3.0)*(table[2]*0.01); i++) {
            testData.add(firstSpec.getRandomIris());
            testData.add(secondSpec.getRandomIris());
            testData.add(thirdSpec.getRandomIris());
        }
        DataOperation.writeData("trainingData",trainingData);
        DataOperation.writeData("validationData",validationData);
        DataOperation.writeData("testData",testData);
    }

    public void setInformation() {
        System.out.println("Collected " + trainingData.size() + " portions of data to train.");
        System.out.println("Collected " + validationData.size() + " portions of validation Data.");
        System.out.println("Collected " + testData.size() + " portions of test Data.");
    }
    

    public ArrayList<Iris> getTestData() {
        return testData;
    }

    public ArrayList<Iris> getValidationData() {
        return validationData;
    }

    public ArrayList<Iris> getTrainingData() {
        return trainingData;
    }

    private void checkSet(int[] table) {
        if(table[0] + table[1] + table[2] > 100) {
            throw new ArgumentException("percentage greater than 100%");
        }
    }
}
