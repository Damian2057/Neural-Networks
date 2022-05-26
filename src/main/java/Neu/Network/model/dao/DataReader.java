package Neu.Network.model.dao;

import Neu.Network.model.exceptions.dao.FileOperationException;
import Neu.Network.model.flower.Iris;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Objects;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

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