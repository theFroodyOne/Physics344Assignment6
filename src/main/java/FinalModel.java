import java.io.FileWriter;
import java.io.IOException;

//TODO majorly a work in progress, might even throw everything out entirely

public class FinalModel {
    public static void main(String[] args){
        double d = 0.2;
        int runs = 1000;
        int steps = 1440;
        try {
            FileWriter fw = new FileWriter("/home/zander/IdeaProjects/Physics344Assignment6/data/phase4/data.csv");
            fw.write("p, t, <v>, Mq, Gq, Aq\n");
            for(int t = 1; t < 120; t ++) {
                for (double p = 0.0; p < 0.6; p += 0.1) {
                    double Mq = 0, Gq = 0, Aq = 0;
                    double avg = 0;
                    fw.write(p + ",");
                    fw.write(t + ",");
                    for (int i = 0; i < runs; i++) {
                        TrafficLightModel R44 = new TrafficLightModel(0.7736, p, 0.7132, 0.3139, 0.3938, 0.4007, 0.7132, 0.3139, t);
                        //TODO
                        SimpleModel DuToitStreet = new SimpleModel(0, p, 6, 80);
                        avg += R44.run(steps);
                        Mq += R44.Mq;
                        Gq += R44.Gq;
                        Aq += R44.Aq;
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
