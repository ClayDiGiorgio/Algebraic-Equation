package Equation;


/**
 * Write a description of class VariableEquation here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class VariableEquation extends Equation{
    
    private String variableName = "t";
    
    public VariableEquation(){
        isVariable = true;
    }
    
    public VariableEquation(String name){
        isVariable = true;
        variableName = name;
    }
    
    public double solve(double t){
        return t;
    }
    
    public Equation solve(SolutionSet s){
        try{
            double value = Double.parseDouble(s.getValue(variableName));
            return new ConstantEquation(value);
        } catch(Exception e){
            return new VariableEquation(variableName);
        }
    }
    
    public String toString(){
        return variableName;
    }
    
    public Equation getDerivative(){
        return new ConstantEquation(1);
    }
    
    public Equation getPartialDerivative(String respect){
        if(respect == variableName){ //we're taking a derrivative with respect to this variable, like normal   
            return new ConstantEquation(1);
        } else { //treat it like a constant
            return new ConstantEquation(0);
        }
    }
    
    public Equation simplify(){
        return this;
    }
    
    public int getOperationLevel(){
        return Equation.VAR_NUM_LEVEL;
    }
}
