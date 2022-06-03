package Neu.Network.model.dao;

import Neu.Network.model.exceptions.dao.FileOperationException;
import Neu.Network.model.flower.Iris;

import java.io.*;
import java.util.ArrayList;

public class DataOperation {

    private static final String readPath = "@../../Data/";
    private static final String savePath = "@../../outputData/";

    public static ArrayList<Iris> readData(String fileName) {
        try {
            ArrayList<Iris> temp = new ArrayList<>();
            BufferedReader csvReader = new BufferedReader(new FileReader(readPath +fileName));
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

    public static void writeData(String fileName, ArrayList<Iris> data) {
        try {
            File fout = new File(savePath + fileName + ".csv");
            FileOutputStream fos = new FileOutputStream(fout);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (Iris datum : data) {
                bw.write(datum.getSepalLength() + ","
                        + datum.getSepalWidth() + ","
                        + datum.getPetalLength() + ","
                        + datum.getPetalWidth() + ","
                        + datum.getType());
                bw.newLine();
            }

            bw.close();
        } catch (IOException e) {
            throw new FileOperationException("Error during saving weights to  file number: ");
        }
    }
}