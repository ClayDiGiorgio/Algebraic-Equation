package Equation;

/**
 * Write a description of class MultiplicationEquation here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MultiplicationEquation extends Equation{
    protected Equation e1;
    protected Equation e2;

    public MultiplicationEquation(Equation e1, Equation e2){
        this.e1 = e1;
        this.e2 = e2;
    }

    public double solve(double t){
        return e1.solve(t) * e2.solve(t);
    }
    
    public Equation solve(SolutionSet s){
        return new MultiplicationEquation(e1.solve(s), e2.solve(s)).simplify();
    }

    public String toString(){
        String e1String = e1.toString();
        e1String = (e1.getOperationLevel() < getOperationLevel())? ("("+e1String+")") : (e1String);
        String e2String = e2.toString();
        e2String = (e2.getOperationLevel() < getOperationLevel())? ("("+e2String+")") : (e2String);
        return e1String + " * " + e2String;
    }

    public Equation getDerivative(){
        return 
        new AdditionEquation(
            new MultiplicationEquation(
                e1.getDerivative(),
                e2
            ), 
            new MultiplicationEquation(
                e1,
                e2.getDerivative()
            )
        );
    }
    
    public Equation getPartialDerivative(String respect){
        return 
        new AdditionEquation(
            new MultiplicationEquation(
                e1.getPartialDerivative(respect),
                e2
            ), 
            new MultiplicationEquation(
                e1,
                e2.getPartialDerivative(respect)
            )
        );
    }

    public Equation simplify(){
        if (e1.isConstant && e2.isConstant) {
            return new ConstantEquation(this.solve(0));
        } else if (e1.isConstant) {
            if (e1.solve(0) == 0) {
                return new ConstantEquation(0);
            } else if (e1.solve(0) == 1) {
                return e2.simplify();
            }
        } else if (e2.isConstant) {
            if (e2.solve(0) == 0) {
                return new ConstantEquation(0);
            } else if (e2.solve(0) == 1) {
                return e1.simplify();
            }
        } else if (e1.isVariable && e2.isVariable) {
            return new PowerEquation(new VariableEquation(), new ConstantEquation(2));
        }
        
        return new MultiplicationEquation(e1.simplify(), e2.simplify());
    }
    
    public int getOperationLevel(){
        return Equation.MUL_DIV_LEVEL;
    }
}

