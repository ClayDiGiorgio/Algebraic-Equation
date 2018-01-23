package Equation;


/**
 * Write a description of class ParametricFunction here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ParametricFunction{
    protected Equation x;
    protected Equation y;
    
    public ParametricFunction(Equation e1, Equation e2){
        x = e1;
        y = e2;
    }
    
    public double getX(double t){
        return x.solve(t);
    }
    public double getY(double t){
        return y.solve(t);
    }
    
    public String getXfunctionAsString(){
        return x.toString();
    }
    public String getYfunctionAsString(){
        return y.toString();
    }
    
    public static void test(){/*
        Equation eq = 
            new AdditionEquation(
                new MultiplicationEquation(
                    new ConstantEquation(4), new VariableEquation()
                ),
                new PowerEquation(
                    new MultiplicationEquation(
                        new ConstantEquation(5), new VariableEquation()
                    ),
                    new ConstantEquation(2)
                )
            );
        Equation eqPrime = eq.getDerivative();
            
        System.out.println(eq.toString());
        System.out.println(eqPrime.toString());
        System.out.println(eqPrime.simplify().toString());
        
        System.out.println(eq.solve(2));
        System.out.println(eq.solve(22));
        System.out.println(eq.solve(0.2) + "\n\n");
        
        
        Equation eq2 = //(5x-17)/((3x^2) + 6)
            new DivisionEquation(
                new SubtractionEquation(
                    new MultiplicationEquation(
                        new ConstantEquation(5),
                        new VariableEquation()
                    ),
                    new ConstantEquation(17)
                ),
                new AdditionEquation(
                    new MultiplicationEquation(
                        new ConstantEquation(3),
                        new PowerEquation(
                            new VariableEquation(),
                            new ConstantEquation(2)
                        )
                    ),
                    new ConstantEquation(6)
                )
            );
            
        Equation eq2Prime = eq.getDerivative();
        
        System.out.println(eq2.toString());
        System.out.println(eq2Prime.toString());
        System.out.println(eq2Prime.simplify().toString()); */
        
        Equation eq3 = //(5x-17)/((3y^2) + 6)
            new DivisionEquation(
                new SubtractionEquation(
                    new MultiplicationEquation(
                        new ConstantEquation(5),
                        new VariableEquation("x")
                    ),
                    new ConstantEquation(17)
                ),
                new AdditionEquation(
                    new MultiplicationEquation(
                        new ConstantEquation(3),
                        new PowerEquation(
                            new VariableEquation("y"),
                            new ConstantEquation(2)
                        )
                    ),
                    new ConstantEquation(6)
                )
            );
        System.out.println(eq3.toString());
        
        Equation eq3PrimeY = eq3.getPartialDerivative("y").simplify();
        System.out.println(eq3PrimeY.toString());
        Equation eq3PrimeX = eq3.getPartialDerivative("x").simplify();
        System.out.println(eq3PrimeX.toString());
        
        String[] s1Vars = {"x"};      double[] s1Vals = {1};
        String[] s2Vars = {"x", "y"}; double[] s2Vals = {1, 2};
        SolutionSet s1 = new SolutionSet(s1Vars, s1Vals); //(5(1)-17)/((3y^2) + 6)
        SolutionSet s2 = new SolutionSet(s2Vars, s2Vals); //(5(1)-17)/(((3(2))^2) + 6)
        System.out.println(eq3.solve(s1));
        System.out.println(eq3.solve(s2));
    }
    
    public static Equation simplifyFully(Equation e){
        String lastEString;
        do{
            lastEString = e.toString();
            e = e.simplify();
        } while(e.toString() != lastEString); //while the string changes after simplifying
            
        
        return e;
    }
}
