package Equation;

/**
 * Write a description of class NaturalLogEquation here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class NaturalLogEquation extends Equation{
    protected Equation e1;

    public NaturalLogEquation(Equation e1){
        this.e1 = e1;
    }

    public double solve(double t){
        return Math.log(e1.solve(t));
    }
    
    public Equation solve(SolutionSet s){
        return new NaturalLogEquation(e1.solve(s)).simplify();
    }

    public String toString(){
        return "ln(" + e1.toString() + ")";
    }

    public Equation getDerivative(){
        return new DivisionEquation(e1.getDerivative(), e1);
    }
    
    public Equation getPartialDerivative(String respect){
        return new DivisionEquation(e1.getPartialDerivative(respect), e1);
    }

    public Equation simplify(){
        if(e1.isConstant){
            return new ConstantEquation(this.solve(0));
        }
        
        return new NaturalLogEquation(e1.simplify());
    }
    
    public int getOperationLevel(){
        return Equation.FUNCTION_LEVEL;
    }
}
