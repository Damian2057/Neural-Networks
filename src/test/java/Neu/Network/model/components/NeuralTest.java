package Neu.Network.model.components;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NeuralTest {

    @Test
    public void weightTest() {
        for (int i = 0; i < 1000000; i++) {
            Layer neural = new Layer(1,1);
            assertTrue(neural.getVector()[0][0] >= -1 && neural.getVector()[0][0] <= 1);
        }
    }

    @Test
    public void noZeroWeightTest() {
        for (int i = 0; i < 10000000; i++) {
            Layer neural = new Layer(1,1);
            assertNotEquals(neural.getVector()[0][0], 0);
        }
    }
}