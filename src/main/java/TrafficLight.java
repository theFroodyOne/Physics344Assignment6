/**
 * Traffic lights at junctions for phase 3
 */
public class TrafficLight {
    /**
     * Can vehicles on the main road move past this traffic light now?
     */
    private boolean isGreen;
    /**
     * How long does this traffic light remain in a particular state?
     */
    private final int onTime;
    /**
     * How long before this traffic changes state?
     */
    private int timeToChange;

    /**
     * Constructor
     * @param onTime Set how long this traffic light remains in a particular state (in time-steps)
     */
    public TrafficLight(int onTime){
        isGreen = true;
        this.onTime = onTime;
        timeToChange = onTime;
    }

    /**
     * Check the state of this traffic light
     * @return The state of this traffic light
     */
    public boolean check(){
        timeToChange --;
        if(timeToChange == 0){
            change();
            timeToChange = onTime;
        }
        return isGreen;
    }

    /**
     * Change the current state of this traffic light
     */
    public void change(){
        isGreen = !isGreen;
    }
}
