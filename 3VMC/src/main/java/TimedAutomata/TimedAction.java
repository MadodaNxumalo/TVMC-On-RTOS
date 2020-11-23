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
    
    public Alphabet getReadSymbol() {
        return symbol;
    }
    
    public Clock getReadInstance()  {
        return timeInstance;
    }
}
