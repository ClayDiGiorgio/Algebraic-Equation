package Equation;


/**
 * Write a description of class ConstantEquation here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ConstantEquation extends Equation{
    private double value;
    
    public ConstantEquation(double n){
        value = n;
        isConstant = true;
    }
    
    public double solve(double t){
        return value;
    }
    
    public Equation solve(SolutionSet s){
        return new ConstantEquation(value);
    }
    
    public String toString(){
        return "" + value;
    }
    
    public Equation getDerivative(){
        return new ConstantEquation(0);
    }
    
    public Equation getPartialDerivative(String respect){
        return getDerivative();
    }
    
    public Equation simplify(){
        return this;
    }
    
    public int getOperationLevel(){
        return Equation.VAR_NUM_LEVEL;
    }
}