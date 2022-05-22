package Neu.Network.model.dao;

import Neu.Network.model.exceptions.dao.FileOperationException;
import Neu.Network.model.flower.EncoderData;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Objects;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class DataReader {

    private static final String path = "@../../Data/";

    public static ArrayList<EncoderData> readData(String fileName) {
        try {
            ArrayList<EncoderData> temp = new ArrayList<>();
            BufferedReader csvReader = new BufferedReader(new FileReader(path+fileName));
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                temp.add(new EncoderData(data[0],data[1],data[2],data[3]));
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

    public static int getJumpOnDisplay() {
        try {
            Object obj = new JSONParser().parse(new FileReader("config.json"));
            JSONObject jo = (JSONObject) obj;
            var jump =  jo.get("jumpOnDisplay");
            return Integer.parseInt(jump.toString());
        } catch (Exception e) {
            throw new FileOperationException("Error reading the configuration file");
        }
    }

    public static double getAccuracy() {
        try {
            Object obj = new JSONParser().parse(new FileReader("config.json"));
            JSONObject jo = (JSONObject) obj;
            var number =  jo.get("accuracy");
            return Double.parseDouble(number.toString());
        } catch (Exception e) {
            throw new FileOperationException("Error reading the configuration file");
        }
    }



    public static double getMomentumValue() {
        try {
            Object obj = new JSONParser().parse(new FileReader("config.json"));
            JSONObject jo = (JSONObject) obj;
            var number =  jo.get("momentumValue");
            return Double.parseDouble(number.toString());
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

    public static double getLearningFactor() {
        try {
            Object obj = new JSONParser().parse(new FileReader("config.json"));
            JSONObject jo = (JSONObject) obj;
            var number =  jo.get("learningFactor");
            return Double.parseDouble(number.toString());
        } catch (Exception e) {
            throw new FileOperationException("Error reading the configuration file");
        }
    }

    public static boolean getBiasMode() {
        try {
            Object obj = new JSONParser().parse(new FileReader("config.json"));
            JSONObject jo = (JSONObject) obj;
            var fileSaveMode =  jo.get("biasStatus");
            return Objects.equals(fileSaveMode.toString(), "true");
        } catch (Exception e) {
            throw new FileOperationException("Error reading the configuration file");
        }
    }

    public static boolean getMomentumMode() {
        try {
            Object obj = new JSONParser().parse(new FileReader("config.json"));
            JSONObject jo = (JSONObject) obj;
            var fileSaveMode =  jo.get("momentumStatus");
            return Objects.equals(fileSaveMode.toString(), "true");
        } catch (Exception e) {
            throw new FileOperationException("Error reading the configuration file");
        }
    }

    public static boolean GetDeleteMode() {
        try {
            Object obj = new JSONParser().parse(new FileReader("config.json"));
            JSONObject jo = (JSONObject) obj;
            var fileSaveMode =  jo.get("fileDeleteMode");
            return Objects.equals(fileSaveMode.toString(), "true");
        } catch (Exception e) {
            throw new FileOperationException("Error reading the configuration file");
        }
    }

    public static int getNumberOfEpochs() {
        try {
            Object obj = new JSONParser().parse(new FileReader("config.json"));
            JSONObject jo = (JSONObject) obj;
            var jump =  jo.get("numberOfEpochs");
            return Integer.parseInt(jump.toString());
        } catch (Exception e) {
            throw new FileOperationException("Error reading the configuration file");
        }
    }

    public static void CreateDirectories() {
        try {
            File theData = new File("@../../Data");
            if (!theData.exists()){
                theData.mkdirs();
            }
            File theArchives = new File("@../../archives");
            if (!theArchives.exists()){
                theArchives.mkdirs();
            }
            File theStats = new File("@../../PythonCharts/statistics");
            if (!theStats.exists()){
                theStats.mkdirs();
            }
        } catch (Exception e) {
            throw new FileOperationException("error while creating paths");
        }
    }

    public static boolean getCreateNewNetworkMode() {
        try {
            Object obj = new JSONParser().parse(new FileReader("config.json"));
            JSONObject jo = (JSONObject) obj;
            var fileSaveMode =  jo.get("createNewNetwork");
            return Objects.equals(fileSaveMode.toString(), "true");
        } catch (Exception e) {
            throw new FileOperationException("Error reading the configuration file");
        }
    }

    public static boolean getLoadPrevNetworkMode() {
        try {
            Object obj = new JSONParser().parse(new FileReader("config.json"));
            JSONObject jo = (JSONObject) obj;
            var fileSaveMode =  jo.get("loadPrevNetwork");
            return Objects.equals(fileSaveMode.toString(), "true");
        } catch (Exception e) {
            throw new FileOperationException("Error reading the configuration file");
        }
    }


}