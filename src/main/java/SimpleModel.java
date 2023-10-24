/**
 * The original Nagel-Schreckenberg model on a straight stretch of road.
 *
 * <p>
 *     Boundary conditions are such that a vehicle appears at one end of the road at every
 *     time-step with some probability {@code q}, works its way down the road in accordance
 *     with the normal model rules, and disappears off the road once it reaches the other end
 * </p>
 */
public class SimpleModel {
    /**
     * Probability of a vehicle appearing at the start end of the road for each time-step
     */
    double q;
    /**
     * Probability of any one vehicle randomly slowing down for reasons unknown
     */
    double p;
    /**
     * Maximum speed a vehicle can achieve on this stretch of road
     */
    int v;
    /**
     * Length(number of cells) of this stretch of road
     */
    int l;
    /**
     * The road itself, holding {@code true} if there is a vehicle in that position
     */
    boolean[] road;

    /**
     * Constructor. Sets up all the fields with their necessary values
     * @param q Probability of a vehicle appearing at the start end of the road for each time-step
     * @param p Probability of any one vehicle randomly slowing down for reasons unknown
     * @param v Maximum speed a vehicle can achieve on this stretch of road
     * @param l Length(number of cells) of this stretch of road
     */
    public SimpleModel(double q, double p, int v, int l){
        if(q <= 1 && q >= 0 && p <= 1 && p >= 0) {
            this.q = q;
            this.p = p;
            this.v = v;
            this.l = l;
            road = new boolean[l];
        }else{
            throw new NumberFormatException("Probabilities p and q must be between 0 and 1");
        }
    }

    /**
     * Move the simulation forward by a single time-step
     */
    public void step(){
        //todo
    }
}
