public class addN implements IntUnaryFunction {

    public addN(int n){
        constant = n;
    }

    public int apply(int x){
        return x + constant;

    }
    private int constant;
}
