package Equation;


/**
 * Write a description of class SigmaEquation here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SigmaEquation extends Equation{
    private String indexVariable = "";
    private int start;
    private int end;
    private Equation insides;
    
    public SigmaEquation(String index, int from, int to, Equation in){
        indexVariable = index;
        start = from;
        end = to;
        insides = in;
    }
    
    public double solve(double t){
        float sum = 0;
        for(int i = start; i <= end; i++){
            sum += insides.solve(t);
        }
        return sum;
    }
    
    public Equation solve(SolutionSet s){
        return new SigmaEquation(indexVariable, start, end, insides.solve(s)).simplify();
    }
    
    public String toString(){
        return "[Σ" + "(" + indexVariable + ")" + "(" + start + ", " + end + ")" + "[" + insides.toString() + "]]";
    }
    
    public Equation getDerivative(){
        return new SigmaEquation(indexVariable, start, end, insides.getDerivative());
    }
    
    public Equation getPartialDerivative(String respect){
        return new SigmaEquation(indexVariable, start, end, insides.getPartialDerivative(respect));
    }
    
    public Equation simplify(){
        return new SigmaEquation(indexVariable, start, end, insides.simplify());
    }
    
    public int getOperationLevel(){
        return Equation.FUNCTION_LEVEL;
    }
}