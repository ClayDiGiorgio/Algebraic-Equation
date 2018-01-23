package Equation;

/**
 * Write a description of class PowerEquation here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PowerEquation extends Equation{
    protected Equation e1;
    protected Equation e2;

    public PowerEquation(Equation e1, Equation e2){
        this.e1 = e1;
        this.e2 = e2;
    }

    public double solve(double t){
        return Math.pow(e1.solve(t), e2.solve(t));
    }
    
    public Equation solve(SolutionSet s){
        return new PowerEquation(e1.solve(s), e2.solve(s)).simplify();
    }

    public String toString(){
        String e1String = e1.toString();
        e1String = (e1.getOperationLevel() < getOperationLevel())? ("("+e1String+")") : (e1String);
        String e2String = e2.toString();
        e2String = (e2.getOperationLevel() < getOperationLevel())? ("("+e2String+")") : (e2String);
        return e1String + " ^ " + e2String;
    }

    public Equation getDerivative(){
        if(e1.isConstant && !e2.isConstant){ //a^x
            return 
            new MultiplicationEquation(
                new NaturalLogEquation(
                    e1
                ),
                new MultiplicationEquation(
                    new PowerEquation(
                        e1,
                        e2
                    ),
                    e2.getDerivative()
                )
            );
        } else if (!e1.isConstant && e2.isConstant){ //x^a
            if(e2.solve(0) != 1){
                return 
                new MultiplicationEquation(
                    new MultiplicationEquation(
                        new ConstantEquation(e2.solve(0)),
                        new PowerEquation(
                            e1,
                            new ConstantEquation(e2.solve(0) - 1)
                        )
                    ),
                    e1.getDerivative()
                );
            } else {
                return new ConstantEquation(1);
            }
        } else {
            return null;
        }
    }
    
    public Equation getPartialDerivative(String respect){
        if(e1.isConstant && !e2.isConstant){ //a^x
            return 
            new MultiplicationEquation(
                new NaturalLogEquation(
                    e1
                ),
                new MultiplicationEquation(
                    new PowerEquation(
                        e1,
                        e2
                    ),
                    e2.getPartialDerivative(respect)
                )
            );
        } else if (!e1.isConstant && e2.isConstant){ //x^a
            if(e2.solve(0) != 1){
                return 
                new MultiplicationEquation(
                    new MultiplicationEquation(
                        new ConstantEquation(e2.solve(0)),
                        new PowerEquation(
                            e1,
                            new ConstantEquation(e2.solve(0) - 1)
                        )
                    ),
                    e1.getPartialDerivative(respect)
                );
            } else {
                return new ConstantEquation(1);
            }
        } else {
            return null;
        }
    }
    
    public Equation simplify(){
        if (e1.isConstant && e2.isConstant) {
            return new ConstantEquation(this.solve(0));
        } else if (e2.isConstant) {
            if (e2.solve(0) == 0) {
                return new ConstantEquation(1);
            } else if (e2.solve(0) == 1) {
                return e1.simplify();
            }
        }
        
        return new PowerEquation(e1.simplify(), e2.simplify());
    }
    
    public int getOperationLevel(){
        return Equation.POW_LEVEL;
    }
}
