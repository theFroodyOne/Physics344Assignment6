/**
 * The original Nagel-Schreckenberg model on a straight stretch of road.
 *
 * <p>
 *     Boundary conditions are such that a vehicle appears at one end of the road at every
 *     time-step with some probability {@code q} at an initial velocity of 0, works its way down
 *     the road in accordance with the normal model rules, and disappears off the road once it
 *     reaches the other end.
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
     * Current time-step in the simulation
     */
    int timeStep;
    /**
     * The number of vehicles that have passed over the road so far
     */
    int vehiclesPassed;
    /**
     * A running average of how man time steps each vehicle spends on the road
     */
    int averageTimeOnRoad;
    /**
     * The road itself, holding {@code true} if there is a vehicle in that position
     */
    Vehicle[] road;

    /**
     * Constructor. Sets up all the fields with their necessary values
     * @param q Probability of a vehicle appearing at the start end of the road for each time-step
     * @param p Probability of any one vehicle randomly slowing down for reasons unknown
     * @param v Maximum speed a vehicle can achieve on this stretch of road
     * @param l Length(number of cells) of this stretch of road
     */
    public SimpleModel(double q, double p, int v, int l){
        if(q <= 1 && q >= 0 && p <= 1 && p >= 0 && v > 0 && l > 0) {
            this.q = q;
            this.p = p;
            this.v = v;
            this.l = l;
            road = new Vehicle[l];
            timeStep = 0;
        }else{
            throw new NumberFormatException("Probabilities p and q must be between 0 and 1");
        }
    }

    /**
     * Move the simulation forward by a single time-step
     */
    public void step(){
        //add vehicles at start point
        if(Math.random() < q && road[0] == null){
            road[0] = new Vehicle();
        }
        for(int i = 0; i < l; i ++) {
            if (road[i] == null) {
                continue;
            }
            //acceleration
            road[i].timeOnRoad++;
            if (road[i].v < v) {
                road[i].v++;
            }
            //slowing down
            for (int j = 1; j <= road[i].v; j++) {
                try {
                    if (road[i + j] != null) {
                        road[i].v = j - 1;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    break;
                }
            }
            //randomisation
            if (road[i].v > 0 && Math.random() < p) {
                road[i].v--;
            }
        }
        for(int i = l -1; i >= 0; i --){
            if (road[i] == null) {
                continue;
            }
            //motion
            if(road[i].v != 0) {
                try {
                    road[i + road[i].v] = road[i];
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("a: " + i + " " + road[i].v);
                    System.out.println("b: " + road[i].timeOnRoad);
                    averageTimeOnRoad += road[i].timeOnRoad;
                    vehiclesPassed ++;
                }
                road[i] = null;
            }
        }
        timeStep ++;
    }

    /**
     * Run the simulation for a certain number of time steps
     * @param numSteps Number of steps to run the simulation for
     */
    public void run(int numSteps){
        for(int i = 0; i < numSteps; i ++){
            step();
            //for debugging
            //System.out.println(this);
        }
        System.out.println("The average vehicle spent " + averageTimeOnRoad/vehiclesPassed + " time-steps on the road");
        System.out.println("The \"vehicle current\" was " + (double)vehiclesPassed/timeStep);
    }

    /**
     * Return a {@code String} representation of the roadway at this {@code timeStep}
     * @return A {@code String} representation of the roadway at this {@code timeStep}. Empty
     * spaces are indicated by _ while the presence of a vehicle is indicated by *
     */
    public String toString(){
        StringBuilder ans = new StringBuilder();
        ans.append(timeStep);
        ans.append(": ");
        for(int i = 0; i < l; i ++){
            if(road[i] == null){
                ans.append("_");
            }else{
                ans.append(road[i].v);
            }
        }
        return ans.toString();
    }

    public static void main(String[] args){
        SimpleModel sm = new SimpleModel(0.9, 0.9, 5, 100);
        sm.run(100);
    }
}
