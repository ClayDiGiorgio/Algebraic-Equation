package Equation;


/**
 * Write a description of class LogEquation here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
class LogEquation extends DivisionEquation{
    public LogEquation(Equation e1, Equation e2){
        super(new NaturalLogEquation(e1), new NaturalLogEquation(e2));
    }
}