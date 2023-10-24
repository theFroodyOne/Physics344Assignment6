import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class SimpleModelTest {

    /**
     * Tests that the constructor initialises the road array correctly
     */
    @Test
    public void checkRoadLength(){
        SimpleModel sm = new SimpleModel(0.5, 0, 5, 10);
        assertEquals(sm.road.length, 10);
    }

    /**
     * Tests that the constructor does not accept invalid numerical inputs
     */
    @Test
    public void invalidProbability(){
        boolean thrown = false;
        try {
            SimpleModel sm = new SimpleModel(1.2, 0.5, 5, 10);
        }catch(NumberFormatException e){
            thrown = true;
        }
        assertTrue(thrown);
        thrown = false;
        try {
            SimpleModel sm = new SimpleModel(0.5, -0.4, 5, 10);
        }catch(NumberFormatException e){
            thrown = true;
        }
        assertTrue(thrown);
        thrown = false;
        try {
            SimpleModel sm = new SimpleModel(0.5, 0.4, 0, 10);
        }catch(NumberFormatException e){
            thrown = true;
        }
        assertTrue(thrown);
        thrown = false;
        try {
            SimpleModel sm = new SimpleModel(0.5, 0.4, 1, 0);
        }catch(NumberFormatException e){
            thrown = true;
        }
        assertTrue(thrown);
    }
}
