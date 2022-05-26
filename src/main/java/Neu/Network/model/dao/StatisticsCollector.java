package Neu.Network.model.dao;

import Neu.Network.charts.Cord;
import Neu.Network.model.exceptions.dao.FileOperationException;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class StatisticsCollector {

    public static void saveErrorFromWholeNetwork(String from , ArrayList<Cord> error) {
        try {
            String name = "Neurons_" + Json.getNumberOfHiddenNeurons() + "_" + from + ".csv";
            File fout = new File("@../../PythonCharts/statistics/" + name);
            FileOutputStream fos = new FileOutputStream(fout);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (Cord cord : error) {
                bw.write(cord.getX() + "," + cord.getY());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            throw new FileOperationException("Error generating statistics");
        }
    }

    public static void saveErrorOnSingleNeuron(String from, int neuron, int epoch, double error) {
        String name = "Neurons_" + Json.getNumberOfHiddenNeurons() + "_" + from + "_" + neuron + ".csv";
        try(FileWriter fileWriter = new FileWriter("@../../PythonCharts/statistics/" + name,true)) {
            BufferedWriter bw = new BufferedWriter(fileWriter);
            bw.write(epoch + "," + error);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            throw new FileOperationException("Error generating statistics on "+ from + " in " + neuron);
        }
    }

    public static void saveWeight(String nameOfFile, double[][] weights) {
       try {
            String name = nameOfFile + getCurrentTime() + ".csv";
            File fout = new File("@../../PythonCharts/statistics/" + name);
            FileOutputStream fos = new FileOutputStream(fout);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (int i = 0; i < weights.length; i++) {
                for (int j = 0; j < weights[0].length; j++) {
                    bw.write(weights[i][j] + ",");
                }
                bw.newLine();
            }

            bw.close();
        } catch (IOException e) {
            throw new FileOperationException("Error during saving weights to  file number: ");
        }
    }

    public static String getCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("_dd-HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}