package Equation;


/**
 * Write a description of class CosEquation here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CosEquation extends Equation{
    protected Equation e1;

    public CosEquation(Equation e1){
        this.e1 = e1;
    }

    public double solve(double t){
        return Math.cos(e1.solve(t));
    }
    
    public Equation solve(SolutionSet s){
        return new CosEquation(e1.solve(s)).simplify();
    }
    
    public String toString(){
        return "cos(" + e1.toString() + ")";
    }

    public Equation getDerivative(){
        return 
        new MultiplicationEquation(
            new ConstantEquation(-1), 
            new MultiplicationEquation(
                new SinEquation(e1), 
                e1.getDerivative()
            )
         );
    }
    
    public Equation getPartialDerivative(String respect){
        return 
        new MultiplicationEquation(
            new ConstantEquation(-1), 
            new MultiplicationEquation(
                new SinEquation(e1), 
                e1.getPartialDerivative(respect)
            )
        );
    }

    public Equation simplify(){
        if(e1.isConstant){
            return new ConstantEquation(this.solve(0));
        }
        
        return new CosEquation(e1.simplify());
    }
    
    public int getOperationLevel(){
        return Equation.FUNCTION_LEVEL;
    }
}
