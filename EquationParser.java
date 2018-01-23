package Equation;
// 
import java.util.ArrayList;
// 
// /**
//  * Write a description of class EquationParser here.
//  * 
//  * @author (your name) 
//  * @version (a version number or a date)
//  */
public final class EquationParser{
    //test string "(x+7)*(x*3)+(5*(x+72))"
    //test string 2 "cos(x) * sin(4+x) / (ln(3) + x)"
    //test string 3 "cos(3)^2 + 3*x^2 + ln(x^2)"

    public static Equation parse(String rawEquationString){
        //remove all spaces from rawEquationString
        rawEquationString.replaceAll("\\s+","");
        
        ArrayList<String> $functions = findParentheses(rawEquationString);
        ArrayList<String> terms      = replaceTerms($functions);
        $functions = splitIntoSingleOperationFunctions($functions);
        
        System.out.println("CONTENTS OF $FUNCTIONS AND TERMS (debug)");
        for(String s : $functions){
            System.out.println(s);
        }
        for(String s : terms){
            System.out.println("\t" + s);
        }
        System.out.println("END CONTENTS OF $FUNCTIONS AND TERMS (debug)");
        
        ArrayList<Equation> termsParsed = new ArrayList<Equation>();
        for(int i = 0; i < terms.size(); i++){
            String term = terms.get(i);
            if(Character.isLetter(term.charAt(0))){
                termsParsed.add(new VariableEquation(term));
            } else {
                termsParsed.add(new ConstantEquation(Double.parseDouble(term)));
            }
        }
        
        String parentEq = $functions.get($functions.size()-1);
        if($functions.size() >= 2 && parentEq.equals("")){
            parentEq = $functions.get($functions.size()-2);
        }
        
        Equation finalEq = recursiveParseSingleOperationFunctions(parentEq, $functions, termsParsed);
        System.out.println("==" + finalEq.toString() + "==" + "\n" + rawEquationString + "\nValue at all vars = 0: " + finalEq.solve(0));
        return finalEq;
    }
    
