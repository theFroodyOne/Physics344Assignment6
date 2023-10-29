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

    @Override
    public void step(){
        //add vehicles at start point
        if(Math.random() < q && road[0] == null){
            road[0] = new Vehicle();
        }
        //todo add vehicles at junctions
        //incoming on Merriman
        if(Math.random() < MerrimanIn && road[MerrimanPos] == null){
            road[MerrimanPos] = new Vehicle();
        }
        //incoming on George Blake
        if(Math.random() < GeorgeBlakeIn && road[GeorgeBlakePos] == null){
            road[GeorgeBlakePos] = new Vehicle();
        }
        //incoming on Alexander
        if(Math.random() < AlexanderIn && road[AlexanderPos] == null){
            road[AlexanderPos] = new Vehicle();
        }
        for(int i = 0; i < l; i ++) {
            if (road[i] == null) {
                continue;
            }
            //todo remove vehicles at junctions(% of those that will pass/reach junction at next time-step)
            //find those that will reach junction in next time-step
            //select those that will turn
            //those that will turn slow down so as not to overshoot junction
            //turners removed from road next time-step
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
                    averageTimeOnRoad += road[i].timeOnRoad;
                    vehiclesPassed ++;
                }
                road[i] = null;
            }
        }
        timeStep ++;
    }

    public static void main(String[] args){
        //
    }
}
