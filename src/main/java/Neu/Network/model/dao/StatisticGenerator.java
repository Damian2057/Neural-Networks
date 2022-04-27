package Neu.Network.model.dao;

import Neu.Network.config.GlobalVariables;
import Neu.Network.model.exceptions.dao.FileOperationException;
import java.io.*;

public class StatisticGenerator {

    public static void saveEpochStats(int numberOfEpoch, double value) {
        try {
            String name = "Science_"+ GlobalVariables.iterator+".txt";
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

    //TODO: collect data from testing
}
