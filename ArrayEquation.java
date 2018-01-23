package Equation;


/**
 * Stores an array of values, returning the value found at the nearest integer to t
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ArrayEquation extends Equation{
    Equation[] values;
    
    public ArrayEquation(Equation[] v){
        values = v;
    }
    
    public double solve(double t){
        int index = Math.round((float)t);
        return (index > values.length)? 0 : values[index].solve(t);
    }
    
    public Equation solve(SolutionSet s){
        Equation[] newValues = new Equation[values.length];
        for(int i = 0; i < values.length; i++){
            newValues[i] = values[i].solve(s).simplify();
        }
        return new ArrayEquation(newValues);
    }
    
    public String toString(){
        String newString = "array{";
        for(Equation eq : values){
            newString += eq.toString() + ", ";
        }
        
        return newString + "}";
    }
    
    public Equation getDerivative(){
        Equation[] derivatives = new Equation[values.length];
        for(int i = 0; i < values.length; i++){
            derivatives[i] = values[i].getDerivative();
        }
        
        return new ArrayEquation(derivatives);
    }
    
    public Equation getPartialDerivative(String respect){
        Equation[] derivatives = new Equation[values.length];
        for(int i = 0; i < values.length; i++){
            derivatives[i] = values[i].getPartialDerivative(respect);
        }
        
        return new ArrayEquation(derivatives);
    }
    
    public Equation simplify(){
        //this should remove zeros from the end of the array, but it's fine
        //this should simplify the equations it contains
        System.out.println("ArrayEquation.simplify() is an incomplete method!");
        return this;
    }
    
    public int getOperationLevel(){
        return Equation.VAR_NUM_LEVEL;
    }
}