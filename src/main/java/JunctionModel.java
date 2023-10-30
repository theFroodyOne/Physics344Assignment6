import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

//TODO replace probabilistic flows with queue and flow rate

public class JunctionModel extends SimpleModel{
    /**
     * Top speed in this version of the model. See report for justification
     */
    private static final int v = 9;
    /**
     * Length of the road in this version of the model. See report for justification
     */
    private static final int l = 74;
    /**
     * Position of the junction with Merriman Avenue
     */
    private static final int MerrimanPos = 5;
    /**
     * Position of the junction with George Blake Street
     */
    private static final int GeorgeBlakePos = 29;
    /**
     * Position of the junction with Alexander Street
     */
    private static final int AlexanderPos = 60;
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
    }

    /**
     * Update step() from SimpleModel to include vehicles entering/leaving at junctions
     */
    @Override
    public void step(){
        //add vehicles at start point & at junctions
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
        for(int i = 0; i < l; i ++) {
            if (road[i] == null) {
                continue;
            }
            //remove vehicle at junctions
            if((Objects.equals(road[i].destination, "Merriman") && i == MerrimanPos - 1) ||
                    (Objects.equals(road[i].destination, "George Blake") && i == GeorgeBlakePos - 1) ||
                    (Objects.equals(road[i].destination, "Alexander") && i == AlexanderPos - 1)){
                road[i] = null;
                continue;
            }
            //acceleration
            road[i].timeOnRoad++;
            if (road[i].v < v) {
                road[i].v++;
            }
            //decide if vehicle will turn off at junction
            if (road[i].destination == null && i <= MerrimanPos && i + road[i].v >= MerrimanPos && Math.random() < MerrimanOut){
                road[i].destination = "Merriman";
                road[i].v = MerrimanPos - i - 1;
            }
            if (road[i].destination == null && i <= GeorgeBlakePos && i + road[i].v >= GeorgeBlakePos && Math.random() < GeorgeBlakeOut){
                road[i].destination = "George Blake";
                road[i].v = GeorgeBlakePos - i - 1;
            }
            if (road[i].destination == null && i <= AlexanderPos && i + road[i].v >= AlexanderPos && Math.random() < AlexanderOut){
                road[i].destination = "Alexander";
                road[i].v = AlexanderPos - i - 1;
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
                    averageTimeOnRoad += road[i].timeOnRoad;
                    vehiclesPassed ++;
                }
                road[i] = null;
            }
        }
        timeStep ++;
    }

    /**
     * Main method for generating and storing the data
     * @param args Superfluous
     */
    public static void main(String[] args){
        int runs = 1000;
        try {
            FileWriter fw = new FileWriter("/home/zander/IdeaProjects/Physics344Assignment6/data/phase2/data.csv");
            //todo codify assumptions and generate some data
            JunctionModel jm = new JunctionModel(1, 0, 0, 1, 0, 0, 0, 0);
            jm.run(10);
            fw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
