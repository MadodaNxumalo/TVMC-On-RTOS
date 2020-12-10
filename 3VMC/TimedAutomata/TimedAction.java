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
    private Alphabet symbol;
    private Clock timeInstance;
    
    public TimedAction(Alphabet action, Clock valuation) {
        symbol = action;    
        timeInstance = valuation;
    }
    
    public TimedAction() {
        symbol = new Alphabet();    
        timeInstance = new Clock();
    }
    
    
    public Alphabet getReadSymbol() {
        return symbol;
    }
    
    public Clock getReadInstance()  {
        return timeInstance;
    }
    
    public void setReadSymbol(Alphabet a)  {
        //if(!a.equals(null))
        symbol = a;
    }
    public void setReadInstance(double c)  {
        timeInstance=new Clock(String.valueOf(c));
    }
}
