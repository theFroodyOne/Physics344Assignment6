import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class SimpleModelTest {

    /**
     * Tests that the constructor initialises the road array correctly
     */
    @Test
    public void checkRoadLength(){
        SimpleModel sm = new SimpleModel(0.5, 0.5, 5, 10);
        assertEquals(sm.road.length, 10);
    }
}