    private static Equation recursiveParseSingleOperationFunctions(String toParse, ArrayList<String> $functions, ArrayList<Equation> terms){
        int operationIndex = -1;
        for(int i = 0; i < toParse.length(); i++){
            if(isOperationChar(toParse.charAt(i))){
                operationIndex = i;
                break;
            }
        }
        
        if(operationIndex != -1){
            Equation eq1 = new ConstantEquation(-Integer.MAX_VALUE);
            for(int i = 0; i < operationIndex; i++){
                if(toParse.charAt(i) == '#'){
                    int octothorpeIndex = i;
                    i++;
                    while(i < toParse.length() && Character.isDigit(toParse.charAt(i))){
                        i++;
                    }
                    
                    eq1 = terms.get(Integer.parseInt(toParse.substring(octothorpeIndex+1, i)));
                    break;
                } else if (toParse.charAt(i) == '$'){
                    int dollarSignIndex = i;
                    i++;
                    while(i < toParse.length() && Character.isDigit(toParse.charAt(i))){
                        i++;
                    }
                    
                    System.out.println("$ " + dollarSignIndex + "  " + i + "   " + toParse);
                    String eqToParse = $functions.get(Integer.parseInt(toParse.substring(dollarSignIndex+1, i)));
                    eq1 = recursiveParseSingleOperationFunctions(eqToParse, $functions, terms);
                    break;
                }
            }
            
            Equation eq2 = new ConstantEquation(-Integer.MAX_VALUE);
            for(int i = operationIndex; i < toParse.length(); i++){
                if(toParse.charAt(i) == '#'){
                    int octothorpeIndex = i;
                    i++;
                    while(i<toParse.length() && Character.isDigit(toParse.charAt(i))){
                        i++;
                    }
                    
                    eq2 = terms.get(Integer.parseInt(toParse.substring(octothorpeIndex+1, i)));
                    break;
                } else if (toParse.charAt(i) == '$'){
                    int dollarSignIndex = i;
                    i++;
                    while(i < toParse.length() && Character.isDigit(toParse.charAt(i))){
                        i++;
                    }
                    
                    String eqToParse = $functions.get(Integer.parseInt(toParse.substring(dollarSignIndex+1, i)));
                    eq2 = recursiveParseSingleOperationFunctions(eqToParse, $functions, terms);
                    break;
                }
            }
            
            switch(toParse.charAt(operationIndex)){
                case '+': return new AdditionEquation      (eq1, eq2);
                case '-': return new SubtractionEquation   (eq1, eq2);
                case '*': return new MultiplicationEquation(eq1, eq2);
                case '/': return new DivisionEquation      (eq1, eq2);
                case '^': return new PowerEquation         (eq1, eq2);
            }
        } else if (operationIndex == -1 && toParse.length() > 2) {
            //the operation is something like sin, cos, ln, floor, etc.
            Equation eq1 = new ConstantEquation(-Integer.MAX_VALUE);
            String operation = "";
            int endOperation = -1;
            for(int i = 0; i < toParse.length(); i++){
                if(toParse.charAt(i) == '#'){
                    operation = toParse.substring(0,i);
                    endOperation = i;
                    int octothorpeIndex = i;
                    i++;
                    while(i<toParse.length() && Character.isDigit(toParse.charAt(i))){
                        i++;
                    }
                    
                    eq1 = terms.get(Integer.parseInt(toParse.substring(octothorpeIndex+1, i)));
                    break;
                } else if (toParse.charAt(i) == '$'){
                    operation = toParse.substring(0,i); 
                    endOperation = i;
                    int dollarSignIndex = i;
                    i++;
                    while(i<toParse.length() && Character.isDigit(toParse.charAt(i))){
                        i++;
                    }
                    
                    String eqToParse = $functions.get(Integer.parseInt(toParse.substring(dollarSignIndex+1, i)));
                    eq1 = recursiveParseSingleOperationFunctions(eqToParse, $functions, terms);
                    break;
                }
            }
            
            System.out.println(toParse + "     " + operationIndex + "      " + endOperation);
            
            switch(operation){
                case "sin": return new SinEquation(eq1);
                case "cos": return new CosEquation(eq1);
                case "ln": return new NaturalLogEquation(eq1);
                default: System.out.println("op: " + operation);
            }
        } else {
            if (toParse.charAt(0) == '#'){
                return terms.get(Integer.parseInt(toParse.substring(1)));
            } else if (toParse.charAt(0) == '$'){
                String eqToParse = $functions.get(Integer.parseInt(toParse.substring(1)));
                return recursiveParseSingleOperationFunctions(eqToParse, $functions, terms);
            }
        }
        
        System.out.println("EquationParser: Unrecognized something: " + toParse);
        return null;
    }
    
