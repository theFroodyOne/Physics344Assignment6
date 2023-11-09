import java.io.FileWriter;
import java.io.IOException;

public class FinalModel extends TrafficLightModel{
    private final DuToitModel DuToit;
    private final double d;
    /**
     * Constructor. Sets up all the fields with their necessary values
     *
     * @param q          Probability of a vehicle appearing at the start end of the road for each time-step
     * @param p          Probability of any one vehicle randomly slowing down for reasons unknown
     */
    public FinalModel(double q, double p, double mIn, double mOut, double gIn, double gOut, double aIn, double aOut, int lightTimes, double d) {
        super(q, p, mIn, mOut, gIn, gOut, aIn, aOut, lightTimes);
        DuToit = new DuToitModel(0, p, v);
        this.d = d;
    }

    @Override
    public void step(){
        //TODO remove from Merriman queue and plonk in DuToit
        //TODO remove from DuToit and plonk in Alexander queue
    }

    public static void main(String[] args){
        double d = 0.2;
        int runs = 1000;
        int steps = 1440;
        try {
            FileWriter fw = new FileWriter("/home/zander/IdeaProjects/Physics344Assignment6/data/phase4/data.csv");
            fw.write("p, t, <v>, Mq, Gq, Aq\n");
            //TODO extract TOR from Merriman for every run manually
            fw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
