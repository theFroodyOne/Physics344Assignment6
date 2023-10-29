/**
 * Vehicle which may find itself on a roadway during the simulation
 */
public class Vehicle {
    /**
     * Current actual speed of the vehicle
     */
    int v;
    /**
     * How long this vehicle has been on a road
     */
    int timeOnRoad;
    /**
     * Destination of this vehicle to determine where it should turn etc.
     */
    String destination;

    /**
     * Initialises a {@code Vehicle} with a particular probability of randomly slowing down and a
     * maximum velocity
     */
    public Vehicle(){
        this.v = 0;
        this.timeOnRoad = 0;
        this.destination = null;
    }
}
