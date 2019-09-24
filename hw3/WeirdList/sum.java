public class sum implements IntUnaryFunction {

    public sum(){total = 0;}


    public int apply(int x){
        total += x;
        return x;
    }

    public int total(){
        return total;
    }
    static private int total;
}