    private static ArrayList<String> splitIntoSingleOperationFunctions(ArrayList<String> $functions){
        int indexOfParentEquation = $functions.size()-1;
        String finalizedParentEquation = "";
        
        for(int i = 0; i < $functions.size(); i++){
            String function = $functions.get(i);
            System.out.println("Run " + i + ": " + function);
            while(getOperationCount(function) > 1){
                int indexOfHighestOrderOperation = -1;
                int highestOrderOperation = 0;
                for(int j = 0; j < function.length(); j++){
                    if ((function.charAt(j) == '+' || function.charAt(j) == '-') && highestOrderOperation < 1){
                        highestOrderOperation = 1;
                        indexOfHighestOrderOperation = j;
                    } else if ((function.charAt(j) == '*' || function.charAt(j) == '/') && highestOrderOperation < 2){
                        highestOrderOperation = 2;
                        indexOfHighestOrderOperation = j;
                    } else if (function.charAt(j) == '^' && highestOrderOperation < 3){
                        highestOrderOperation = 3;
                        indexOfHighestOrderOperation = j;
                    } else if(Character.isLetter(function.charAt(j)) && highestOrderOperation < 4){
                        highestOrderOperation = 4;
                        indexOfHighestOrderOperation = j;
                    }
                }
                
                System.out.println("\t " + highestOrderOperation + "     " + indexOfHighestOrderOperation);
                
                if(Character.isLetter(function.charAt(indexOfHighestOrderOperation))){
                    System.out.println("IMPLEMENT DEALING WITH WORD OPERATIONS");
                    int indexOfOperationEnd = indexOfHighestOrderOperation;
                    while(indexOfOperationEnd < function.length() && Character.isLetter(function.charAt(indexOfOperationEnd))){
                        indexOfOperationEnd++;
                    }
                    
                    String operation = function.substring(indexOfHighestOrderOperation, indexOfOperationEnd);
                    if(operation.equals("cos")){
                        indexOfOperationEnd += 2;
                        while(indexOfOperationEnd < function.length() && Character.isDigit(indexOfOperationEnd)){
                            indexOfOperationEnd++;
                        }
                        
                        $functions.add(function.substring(indexOfHighestOrderOperation, indexOfOperationEnd));
                        function = replaceString(function, ("$" + ($functions.size()-1)), indexOfHighestOrderOperation, indexOfOperationEnd);
                        $functions.set(i, function);
                        
                        System.out.println("Function replacing: " + function + "    " + indexOfHighestOrderOperation + "   " + indexOfOperationEnd);
                    } else if(operation.equals("sin")){
                        indexOfOperationEnd += 2;
                        while(indexOfOperationEnd < function.length() && Character.isDigit(indexOfOperationEnd)){
                            indexOfOperationEnd++;
                        }
                        
                        $functions.add(function.substring(indexOfHighestOrderOperation, indexOfOperationEnd));
                        function = replaceString(function, ("$" + ($functions.size()-1)), indexOfHighestOrderOperation, indexOfOperationEnd);
                        $functions.set(i, function);
                        
                        System.out.println("Function replacing: " + function + "    " + indexOfHighestOrderOperation + "   " + indexOfOperationEnd);
                    
                    } else if(operation.equals("ln")){
                        indexOfOperationEnd += 2;
                        while(indexOfOperationEnd < function.length() && Character.isDigit(indexOfOperationEnd)){
                            indexOfOperationEnd++;
                        }
                        
                        $functions.add(function.substring(indexOfHighestOrderOperation, indexOfOperationEnd));
                        function = replaceString(function, ("$" + ($functions.size()-1)), indexOfHighestOrderOperation, indexOfOperationEnd);
                        $functions.set(i, function);
                    } else if(operation.equals("log")){
                        System.out.println("DEAL WITH LOG PARSING");
                    }
                    
                    System.out.println(function);
                } else {
                    char operation = function.charAt(indexOfHighestOrderOperation);
                    int eq1Start = -1, eq1End = -1;
                    
                    for(int j = indexOfHighestOrderOperation; j >= 0; j--){
                        if(eq1End == -1 && Character.isDigit(function.charAt(j))){
                            eq1End = j+1;
                        } else if (function.charAt(j) == '$' || function.charAt(j) == '#'){
                            eq1Start = j;
                            break;
                        }
                    }
                    
                    int eq2Start = -1, eq2End = -1;
                    
                    for(int j = indexOfHighestOrderOperation; j < function.length(); j++){
                        if (function.charAt(j) == '$' || function.charAt(j) == '#'){
                            eq2Start = j;
                            j++;
                            while(j<function.length() && Character.isDigit(function.charAt(j))){
                                j++;
                            }
                            eq2End = j;
                            break;
                        }
                    }
                    
                    $functions.add(function.substring(eq1Start,eq2End));
                    function = replaceString(function, ("$" + ($functions.size()-1)), eq1Start, eq2End);
                    $functions.set(i, function);
                }
            }
            
            if(i == indexOfParentEquation){
                finalizedParentEquation = function;
            }
        }
        
        $functions.add(finalizedParentEquation); //we have to make sure that the highest level equation is always the last object in the array
        return $functions;
    }
    private static int getOperationCount(String function){
        int operationCount = 0;
        for(int j = 0; j < function.length(); j++){
            if(Character.isLetter(function.charAt(j))){
                operationCount++;
                while(j < function.length() && Character.isLetter(function.charAt(j))){
                    j++;
                }
            } else if (isOperationChar(function.charAt(j))){
                operationCount++;
            }
        }
        return operationCount;
    }

