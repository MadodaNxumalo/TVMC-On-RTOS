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
    
    public TimedAction(String action, Double e) {
        symbol = action;    
        elapse = e;
    }
    
    public TimedAction() {
        symbol = new String();    
        elapse = 0.0;
    }
    
    
    public String getSymbol() {
        return symbol;
    }
    
    public Double getElapse()  {
        return elapse;
    }
    
    public void setSymbol(String a)  {
        //if(!a.equals(null))
        symbol = a;
    }
    public void setInstance(double c)  {
        elapse = c;
    }
    
    @Override
    public String toString() {
        return " "+symbol+" "+elapse+" ";
    }
}
