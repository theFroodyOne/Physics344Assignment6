import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

/**
 * Extend {@code SimpleModel} to include vehicles entering and exiting the road at junctions.
 * Some variables are fixed to reflect the real-world setup that is being modelled.
 */
public class JunctionModel extends SimpleModel{
    /**
     * Top speed in this version of the model. See report for justification
     */
    protected static final int v = 9;
    /**
     * Length of the road in this version of the model. See report for justification
     */
    protected static final int l = 74;
    /**
     * Position of the junction with Merriman Avenue
     */
    protected static final int MerrimanPos = 5;
    /**
     * Position of the junction with George Blake Street
     */
    protected static final int GeorgeBlakePos = 29;
    /**
     * Position of the junction with Alexander Street
     */
    protected static final int AlexanderPos = 60;
    /**
     * Probability of a vehicle entering from Merriman Avenue
     */
    double MerrimanIn;
    /**
     * Probability of a vehicle exiting via Merriman Avenue
     */
    double MerrimanOut;
    /**
     * Probability of a vehicle entering from George Blake Street
     */
    double GeorgeBlakeIn;
    /**
     * Probability of a vehicle exiting via George Blake Street
     */
    double GeorgeBlakeOut;
    /**
     * Probability of a vehicle entering from Alexander Street
     */
    double AlexanderIn;
    /**
     * Probability of a vehicle exiting via Alexander Street
     */
    double AlexanderOut;

    /**
     * Constructor. Sets up all the fields with their necessary values
     *
     * @param q Probability of a vehicle appearing at the start end of the road for each time-step
     * @param p Probability of any one vehicle randomly slowing down for reasons unknown
     */
    public JunctionModel(double q, double p, double mIn, double mOut, double gIn, double gOut, double aIn, double aOut) {
        super(q, p, v, l);
        MerrimanIn = mIn;
        MerrimanOut = mOut;
        GeorgeBlakeIn = gIn;
        GeorgeBlakeOut = gOut;
        AlexanderIn = aIn;
        AlexanderOut = aOut;
        averageSpeed = 0;
        averageSpeedReadings = 0;
    }

    /**
     * Run the simulation for a certain number of time steps
     * @param numSteps Number of steps to run the simulation for
     */
    @Override
    public double run(int numSteps){
        for(int i = 0; i < numSteps; i ++){
            step();
            //for debugging
            //System.out.println(this);
        }
        return averageSpeed/averageSpeedReadings;
    }

    /**
     * Expanded for adding vehicles at junctions
     */
    @Override
    protected void addVehicles(){
        if(Math.random() < q && road[0] == null){
            road[0] = new Vehicle();
        }
        if(Math.random() < MerrimanIn && road[MerrimanPos] == null){
            road[MerrimanPos] = new Vehicle();
        }
        if(Math.random() < GeorgeBlakeIn && road[GeorgeBlakePos] == null){
            road[GeorgeBlakePos] = new Vehicle();
        }
        if(Math.random() < AlexanderIn && road[AlexanderPos] == null){
            road[AlexanderPos] = new Vehicle();
        }
    }

    /**
     * Expand acceleration step to include removing vehicles at junctions, marking them for
     * removal and slowing down to avoid overshooting said junctions
     */
    @Override
    protected void acceleration(){
        for(int i = 0; i < l; i ++) {
            if (road[i] == null) {
                continue;
            }
            //remove vehicles at junctions
            if((Objects.equals(road[i].destination, "Merriman") && i == MerrimanPos - 1) ||
                    (Objects.equals(road[i].destination, "George Blake") && i == GeorgeBlakePos - 1) ||
                    (Objects.equals(road[i].destination, "Alexander") && i == AlexanderPos - 1)){
                vehiclesPassed ++;
                road[i] = null;
                continue;
            }
            if (road[i].v < v) {
                road[i].v++;
            }
            //decide if vehicle will turn off at junction
            if (road[i].destination == null && i < MerrimanPos && i + road[i].v >= MerrimanPos && Math.random() < MerrimanOut){
                road[i].destination = "Merriman";
                road[i].v = MerrimanPos - i - 1;
            }
            if (road[i].destination == null && i < GeorgeBlakePos && i + road[i].v >= GeorgeBlakePos && Math.random() < GeorgeBlakeOut){
                road[i].destination = "George Blake";
                road[i].v = GeorgeBlakePos - i - 1;
            }
            if (road[i].destination == null && i < AlexanderPos && i + road[i].v >= AlexanderPos && Math.random() < AlexanderOut){
                road[i].destination = "Alexander";
                road[i].v = AlexanderPos - i - 1;
            }
        }
    }

    /**
     * Main method for generating and storing the data
     * @param args Superfluous
     */
    public static void main(String[] args){
        int runs = 1000, steps = 1440;
        try {
            FileWriter fw = new FileWriter("/home/zander/IdeaProjects/Physics344Assignment6/data/phase2/data.csv");
            fw.write("p, v\n");
            for(double p = 0; p < 1; p += 0.01) {
                double avg = 0;
                fw.write(p + ",");
                for (int i = 0; i < runs; i++) {
                    avg += new JunctionModel(0.7736, p, 0.7132, 0.3139, 0.3938, 0.4007, 0.7132, 0.3139).run(steps);
                }
                fw.write(avg / runs + "");
                fw.write("\n");
            }
            fw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
