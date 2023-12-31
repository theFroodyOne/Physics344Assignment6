import java.io.FileWriter;
import java.io.IOException;

/**
 * The original Nagel-Schreckenberg model on a straight stretch of road, used for phase 1.
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
    protected final double q;
    /**
     * Probability of any one vehicle randomly slowing down for reasons unknown
     */
    protected final double p;
    /**
     * Maximum speed a vehicle can achieve on this stretch of road
     */
    protected final int v;
    /**
     * Length(number of cells) of this stretch of road
     */
    protected final int l;
    /**
     * Current time-step in the simulation
     */
    protected int timeStep;
    /**
     * The number of vehicles that have passed over the road so far
     */
    protected int vehiclesPassed;
    /**
     * A running average of how man time steps each vehicle spends on the road
     */
    protected int averageTimeOnRoad;
    /**
     * Total of speed readings
     */
    protected double averageSpeed;
    /**
     * Number of speed readings to calculate average
     */
    protected int averageSpeedReadings;
    /**
     * The road itself
     */
    protected final Vehicle[] road;

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
        addVehicles();
        acceleration();
        slowDown();
        randomize();
        move();
        timeStep ++;
    }

    /**
     * Add vehicles to the start of the road with probability {@code q}
     */
    protected void addVehicles(){
        if(Math.random() < q && road[0] == null){
            road[0] = new Vehicle();
        }
    }

    /**
     * Vehicles not moving at the maximum speed accelerate
     */
    protected void acceleration(){
        for(int i = 0; i < l; i ++) {
            if (road[i] == null) {
                continue;
            }
            if (road[i].v < v) {
                road[i].v++;
            }
        }
    }

    /**
     * Vehicles must slow down so as not to hit another vehicle in front of it
     */
    protected void slowDown(){
        for(int i = 0; i < l; i ++) {
            if (road[i] == null) {
                continue;
            }
            for (int j = 1; j <= road[i].v; j++) {
                try {
                    if (road[i + j] != null) {
                        road[i].v = j - 1;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    break;
                }
            }
        }
    }

    /**
     * Some fraction {@code p} of vehicles slow down randomly
     */
    protected void randomize(){
        for(int i = 0; i < l; i ++) {
            if (road[i] == null) {
                continue;
            }
            if (road[i].v > 0 && Math.random() < p) {
                road[i].v--;
            }
        }
    }

    /**
     * Move vehicles forward at the end of the time-step and remove them from the road if they
     * reach the end
     */
    protected void move(){
        for(int i = l -1; i >= 0; i --){
            if (road[i] == null) {
                continue;
            }
            averageSpeed += road[i].v;
            averageSpeedReadings ++;
            road[i].timeOnRoad++;
            if(road[i].v != 0) {
                try {
                    road[i + road[i].v] = road[i];
                } catch (ArrayIndexOutOfBoundsException e) {
                    averageTimeOnRoad += road[i].timeOnRoad;
                    vehiclesPassed ++;
                }
                road[i] = null;
            }
        }
    }

    /**
     * Run the simulation for a certain number of time steps
     * @param numSteps Number of steps to run the simulation for
     */
    public double run(int numSteps){
        for(int i = 0; i < numSteps; i ++){
            step();
            //for debugging
            //System.out.println(this);
        }
        return (double)averageTimeOnRoad/vehiclesPassed;
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

    /**
     * Main method for generating and storing the data
     * @param args Superfluous
     */
    public static void main(String[] args){
        int runs = 1000;
        int l = 80;
        try {
            FileWriter fw = new FileWriter("/home/zander/IdeaProjects/Physics344Assignment6/data/phase1/data_fixed_l.csv");
            fw.write("q,p,v,l,timeOnRoad\n");
            for (double q = 0.1; q <= 1; q += 0.1) {
                for (double p = 0; p <= 0.9; p += 0.1) {
                    for (int v = 3; v <= 12; v += 3) {
                        double avg = 0;
                        fw.write(q + "," + p + "," + v + "," + l + ",");
                        for(int i = 0; i < runs; i ++){
                            avg += new SimpleModel(q, p, v, l).run(10000);
                        }
                        fw.write(avg/runs + "");
                        fw.write("\n");
                    }
                }
            }
            fw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
