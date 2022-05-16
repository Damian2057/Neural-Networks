package Neu.Network.model.dao;

import Neu.Network.model.exceptions.dao.FileOperationException;
import Neu.Network.model.flower.Iris;
import java.io.BufferedReader;
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

    public static boolean getFileSaveFlag() {
        try {
            Object obj = new JSONParser().parse(new FileReader("config.json"));
            JSONObject jo = (JSONObject) obj;
            var fileSaveMode =  jo.get("fileSaveMode");
            return Objects.equals(fileSaveMode.toString(), "true");
        } catch (Exception e) {
            throw new FileOperationException("Error reading the configuration file");
        }
    }

    public static int getJump() {
        try {
            Object obj = new JSONParser().parse(new FileReader("config.json"));
            JSONObject jo = (JSONObject) obj;
            var jump =  jo.get("epochJump");
            return Integer.parseInt(jump.toString());
        } catch (Exception e) {
            throw new FileOperationException("Error reading the configuration file");
        }
    }

    public static int getNumberOfHiddenNeurons() {
        try {
            Object obj = new JSONParser().parse(new FileReader("config.json"));
            JSONObject jo = (JSONObject) obj;
            var number =  jo.get("numberOfHiddenNeurons");
            return Integer.parseInt(number.toString());
        } catch (Exception e) {
            throw new FileOperationException("Error reading the configuration file");
        }
    }

    public static int getNumberOfInPuts() {
        try {
            Object obj = new JSONParser().parse(new FileReader("config.json"));
            JSONObject jo = (JSONObject) obj;
            var number =  jo.get("numberOfInPuts");
            return Integer.parseInt(number.toString());
        } catch (Exception e) {
            throw new FileOperationException("Error reading the configuration file");
        }
    }

    public static int getNumberOfOutPuts() {
        try {
            Object obj = new JSONParser().parse(new FileReader("config.json"));
            JSONObject jo = (JSONObject) obj;
            var number =  jo.get("numberOfOutPuts");
            return Integer.parseInt(number.toString());
        } catch (Exception e) {
            throw new FileOperationException("Error reading the configuration file");
        }
    }
}