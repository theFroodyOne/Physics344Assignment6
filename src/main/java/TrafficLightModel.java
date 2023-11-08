import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;

public class TrafficLightModel extends JunctionModel{
    private static final int QUEUE_CAP = 10000;
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
    double Mq;
    double Gq;
    double Aq;

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
        Mq = 0;
        Gq = 0;
        Aq = 0;
    }

    /**
     * Include keeping track of queue lengths and incrementing traffic lights' internal time
     */
    @Override
    public void step(){
        addVehicles();
        removeAtJunctions();
        acceleration();
        turnDecision();
        slowDown();
        randomize();
        move();
        Mq += MerrimanQueue.size();
        Gq += GeorgeBlakeQueue.size();
        Aq += AlexanderQueue.size();
        timeStep ++;
        MerrimanLight.incrementTime();
        GeorgeBlakeLight.incrementTime();
        AlexanderLight.incrementTime();
    }

    /**
     * Add vehicles from side streets to queues, and let them in from those queues only when the
     * traffic lights let them
     */
    @Override
    protected void addVehicles(){
        if(Math.random() < q && road[0] == null){
            road[0] = new Vehicle();
        }
        if(Math.random() < MerrimanIn){
            MerrimanQueue.add(new Vehicle());
        }
        if(Math.random() < GeorgeBlakeIn){
            GeorgeBlakeQueue.add(new Vehicle());
        }
        if(Math.random() < AlexanderIn){
            AlexanderQueue.add(new Vehicle());
        }
        if(!MerrimanQueue.isEmpty() && !MerrimanLight.check()){
            road[MerrimanPos] = MerrimanQueue.poll();
        }
        if(!GeorgeBlakeQueue.isEmpty() && !GeorgeBlakeLight.check()){
            road[GeorgeBlakePos] = GeorgeBlakeQueue.poll();
        }
        if(!AlexanderQueue.isEmpty() && !AlexanderLight.check()){
            road[AlexanderPos] = AlexanderQueue.poll();
        }
    }

    /**
     * Include slowing down/stopping for traffic lights
     */
    @Override
    protected void slowDown() {
        for(int i = 0; i < l; i ++) {
            if (road[i] == null) {
                continue;
            }
            //slowing down for traffic lights
            if (i < MerrimanPos && i + road[i].v >= MerrimanPos && !MerrimanLight.check()) {
                road[i].v = MerrimanPos - i - 1;
            }
            if (i < GeorgeBlakePos && i + road[i].v >= GeorgeBlakePos && !GeorgeBlakeLight.check()) {
                road[i].v = GeorgeBlakePos - i - 1;
            }
            if (i < AlexanderPos && i + road[i].v >= AlexanderPos && !AlexanderLight.check()) {
                road[i].v = AlexanderPos - i - 1;
            }
            //slowing down for other vehicles
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
     * Main method for generating and storing the data
     * @param args Superfluous
     */
    public static void main(String[] args){
        int runs = 1000;
        int steps = 1440;
        try {
            FileWriter fw = new FileWriter("/home/zander/IdeaProjects/Physics344Assignment6/data/phase3/data.csv");
            fw.write("p, t, <v>, Mq, Gq, Aq\n");
            for(int t = 1; t < 120; t ++){
                for (double p = 0.05; p < 0.65; p += 0.05) {
                    double Mq = 0, Gq = 0, Aq = 0;
                    double avg = 0;
                    fw.write(p + ",");
                    fw.write(t + ",");
                    for (int i = 0; i < runs; i++) {
                        TrafficLightModel tlm = new TrafficLightModel(0.7736, p, 0.7132, 0.3139, 0.3938, 0.4007, 0.4725, 0.1640, t);
                        avg += tlm.run(steps);
                        Mq += tlm.Mq;
                        Gq += tlm.Gq;
                        Aq += tlm.Aq;
                    }
                    fw.write(avg / runs + ",");
                    fw.write(Mq/(steps*runs) + ",");
                    fw.write(Gq/(steps*runs) + ",");
                    fw.write(Aq/(steps*runs) + "");
                    fw.write("\n");
                }
            }
            fw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
