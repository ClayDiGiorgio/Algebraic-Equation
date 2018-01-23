package Equation;


/**
 * Abstract class Equation - write a description of the class here
 * 
 * @author (your name here)
 * @version (version number or date here)
 */
public abstract class Equation{
    boolean isConstant = false;
    boolean isVariable = false;
    public static final int     ADD_SUB_LEVEL = 1;
    public static final int     MUL_DIV_LEVEL = 2;
    public static final int         POW_LEVEL = 3;
    public static final int    FUNCTION_LEVEL = 4;
    public static final int PARENTHESIS_LEVEL = 5;
    public static final int     VAR_NUM_LEVEL = 6;
    
    public static void test(){
        Equation eq = new AdditionEquation(new VariableEquation("t"), new ConstantEquation(2));
        System.out.println(eq.toString());
    }
    
    abstract public double   solve(double t);
    abstract public Equation solve(SolutionSet s);
    abstract public String   toString();
    abstract public Equation getDerivative();
    abstract public Equation getPartialDerivative(String respect);
    abstract public Equation simplify();
    abstract public int      getOperationLevel();
}
