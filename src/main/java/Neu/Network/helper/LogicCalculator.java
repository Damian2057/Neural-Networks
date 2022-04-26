package Neu.Network.helper;

import Neu.Network.model.flower.Irys;

import java.util.ArrayList;

public class LogicCalculator {
    //0 - 1 0 0 0
    //1 - 0 1 0 0
    //2 - 0 0 1 0

    public static void Summarize(ArrayList<Double> result, Irys flower) {
        if(flower.getType() == 0) {
            if(result.get(0) > 0.8 && result.get(1) < 0.5 && result.get(2) < 0.5 && result.get(3) < 0.5) {
                System.out.println("Flower type FIRST found as expected, The significant factor has been reached:" + result.get(0));
            } else {
                System.out.println("The obtained data did not clearly identify the type of flower");
            }
        } else if(flower.getType() == 1) {
            if(result.get(0) > 0.8 && result.get(1) < 0.5 && result.get(2) < 0.5 && result.get(3) < 0.5) {
                System.out.println("Flower type SECOND found as expected, The significant factor has been reached:" + result.get(0));
            } else {
                System.out.println("The obtained data did not clearly identify the type of flower");
            }
        } else {
            if(result.get(0) > 0.8 && result.get(1) < 0.5 && result.get(2) < 0.5 && result.get(3) < 0.5) {
                System.out.println("Flower type THIRD found as expected, The significant factor has been reached:" + result.get(0));
            } else {
                System.out.println("The obtained data did not clearly identify the type of flower");
            }
        }
    }
}
