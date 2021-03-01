/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TimedAutomata;

//import TCTL.AtomicProposition;


//import java.util.function.Predicate;        

/**
 *
 * @author Madoda
 */
//A clock constraint is a boolean variable
public class ClockConstraint  {
    
    private String label;
    private Clock clock;
    private double bound;
    private boolean operand;
    private boolean ccEval;
    //private Variable logicVar;
    
    public ClockConstraint(String l,Clock c, double b, boolean op) {
        label = l;
        clock = c;
        bound = b;
        operand = op;
        ccEval = switchOperation();
        //logicVar = 
    }

    
    public ClockConstraint() {
        label = new String();
        clock = new Clock();
        bound = 0.0;
        operand = false;
        ccEval = true;
    }
    
    public ClockConstraint subSetting(ClockConstraint other)   {
        if(label==other.label)
            if(other.clock.getValue() < clock.getValue())
                return other;
            else //if(other.clock.getValue() > clock.getValue())
                return this;
            //else
        return this;        
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
 
    public boolean conjunctCC(ClockConstraint other)   {
        return ccEval && other.ccEval;
    }
  
    
    public boolean switchOperation()   {
        if (operand)
            return lessThanCC();
        else
            return lessEqualToCC();
    }
    
    public boolean getEvaluation()  {
        return ccEval;
    }
    
    public String getLabel()  {
        return label;
    }
    
    public Clock getClock()  {
        return clock;
    }
    
    public boolean getOperand()  {
        return operand;
    }
    
    public void setBound(double b) {
        if(b>=0)
            bound = b;
        else 
            bound = -0.01;
    }
    
    public void setOperand(boolean b)   {
        operand = b;
    }
    
    public Double getBound()  {
        return bound;
    }
    
    
    @Override
     public String toString()  {
        return clock.toString()+" "+ccEval+" "+bound;   
    }
    
    @Override
    public boolean equals(Object obj){
        //other.equals(other)
        if (obj == this) { 
            return true; 
        } 
        if (obj == null || obj.getClass() != this.getClass()) { 
            return false; 
        }
        ClockConstraint o = (ClockConstraint) obj;
        return clock.equals(o.clock) && bound==o.bound && operand==o.operand; //&& (clock==o.clock);
    }
}