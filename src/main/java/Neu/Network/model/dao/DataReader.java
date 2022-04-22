package Neu.Network.model.dao;

import Neu.Network.model.flower.Irys;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DataReader {

    private static final String path = "@../../Data/data.csv";

    public static ArrayList<Irys> readData() throws IOException {
        ArrayList<Irys> temp = new ArrayList<>();
        BufferedReader csvReader = new BufferedReader(new FileReader(path));
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            temp.add(new Irys(data[0],data[1],data[2],data[3],data[4]));
        }
        csvReader.close();
        return temp;
    }

}