    private static ArrayList<String> findParentheses(String rawEquationString){
        String workingString = rawEquationString;
        ArrayList<String> $functions = new ArrayList<String>();
        while(isParenthesesPair(workingString)){
            int lastParenthesisOpen = -1;
            for(int i = 0; i < workingString.length(); i++){
                if(workingString.charAt(i) == '('){
                    lastParenthesisOpen = i;
                } else if (workingString.charAt(i) == ')'){
                    if(lastParenthesisOpen != -1){
                        $functions.add(workingString.substring(lastParenthesisOpen+1, i));
                        if(lastParenthesisOpen != 0){
                            workingString = workingString.substring(0, lastParenthesisOpen) + "$" + ($functions.size()-1) + workingString.substring(i+1);
                        } else if (i+1 != workingString.length()){
                            workingString = "$" + ($functions.size()-1) + workingString.substring(i+1);
                        } else {
                            workingString = "$" + ($functions.size()-1);
                        }
                    }

                    lastParenthesisOpen = -1;
                }
            }
        }

        $functions.add(workingString);

        return $functions;
    }
    private static boolean isParenthesesPair(String s){
        int open = 0;
        int close = 0;
        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) == '('){
                open++;
            } else if (s.charAt(i) == ')'){
                close++;
            }
        }

        System.out.println(open + "  " + close);

        return open == close && open != 0; 
    }

    //replace all variables and numbers in $functions with a #[number] and then return the key
    private static ArrayList<String> replaceTerms(ArrayList<String> $functions){
        ArrayList<String> terms = new ArrayList<String>();
        for(int i = 0; i < $functions.size(); i++){
            String function = $functions.get(i);
            while(containsUnconvertedNumberOrVariable(function)){ System.out.println("main while loop");
                int start = 0;
                for(int j = 0; j < function.length(); j++){
                    //if a $ function code or # var/num code is found, skip it
                    if(function.charAt(j) == '$' || function.charAt(j) == '#'){
                        j++;
                        while(j < function.length() && Character.isDigit(function.charAt(j))){
                            j++;
                        }

                        //safety
                        if(j == function.length()){
                            break;
                        }
                    }

                    if(Character.isDigit(function.charAt(j))){
                        int numberStartIndex = j;
                        while(j < function.length() && Character.isDigit(function.charAt(j))){
                            j++;
                        }

                        terms.add(function.substring(numberStartIndex,j));
                        function = replaceString(function, "#"+(terms.size()-1), numberStartIndex, j);
                        break;
                    } else if (letterIsAlone(j, function)){
                        terms.add(function.substring(j,j+1));
                        function = replaceString(function, "#"+(terms.size()-1), j, j+1);
                        break;
                    }
                }
            }

            $functions.set(i, function);
            System.out.println(function + "\t" + terms.get(terms.size()-1));
        }

        return terms;
    }
    private static boolean letterIsAlone(int index, String string){
        boolean characterBeforeIsNotLetter = true, characterAfterIsNotLetter = true;
        if(!Character.isLetter(string.charAt(index))){
            return false;
        }
        
        if(index == 0){
            characterBeforeIsNotLetter = true;
        } else {
            characterBeforeIsNotLetter = !Character.isLetter(string.charAt(index-1));
        }
        
        if(index == string.length()-1){
            characterAfterIsNotLetter = true;
        } else {
            characterAfterIsNotLetter = !Character.isLetter(string.charAt(index+1));
        }
        
        return (characterBeforeIsNotLetter && characterAfterIsNotLetter);
    }
    
    private static String replaceString(String base, String replace, int start, int end){
        if(start == 0){
            if(end == base.length()){
                base = replace;
            } else {
                base = replace + base.substring(end);
            }
        } else if (end == base.length()){
            base = base.substring(0, start) + replace;
        } else {
            base = base.substring(0, start) + replace + base.substring(end);
        }

        return base;
    }

    private static boolean containsUnconvertedNumberOrVariable(String function){
        System.out.println(function);
        for(int j = 0; j < function.length(); j++){
            //if a $ function code or # var/num code is found, skip it
            if(function.charAt(j) == '$' || function.charAt(j) == '#'){
                j++;
                while(j < function.length() && Character.isDigit(function.charAt(j))){
                    j++;
                }
                
                if(j == function.length()){
                    break;
                }
            }

            if(Character.isDigit(function.charAt(j))){
                return true;
            } else if (Character.isLetter(function.charAt(j))){
                if(j == function.length()-1){
                    return true;
                } else if (letterIsAlone(j, function)){
                    return true;
                }
            }
            
            
        }
        
        return false;
    }
    
    private static boolean isOperationChar(char c){
        switch(c){
            case '+': return true;
            case '-': return true;
            case '*': return true;
            case '/': return true;
            case '^': return true;
            default: return false;
        }
    }
    
    // public static Equation parse(String string){
    // ArrayList<Equation> equations = new ArrayList<Equation>();
    // boolean done = false;

    // do{
    // done = !replacePEMDAS(string, equations);
    // }while(!done);

    // return equations.get(equations.size()-1);
    // }

    // private static boolean replacePEMDAS(String string, ArrayList<Equation> equations){
    // //look for and replace parentheses
    // boolean noOperationFound = false;

    // while(!noOperationFound){
    // for(int i = 0; i < string.length(); i++){
    // if(string.charAt(i) == '('){
    // String restOfString = string.substring(i);
    // int endParentheses = restOfString.indexOf(')');
    // String testString = string.substring(i, endParentheses);

    // if(isSimpleEquation(testString)){
    // //add testString (translated) to the equations array and replace it in the main string with f#
    // string = string.substring(0, i) + "f" + equations.size() + string.substring(endParentheses+1);

    // equations.add(translateSimpleEquation(testString));
    // break;
    // }
    // }

    // if(i == string.length()-1){
    // noOperationFound = true;
    // }
    // }
    // }

    // //look for trig/ln/abs value
    // while(!noOperationFound){
    // for(int i = 0; i < string.length(); i++){
    // if(string.charAt(i) == 's' || string.charAt(i) == 'c' || string.charAt(i) == 'l'){
    // String restOfString = string.substring(i);
    // int endParentheses = restOfString.indexOf(')');
    // String testString = string.substring(i, endParentheses);

    // if(isSimpleEquation(testString)){
    // //add testString (translated) to the equations array and replace it in the main string with f#
    // string = string.substring(0, i) + "f" + equations.size() + string.substring(endParentheses+1);

    // equations.add(translateSimpleEquation(testString));
    // break;
    // }
    // } else if (string.charAt(i) == '|'){
    // String restOfString = string.substring(i);
    // int endParentheses = restOfString.indexOf('|');
    // String testString = string.substring(i, endParentheses);

    // if(isSimpleEquation(testString)){
    // //add testString (translated) to the equations array and replace it in the main string with f#
    // string = string.substring(0, i) + "f" + equations.size() + string.substring(endParentheses+1);

    // equations.add(translateSimpleEquation(testString));
    // break;
    // }
    // }

    // if(i == string.length()-1){
    // noOperationFound = true;
    // }
    // }
    // }

    // //look for and replace ^
    // //look for and replace * and /
    // //look for and replace + and -
    // return noOperationFound;
    // }

    // private static boolean isSimpleEquation(String string){
    // if(string.charAt(0) == 's' || string.charAt(0) == 'c' || string.charAt(0) == 'l'){
    // return isSimpleEquation(string.substring(string.indexOf('(')));
    // }

    // if(string.charAt(0) == '('){
    // string = string.substring(1, string.length()-1);
    // }

    // int operationCount = 0;
    // char currentChar;
    // for(int i = 0; i < string.length(); i++){
    // currentChar = string.charAt(i);
    // if(isSpecialChar(currentChar)){ //exclude numbers, letters, and periods
    // operationCount++;
    // }
    // }

    // return operationCount <= 1;
    // }

    // private static Equation translateSimpleEquation(String simpleEquation){
    // //find the index of the operation
    // int indexOfOperation = -1;
    // for(int i = 0; i < simpleEquation.length(); i++){
    // if(isSpecialCharacter(simpleEquation.charAt(i))){
    // indexOfOperation = i;
    // break;
    //             }
    //         }
    // 
    //         if(indexOfOperation == -1){
    //             return translateTerm(simpleEquation);
    //         } else {
    //             Equation term1 = translateTerm(simpleEquation.substring(0, indexOfOperation));
    //             Equation term2 = translateTerm(indexOfOperation+1);
    //         }
    //     }
    // 
    //     private static Equation translateTerm(String term){
    //         
    //     }
    //     
    private static boolean isSpecialChar(char check){
        switch(check){
            case '+': return true;
            case '-': return true;
            case '*': return true;
            case '/': return true;
            case '^': return true;
            case '(': return true;
            case ')': return true;
            case '{': return true;
            case '}': return true;
            case '[': return true;
            case ']': return true;
            case ':': return true;
            default: return false;
        }
    }
}
