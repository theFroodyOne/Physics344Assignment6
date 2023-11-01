import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;

public class TrafficLightModel extends JunctionModel{
    private static final int QUEUE_CAP = 100;
    /**
     * Queue for vehicles waiting to enter via Merriman Avenue
     */
    ArrayBlockingQueue<Vehicle> MerrimanQueue;
    /**
     * Queue for vehicles waiting to enter via George Blake Street
     */
    ArrayBlockingQueue<Vehicle> GeorgeBlakeQueue;
    /**
     * Queue for vehicles waiting to enter via Alexander Street
     */
    ArrayBlockingQueue<Vehicle> AlexanderQueue;
    /**
     * Traffic light controlling inflows from Merriman Avenue
     */
    TrafficLight MerrimanLight;
    /**
     * Traffic light controlling inflows from George Blake Street
     */
    TrafficLight GeorgeBlakeLight;
    /**
     * Traffic light controlling inflows from Alexander Street
     */
    TrafficLight AlexanderLight;
    /**
     * Constructor. Sets up all the fields with their necessary values
     *
     * @param q    Probability of a vehicle appearing at the start end of the road for each time-step
     * @param p    Probability of any one vehicle randomly slowing down for reasons unknown
     */
    public TrafficLightModel(double q, double p, double mIn, double mOut, double gIn, double gOut, double aIn, double aOut, int lightTimes) {
        super(q, p, mIn, mOut, gIn, gOut, aIn, aOut);
        MerrimanQueue = new ArrayBlockingQueue<>(QUEUE_CAP);
        GeorgeBlakeQueue = new ArrayBlockingQueue<>(QUEUE_CAP);
        AlexanderQueue = new ArrayBlockingQueue<>(QUEUE_CAP);
        MerrimanLight = new TrafficLight(lightTimes);
        GeorgeBlakeLight = new TrafficLight(lightTimes);
        AlexanderLight = new TrafficLight(lightTimes);
    }

    @Override
    public void step(){
        //add vehicles at start point and at junctions
        if(Math.random() < q && road[0] == null){
            road[0] = new Vehicle();
        }
        if(Math.random() < MerrimanIn){
            MerrimanQueue.add(new Vehicle());
        }
        if(Math.random() < GeorgeBlakeIn){
            GeorgeBlakeQueue.add(new Vehicle());
        }
        if(Math.random() < MerrimanIn){
            AlexanderQueue.add(new Vehicle());
        }
        //TODO pull vehicles in from queue when lights are red
        for(int i = 0; i < l; i ++) {
            if (road[i] == null) {
                continue;
            }
            //remove vehicle at junctions
            if((Objects.equals(road[i].destination, "Merriman") && i == MerrimanPos - 1) ||
                    (Objects.equals(road[i].destination, "George Blake") && i == GeorgeBlakePos - 1) ||
                    (Objects.equals(road[i].destination, "Alexander") && i == AlexanderPos - 1)){
                vehiclesPassed ++;
                road[i] = null;
                continue;
            }
            //acceleration
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
            //System.out.println(road[i].v);
            averageSpeed += road[i].v;
            averageSpeedReadings ++;
            //motion
            //TODO stop motion when traffic lights are not green
            if(road[i].v != 0) {
                try {
                    road[i + road[i].v] = road[i];
                } catch (ArrayIndexOutOfBoundsException e) {
                    vehiclesPassed ++;
                }
                road[i] = null;
            }
        }
        timeStep ++;
    }

    public static void main(String[] args){
        //TODO
    }
}
