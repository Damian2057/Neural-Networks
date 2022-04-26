package Neu.Network.model.component;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NeuralTest {

    @Test
    public void runTestTest() {
        for (int i = 0; i < 1000000; i++) {
            Neural neural = new Neural(1);
            assertTrue(neural.getWeights().get(0) >= -0.5 && neural.getWeights().get(0) <= 0.5);
        }
    }
}
