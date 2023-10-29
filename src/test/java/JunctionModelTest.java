import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class JunctionModelTest {
    /**
     * Test that new functionality re joining from junctions works as intended
     */
    @Test
    public void testJoin(){
        JunctionModel jm = new JunctionModel(0, 0, 1, 0, 1, 0, 1, 0);
        jm.road[29] = new Vehicle();
        jm.road[29].destination = "hmm";
        jm.step();
        assertEquals(jm.road[6].v, 1);
        assertNull(jm.road[29]);
        assertEquals(jm.road[30].v, 1);
        assertEquals(jm.road[30].destination, "hmm");
        assertNull(jm.road[31]);
        assertEquals(jm.road[61].v, 1);
    }

    /**
     * Test that new functionality re leaving at junctions works as intended
     */
    @Test
    public void testLeave(){
        JunctionModel jm = new JunctionModel(0, 0, 0, 1, 0, 0, 0, 1);
        jm.road[2] = new Vehicle();
        jm.road[2].v = 4;
        jm.road[27] = new Vehicle();
        jm.road[28] = new Vehicle();
        jm.road[28].v = 1;
        jm.road[28].destination = "George Blake";
        jm.road[62] = new Vehicle();
        jm.step();
        assertEquals(jm.road[4].destination, "Merriman");
        assertNull(jm.road[7]);
        assertNull(jm.road[29]);
        assertEquals(jm.road[63].v, 1);
        jm.step();
        assertNull(jm.road[4]);
        assertNull(jm.road[6]);
        assertNull(jm.road[7]);
        assertNull(jm.road[28].destination);
    }
}
