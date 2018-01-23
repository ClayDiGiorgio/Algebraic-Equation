package Equation;


/**
 * Stores an array of variables and values for those variables to solve an equation with
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SolutionSet{
    private String[] variables;
    private double[] values;
    
    public SolutionSet(String[] vars, double[] vals){
        int varsLength = vars.length;
        int valsLength = vals.length;
        
        variables = vars;
        values = new double[varsLength];
        
        if(varsLength > valsLength){ //if vars is longer than vals, fill vals with zeros
            for(int i = 0; i < valsLength; i++){
                values[i] = vals[i];
            }
            for(int i = valsLength; i < varsLength; i++){
                values[i] = 0;
            }
        } else if (varsLength < valsLength){ //if vars is longer than vals, truncate vals
            for(int i = 0; i < varsLength; i++){
                values[i] = vals[i];
            }
        } else { // if they're the same size, change nothing
            values = vals;
        }
    }
    
    public String getValue(String varName){
        for(int i = 0; i < variables.length; i++){
            if(variables[i] == varName){
                return values[i] + "";
            }
        }
        
        return "undef";
    }
    
    public String[] getVariables(){
        return variables;
    }
}
