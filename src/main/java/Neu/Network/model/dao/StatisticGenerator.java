package Neu.Network.model.dao;

import Neu.Network.config.GlobalConfiguration;

import java.io.*;

public class StatisticGenerator {
//TODO: collect data from testing


    public static void saveEpochStats(int numberOfEpoch, double value) {
        try {
            String name = "Science_"+ GlobalConfiguration.iterator+".txt";
            GlobalConfiguration.iterator++;
            File fout = new File("@../../statistics/"+name);
            FileOutputStream fos = new FileOutputStream(fout);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            bw.write(String.valueOf(numberOfEpoch));
            bw.newLine();
            bw.write(String.valueOf(value));
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
