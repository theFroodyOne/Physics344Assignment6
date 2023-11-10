/**
 * Model peculiarities of Du Toit street in phase 4
 */
public class DuToitModel extends SimpleModel{
    static final int l = 60;

    //TODO fix bug with TOR going off the rails

    /**
     * Constructor. Sets up all the fields with their necessary values
     * @param p Probability of any one vehicle randomly slowing down for reasons unknown
     * @param v Maximum speed a vehicle can achieve on this stretch of road
     */
    public DuToitModel(double p, int v) {
        super(0, p, v, l);
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
