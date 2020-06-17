/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TimedAutomata;

import TCTL.Clock;
import TCTL.Predicate;
import java.util.ArrayList;

/**
 *
 * @author Madoda
 */
public class Transition {
    
    private State source; //l
    private State destination; //l'
    private ArrayList<ClockConstraint> guard; //phi - implement a clock constraint 
    private Alphabet action; 
    public ArrayList<Double> clockAssign = new ArrayList<>(); //reset/delay clock are actions 
    
    public Transition(State destination, ArrayList<ClockConstraint> guard, Alphabet act, ArrayList<Double> assign) {
        //this.source = source;
        this.destination = destination;
        this.guard = guard;
        this.action = act;
        this.clockAssign = assign;
    }
    
    public Transition(State destination, ClockConstraint guard, String act, String assign) {
        //this.source = source;
        this.destination = destination;
        this.guard = new ArrayList<>();
        //encodeCC(guard);
        this.action = new Alphabet(act);
        updateCC(assign); //Associate update/reset of clocks in CC
    }
    
    public Transition() {
        //source = new State();
        destination = new State();
        guard = new ArrayList();
        action = new Alphabet();
        //assignments = 0.0;
    }
    
    public void encodeCC(String s) {
        
    }
    
    public void updateCC(String s) {
        
    }
    
    public void addTransition(Transition other)   {
        
    }
    
    public Alphabet getAction()   {
        return action;
    }
    
    public State getDestinationState()   {
        return destination;
    }
    
    public ArrayList<ClockConstraint> getGuard()   {
        return guard;
    }
    
    public State getSourceState()   {
        return source;
    }
    
    
    public void print()  {
        System.out.println("Transition: "+action.getAlphabet()+" "+ destination.getLabel());
    }
}
