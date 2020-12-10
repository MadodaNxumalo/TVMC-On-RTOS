/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TimedAutomata;

import java.util.ArrayList;

/**
 *
 * @author Madoda
 */
public class Transition {
    
    final private State source; //l
    final private State destination; //l'
    //private ArrayList<ClockConstraint> guard; //phi - implement a clock constraint
    final private ArrayList<ClockConstraint> guardsChecked; 
    final private Alphabet action; 
    private ArrayList<Clock> clockUpdates; //reset/delay clock are actions
    //private ArrayList<Clock> clockReset;
   /*
   public Transition(int s, int d, ArrayList<Integer> g, int a, ArrayList<Integer> updates,
           ArrayList<State> sS, ArrayList<ClockConstraint> ccS, ArrayList<Alphabet> aS) {
       Transition(ss.get(s),ss.get(d), ArrayList<ClockConstraint> guard, aS.get(a), ArrayList<Integer> resets) 
       /*setSourceState(s,sS);
       setDestinationState(d, sS);
       setGuards(g, ccS);
       setAction(a, aS);
       setClockUpdates(updates, ccS)*/
   //} 
    
    
    public Transition(State source,State destination, ArrayList<ClockConstraint> guard, Alphabet act, ArrayList<Clock> resets) {
        this.source = source;
        this.destination = destination;
        this.guardsChecked = guard;
        this.action = act;
        this.clockUpdates = resets;
    }
    //new Transition(processorAutomata.getStateSet().get(1), processorAutomata.getStateSet().get(0), 
               //zeroIndex, processorAutomata.getAlphabetSet().get(0), delay2);  
    public Transition(State source, State destination, ArrayList<ClockConstraint> guard, Alphabet act, String assign) {
        this.source = source;
        this.destination = destination;
        this.guardsChecked = guard;
        //encodeCC(guard);
        this.action = act;
        updateCC(assign); //Associate update/reset of clocks in CC
    }
    
    public Transition() {
        source = new State();
        destination = new State();
        guardsChecked = new ArrayList();
        action = new Alphabet();
        clockUpdates = new ArrayList();
    }
  
        
    public ArrayList<Clock> getClockResetS()  {
        return clockUpdates;
    }
    
    public Alphabet getAction()   {
        return action;
    }
    
    public State getDestinationState()   {
        return destination;
    }
    
    public ArrayList<ClockConstraint> getGuard()   {
        return guardsChecked;
    }
    
    public State getSourceState()   {
        return source;
    }
    
    
    @Override
    public String toString()  {
        return "Transition: "+source.getLabel()+" ---"+action.getAlphabet()+"--> "+destination.getLabel();
    }
    
        public void encodeCC(String s) {
        
    }
    
    public void updateCC(String s) {
        
    }
    
    public void addTransition(Transition other)   {
        
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
        Transition o = (Transition) obj;
        return action.getAlphabet().equals(o.action.getAlphabet()) && destination.getLabel().equals(o.destination.getLabel()) && source.getLabel().equals(o.source.getLabel());
    }
}
