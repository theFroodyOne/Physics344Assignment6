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
        //empty since FinalModel handles it
    }

    /**
     * Update so vehicles wait at the end of the road for FinalModel to pick them up
     */
    @Override
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
                    road[i].v = j - 1;
                    break;
                }
            }
        }
    }
}
