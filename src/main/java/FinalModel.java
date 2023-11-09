import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class FinalModel extends TrafficLightModel{
    private static final double p = 0.3;
    private static final int lightTimes = 14;
    private final DuToitModel DuToit;
    private final double d;
    protected int TORDetour;
    protected int detourPassed;
    protected int vp;

    /**
     * Constructor. Sets up all the fields with their necessary values
     *
     * @param q          Probability of a vehicle appearing at the start end of the road for each time-step
     */
    public FinalModel(double q, double mIn, double mOut, double gIn, double gOut, double aIn, double aOut, double d) {
        super(q, p, mIn, mOut, gIn, gOut, aIn, aOut, lightTimes);
        DuToit = new DuToitModel(p, v);
        this.d = d;
        TORDetour = 0;
        detourPassed = 0;
        vp = 0;
    }

    /**
     * Track vehicles originating from Merriman Avenue, allow vehicles in the Merriman queue to
     * turn down Du Toit street, add vehicles from the end of Du Toit street to the Alexander
     * street queue
     */
    @Override
    protected void addVehicles(){
        if(Math.random() < q && road[0] == null){
            road[0] = new Vehicle();
        }
        if(Math.random() < MerrimanIn){
            Vehicle v = new Vehicle();
            v.origin = "Merriman";
            MerrimanQueue.add(v);
        }
        if(Math.random() < GeorgeBlakeIn){
            GeorgeBlakeQueue.add(new Vehicle());
        }
        if(Math.random() < AlexanderIn){
            AlexanderQueue.add(new Vehicle());
        }
        if(Math.random() < d && !MerrimanQueue.isEmpty() && DuToit.road[0] == null){
            Vehicle v = MerrimanQueue.poll();
            v.route = "DuToit";
            DuToit.road[0] = v;
        }
        if(!MerrimanQueue.isEmpty() && !MerrimanLight.check()){
            road[MerrimanPos] = MerrimanQueue.poll();
        }
        if(!GeorgeBlakeQueue.isEmpty() && !GeorgeBlakeLight.check()){
            road[GeorgeBlakePos] = GeorgeBlakeQueue.poll();
        }
        if(DuToit.road[DuToitModel.l-1] != null){
            //System.out.println("TOR when exiting Du Toit street: " + DuToit.road[DuToitModel.l-1].timeOnRoad);
            AlexanderQueue.add(DuToit.road[DuToitModel.l-1]);
        }
        if(!AlexanderQueue.isEmpty() && !AlexanderLight.check()){
            road[AlexanderPos] = AlexanderQueue.poll();
        }
        DuToit.step();
    }

    /**
     * Track TOR only for vehicles originating from Merriman Avenue
     */
    @Override
    protected void move(){
        for(int i = l -1; i >= 0; i --){
            if (road[i] == null) {
                continue;
            }
            averageSpeed += road[i].v;
            averageSpeedReadings ++;
            road[i].timeOnRoad++;
            if(road[i].v != 0) {
                try {
                    road[i + road[i].v] = road[i];
                } catch (ArrayIndexOutOfBoundsException e) {
                    if(Objects.equals(road[i].origin, "Merriman")) {
                        if(!Objects.equals(road[i].route, "DuToit")) {
                            System.out.println("TOR non-detour:" + road[i].timeOnRoad);
                            averageTimeOnRoad += road[i].timeOnRoad;
                            System.out.println(vp);
                            vp++;
                        }else{
                            System.out.println(road[i].route);
                            System.out.println(road[i].timeOnRoad);
                            TORDetour += road[i].timeOnRoad;
                            detourPassed++;
                        }
                    }
                }
                road[i] = null;
            }
        }
    }

    public static void main(String[] args){
        int runs = 1;
        int steps = 1440;
        try {
            FileWriter fw = new FileWriter("/home/zander/IdeaProjects/Physics344Assignment6/data/phase4/data.csv");
            fw.write("d, <v>, Mq, Gq, Aq, R44TOR, detourTOR\n");
            for(double d = 0.5; d < 1; d += 0.5) {
                double Mq = 0, Gq = 0, Aq = 0;
                double avg = 0;
                double R44TOR = 0, DuToitTOR = 0;
                for (int i = 0; i < runs; i++) {
                    FinalModel fm = new FinalModel(0.7736, 0.7132, 0.3139, 0.3938, 0.4007, 0.4725, 0.1640, d);
                    avg += fm.run(steps);
                    Mq += fm.Mq;
                    Gq += fm.Gq;
                    Aq += fm.Aq;
                    System.out.println(fm.averageTimeOnRoad);
                    System.out.println(fm.vp);
                    R44TOR += (double)fm.averageTimeOnRoad/fm.vp;
                    System.out.println(fm.TORDetour);
                    System.out.println(fm.detourPassed);
                    DuToitTOR += (double)fm.TORDetour/fm.detourPassed;
                }
                fw.write(d + ",");
                fw.write(avg / runs + ",");
                fw.write(Mq/(steps*runs) + ",");
                fw.write(Gq/(steps*runs) + ",");
                fw.write(Aq/(steps*runs) + ",");
                fw.write(R44TOR / runs + ",");
                fw.write(DuToitTOR / runs + "");
                fw.write("\n");
            }
            fw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
