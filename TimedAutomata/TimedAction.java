/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TimedAutomata;



/**
 *
 * @author Madoda
 */
public class TimedAction {
    private String symbol;
    private Double elapse;
    private boolean command;
    
    public TimedAction(String action, Double e, boolean trigger) {
        symbol = action;    
        elapse = e;
        command = trigger;
    }
    
    public TimedAction() {
        symbol = new String();    
        elapse = 0.0;
        command = false;
    }
    
    public TimedAction(TimedAction other) {
        symbol = other.symbol;    
        elapse = other.elapse;
        command = other.command;
    }
    
    
    public String getSymbol() {
        return symbol;
    }
    
    public Double getElapse()  {
        return elapse;
    }
    
    public boolean getCommand()  {
        return command;
    }
    
    public void setSymbol(String a)  {
        //if(!a.equals(null))
        symbol = a;
    }
    public void setElapse(double e)  {
        elapse = e;
    }
    
    public void setCommand(boolean c)  {
        command = c;
    }
    
    @Override
    public String toString() {
        return " "+symbol+" "+elapse+" "+command+" ";
    }
    
    
    
    
}
