package Neu.Network.model.dao;

import Neu.Network.global.GlobalVariables;
import Neu.Network.model.exceptions.dao.FileOperationException;
import java.io.*;

public class StatisticGenerator {

    public static void saveEpochErrorStats(String fileName, int numberOfEpoch, double value) {
        try {
            String name = "Science_" + fileName + "_"+ GlobalVariables.iterator+".txt";
            GlobalVariables.iterator++;
            File fout = new File("@../../statistics/"+name);
            FileOutputStream fos = new FileOutputStream(fout);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            bw.write(String.valueOf(numberOfEpoch));
            bw.newLine();
            bw.write(String.valueOf(value));
            bw.close();
        } catch (IOException e) {
            throw new FileOperationException("Error generating statistics file number: "
                    + GlobalVariables.iterator + "during the era: "+ numberOfEpoch + "value: "+ value);
        }
    }

    public static void saveWeight(String nameOfFile, double[][] weights) {
        try {
            String name = nameOfFile + ".txt";
            File fout = new File("@../../statistics/" + name);
            FileOutputStream fos = new FileOutputStream(fout);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (int i = 0; i < weights.length; i++) {
                for (int j = 0; j < weights[0].length; j++) {
                    bw.write(String.valueOf(weights[i][j]) + " ");
                }
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            throw new FileOperationException("Error during saving weights to  file number: ");
        }
    }

    //TODO: collect data from testing
}