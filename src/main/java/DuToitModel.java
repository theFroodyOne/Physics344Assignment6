public class DuToitModel extends SimpleModel{

    private static final int l = 60;

    /**
     * Constructor. Sets up all the fields with their necessary values
     *
     * @param q Probability of a vehicle appearing at the start end of the road for each time-step
     * @param p Probability of any one vehicle randomly slowing down for reasons unknown
     * @param v Maximum speed a vehicle can achieve on this stretch of road
     */
    public DuToitModel(double q, double p, int v) {
        super(q, p, v, l);
    }

    @Override
    protected void addVehicles(){
        //TODO take in vehicles from Merriman queue of FinalModel instead
    }

    @Override
    protected void move(){
        //TODO dump vehicles that fall off the end onto Alexander queue
    }
}
