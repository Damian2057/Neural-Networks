package Neu.Network.model.math;

import java.io.Serializable;

public class Sigmoid implements Serializable {

    public double sigmoid(double sum) {
        return 1.0/(1.0 + java.lang.Math.exp(-sum));
    }

    public double dsigmoid(double sum) {
        return sum * (1-sum);
    }
}
