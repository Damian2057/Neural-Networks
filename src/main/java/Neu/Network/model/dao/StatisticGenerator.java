package Neu.Network.model.dao;

import Neu.Network.charts.Cord;
import Neu.Network.model.exceptions.dao.FileOperationException;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class StatisticGenerator {

    public static void saveError(String from ,int neuron ,ArrayList<Cord> error) {
        try {
            String name = "Neuron_" + from + neuron + ".txt";
            File fout = new File("@../../statistics/"+name);
            FileOutputStream fos = new FileOutputStream(fout);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (Cord cord : error) {
                bw.write(cord.getX() + "," + cord.getY());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            throw new FileOperationException("Error generating statistics neuron number: "
                    + neuron);
        }
    }

    public static void saveErrorOnSingleNeuron(String from, int neuron, int epoch, double error) {
        String name = "Neuron_" + from + neuron + ".txt";
        try(FileWriter fileWriter = new FileWriter("@../../statistics/" + name,true)) {
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(epoch + "," + error);

        } catch (IOException e) {
            e.printStackTrace();
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
                    bw.write(weights[i][j] + " ");
                }
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
           e.printStackTrace();
            throw new FileOperationException("Error during saving weights to  file number: ");
        }
    }

    public static String getCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("_dd-HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}