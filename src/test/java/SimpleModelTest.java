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

    /**
     * Test step() with a single vehicle accelerating on the roadway
     */
    @Test
    public void testStep(){
        SimpleModel sm = new SimpleModel(0.0, 0.0, 3, 10);
        sm.road[0] = new Vehicle();
        sm.step();
        assertEquals(sm.road[1].v, 1);
        sm.step();
        assertEquals(sm.road[3].v, 2);
        sm.step();
        assertEquals(sm.road[6].v, 3);
        sm.step();
        assertEquals(sm.road[9].v, 3);
    }

    /**
     * Test step() when there are multiple vehicles on the road potentially causing interference
     */
    @Test
    public void testStepMultipleVehicles(){
        SimpleModel sm = new SimpleModel(0.0, 0.0, 5, 10);
        sm.road[2] = new Vehicle();
        sm.road[0] = new Vehicle();
        sm.road[0].v = 4;
        sm.step();
        assertEquals(sm.road[3].v, 1);
        assertEquals(sm.road[1].v, 1);
    }
}
