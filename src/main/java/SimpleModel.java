public class SimpleModel {
    double q;
    double p;
    int v;
    int l;
    boolean[] road;

    public SimpleModel(double q, double p, int v, int l){
        if(q <= 1 && q >= 0 && p <= 1 && p >= 0) {
            this.q = q;
            this.p = p;
            this.v = v;
            this.l = l;
            road = new boolean[l];
        }else{
            throw new NumberFormatException("Probabilities p and q must be between 0 and 1");
        }
    }

    public void step(){
        //todo move the simulation one time-step forward
    }
}
