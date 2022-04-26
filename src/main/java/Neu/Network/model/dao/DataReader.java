package Neu.Network.model.dao;

import Neu.Network.model.exceptions.dao.FileOperationException;
import Neu.Network.model.flower.Iris;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DataReader {

    private static final String path = "@../../Data/";

    public static ArrayList<Iris> readData(String fileName) {
        try {
            ArrayList<Iris> temp = new ArrayList<>();
            BufferedReader csvReader = new BufferedReader(new FileReader(path+fileName));
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                temp.add(new Iris(data[0],data[1],data[2],data[3],data[4]));
            }
            csvReader.close();
            return temp;
        } catch (Exception e) {
            throw new FileOperationException("Error while downloading data to the program");
        }
    }
}
