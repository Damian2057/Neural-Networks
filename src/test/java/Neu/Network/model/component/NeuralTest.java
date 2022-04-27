package Neu.Network.model.component;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NeuralTest {

    @Test
    public void weightTest() {
        for (int i = 0; i < 1000000; i++) {
            Layer neural = new Layer(1,1);
            assertTrue(neural.getWeights()[0][0] >= -0.5 && neural.getWeights()[0][0] <= 0.5);
        }
    }

    @Test
    public void noZeroWeightTest() {
        for (int i = 0; i < 10000000; i++) {
            Layer neural = new Layer(1,1);
            assertNotEquals(neural.getWeights()[0][0], 0);
        }
    }
}
