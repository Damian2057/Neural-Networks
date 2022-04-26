package Neu.Network.model.math;

import java.io.Serializable;

public class Math implements Serializable {

    public double sigmoid(double sum) {
        return 1.0/(1.0 + java.lang.Math.exp(-sum));
    }
}
