/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TimedAutomata;

//import TCTL.AtomicProposition;

import java.util.ArrayList;

//import java.util.function.Predicate;        
import org.logicng.formulas.*;

/**
 *
 * @author Madoda
 */
//A clock constraint is a boolean variable
public class ClockConstraint  {
    
    private String label;
    private Clock clock;
    private double bound;
    private String operand;
    private boolean ccEval;
    //private Variable logicVar;
    
    public ClockConstraint(String l,Clock c, double b, String op) {
        label = l;
        clock = c;
        bound = b;
        operand = op;
        ccEval = switchOperation();
        //logicVar = 
    }
    
    
    public ClockConstraint(Clock cl, String s) {
        //String[] str = s.split("(!&|)<>=");
        String[] str = s.split(" ");
        //encodePredicate(s);
        System.out.println(s + "s");
        for(String st : str)
            System.out.println(st);
        if (str.length >= 3)    {
            //clock = new Clock(str[0]);
            clock = cl;
            operand = str[1];
            bound = Double.parseDouble(str[2]);
            //operandAnd 
        }
        ccEval = switchOperation();
    }
    
    ClockConstraint() {
        label = new String();
        clock = new Clock();
        bound = 0.0;
        operand = "";
        ccEval = true;
    }
    
    public boolean lessThanCC()   {
        ccEval = clock.getValue() < bound;
        return ccEval;
    }
    
    public boolean lessEqualToCC()   {
        ccEval = clock.getValue() <= bound;
        return ccEval;
    }
    
    
    public boolean greaterEqualToCC()   {
        ccEval = clock.getValue() >= bound;
        return ccEval;
    }
    
    public boolean greaterThanCC()   {
        ccEval =  clock.getValue() > bound;
        return ccEval;
    }
    
    public boolean equalToCC()   {
        ccEval = clock.getValue() == bound;
        return ccEval;
    }
    
    public boolean conjunctCC(ClockConstraint other)   {
        return ccEval && other.ccEval;
    }
    
    
    
    
    public boolean switchOperation()   {
    switch (operand)  {
            case "==":
                return equalToCC();
            case "<":
                return lessThanCC();
            case "<=":
                return lessEqualToCC();
            case ">":
                return greaterThanCC();
            case ">=":
                return greaterEqualToCC();
        default:
            return false;
        }
    }
    
    public boolean getEvaluation()  {
        return ccEval;
    }
    
    public String getLabel()  {
        return label;
    }
    
    
    @Override
     public String toString()  {
        return clock.toString()+" "+ccEval+" "+bound;   
    }
    
    /*public Predicate<Clock> equalToCC(Clock c, double x)    {
        return clockPred -> (c.getClock() == x);
    }
    
    public Predicate<Clock> conjunctCC(ClockConstraint other)   {
        return clockPred.and(other.clockPred);
    }
    
    public Predicate<Clock> differenceCC(Clock clockX, Clock clockY, String value)   {//value Enum
                        
        double diff = clockX.getClock()-clockY.getClock(); //Check less than zero difference
        switch (value)  {
            case "==":
                return equalToCC(clockX, diff);
            case "<":
                return lessThanCC(clockX, diff);
            case "<=":
                return lessEqualToCC(clockX, diff);
            case ">":
                return greaterThanCC(clockX, diff);
            case ">=":
                return greaterEqualToCC(clockX, diff);
        default:
            return null;
        }
    }

    public Predicate<Clock> neg(Clock clock) {
        return clockPred.negate(); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Predicate<Clock> noChangeCC(Clock c)   {
        return clockPred;
    }

*/
}
