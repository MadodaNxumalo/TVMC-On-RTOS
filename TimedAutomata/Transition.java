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
    
    private State source; //l
    private State destination; //l'
    private ArrayList<ClockConstraint> guardsChecked;
    private TimedAction action; 
    private ArrayList<Clock> clockUpdates; //reset/delay clock are actions
  
    
    
    public Transition(State source, State destination, ArrayList<ClockConstraint> guard, TimedAction act, ArrayList<Clock> resets) {
        this.source = source;
        this.destination = destination;
        this.guardsChecked = guard;
        this.action = act;
        this.clockUpdates = resets;
    }
    //new Transition(processorAutomata.getStateSet().get(1), processorAutomata.getStateSet().get(0), 
               //zeroIndex, processorAutomata.getAlphabetSet().get(0), delay2);  
    public Transition(State source, State destination, ArrayList<ClockConstraint> guard, TimedAction act, String assign) {
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
        action = new TimedAction();
        clockUpdates = new ArrayList();
    }
    
    public Transition(Transition other) {
        source = new State(other.source);
        destination = new State(other.destination);
        guardsChecked = new ArrayList();
        guardsChecked.addAll(other.guardsChecked);
        action = new TimedAction(other.action);
        clockUpdates = new ArrayList();
        clockUpdates.addAll(other.clockUpdates);
    }
  
        
    public ArrayList<Clock> getClockResetS()  {
        return clockUpdates;
    }
    
    public void setClockResets(ArrayList<Clock> clockR)  {
        clockUpdates = new ArrayList<>();
    }
    
    public TimedAction getTimedAction()   {
        return action;
    }
    
    public State getDestinationState()   {
        return destination;
    }
    
    public ArrayList<ClockConstraint> getGuard()   {
        return guardsChecked;
    }
    
    public void setGuard(ArrayList<ClockConstraint> g)  {
        guardsChecked=g;
    }
    
    public State getSourceState()   {
        return source;
    }
    
    
    @Override
    public String toString()  {
        return "Transition: "+source.toString()+" --("+action.toString()+")-"+guardsChecked.toString()+"--> "+destination.toString();
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
        return action.getSymbol().equals(o.action.getSymbol()) && destination.getLabel().equals(o.destination.getLabel()) && source.getLabel().equals(o.source.getLabel());
    }
}
