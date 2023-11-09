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
     * Origin of the vehicle to compare like with like for TOR
     */
    String origin;
    /**
     * The route this vehicle followed so TOR for different routes can be compared
     */
    String route;

    /**
     * Initialises a {@code Vehicle} with a particular probability of randomly slowing down and a
     * maximum velocity
     */
    public Vehicle(){
        v = 0;
        timeOnRoad = 0;
        destination = null;
        origin = null;
        route = null;
    }
}
