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
        //todo add vehicles entering/exiting at junctions
    }
}
