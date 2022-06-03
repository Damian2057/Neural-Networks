package Neu.Network.DataManager;

import Neu.Network.model.flower.Iris;
import java.util.ArrayList;
import java.util.Random;

public class DataSet {
    private final ArrayList<Iris> data;

    public DataSet(ArrayList<Iris> data) {
        this.data = new ArrayList<>(data);
    }

    public Iris getRandomIris() {
        Random rand = new Random();
        Iris randomElement = data.get(rand.nextInt(data.size()));
        data.remove(randomElement);
        return randomElement;
    }
}
