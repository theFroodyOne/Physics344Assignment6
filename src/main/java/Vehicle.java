/**
 * Vehicle which may find itself on a roadway during the simulation
 */
public class Vehicle {
    /**
     * Probability of this {@code Vehicle} randomly slowing down for reasons unknown
     */
    double p;
    /**
     * Maximum speed this {@code Vehicle} can achieve
     */
    int maxSpeed;
    /**
     * Current actual speed of the vehicle
     */
    int v;
    /**
     * How long this vehicle has been on a road
     */
    int timeOnRoad;

    /**
     * Initialises a {@code Vehicle} with a particular probability of randomly slowing down and a
     * maximum velocity
     * @param p Probability of this {@code Vehicle} randomly slowing down for reasons unknown
     * @param maxSpeed Maximum speed this {@code Vehicle} can achieve
     */
    public Vehicle(double p, int maxSpeed){
        this.p = p;
        this.maxSpeed = maxSpeed;
        this.v = 0;
    }
}
