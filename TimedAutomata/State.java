/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TimedAutomata;

import java.util.ArrayList;
import java.util.Objects;
//import java.util.Queue; 
//import pkg3vmc.Task;
/**
 *
 * @author Madoda
 */
public class State {
    private String label;
    private ArrayList<ClockConstraint> invariant;  //implement a ClockConstraint type
    private Boolean isInitial;
    private Boolean isFinal;
    //private Task stateComponent;

    public State(String name, ArrayList<ClockConstraint> inv, Boolean init, Boolean isfinal){//,Task t){
        label = name;
        invariant = new ArrayList<>();
        invariant.addAll(inv);
        //for(ClockConstraint in: inv)    
        //    invariant.add(new ClockConstraint(in));
        isInitial = init;
        isFinal = isfinal;
    }
    
    public State(String name, Boolean init, Boolean isfinal){//,Task t){
        label = name;
        invariant = new ArrayList<>();
        isInitial = init;
        isFinal = isfinal;
    }
    
    public State(){
        this.label = new String();
        this.invariant = new ArrayList<>();
        isInitial = false;
        isFinal = false;
        //stateComponent = new Task("NoName",0,0,0);
        //outTransitions = new ArrayList<>();
    }
    
    public State(State other){
        label = other.label;
        invariant = new ArrayList<>();
        invariant.addAll(other.invariant);
        isInitial = other.isInitial;
        isFinal = other.isFinal; 
    }
    
    public void setState(State other)   {
         label = other.label;
        invariant = other.invariant;
        isInitial = other.isInitial;
        isFinal = other.isFinal;
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
        State o = (State) obj;
        return label.equals(o.label); //&& Objects.equals(isFinal, o.isFinal) && Objects.equals(isInitial, o.isInitial);
    }
    


    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.label);
        return hash;
    }
    
    public State addState(State other) {
        State p = new State();
        p.appendState(other);
        p.appendState(this);
                
        //p.setInvariant(i.getInvariant());
        //p.setInvariant(j.getInvariant());
                 
        //p.setOutTransitions(other);
                
        p.setFinal(other);
        p.setInitial(other);
        
        return p;
    }
    
    public State joinTwoStates(State i, State j)	{
    	State p = new State(i);
        p = p.appendState(j);
        

        
        if(i.isFinalState()) 
            p.setFinal(i);
        if(j.isFinalState())
            p.setFinal(j);
        
        if(i.isFinalState() && j.isFinalState())
            p.setInitial(i);
        
        p.getInvariant().addAll(i.getInvariant());
        p.getInvariant().addAll(j.getInvariant());
        /*for(ClockConstraint c: i.getInvariant())    { 
            if(!t.acc.contains(c))  {
                t.acc.add(c);
            }
        }
         
        for(ClockConstraint c: j.getInvariant())    { 
            if(!t.acc.contains(c))  {
                t.acc.add(c);
            }
        }*/ 
        
        return p;
    }
    
    
    
    void addInvariant(ClockConstraint i)  {
        invariant.add(i);
    }
    /*
    void setInvariantSet(ArrayList<ClockConstraint> other)    {
        for (ClockConstraint i:other)
            invariant.add(i);
    }
    
    void setOutTransitions(State other)    {
        other.outTransitions.forEach((i) -> {
            outTransitions.add(i);
        });
    }*/
    
    void setFinal(State other) {
        isFinal = isFinal && other.isFinal;
    }
    
    void setInitial(State other) {
        isInitial = isInitial && other.isInitial;
    }
            
    public State appendState(State other)   {
        State x = this;
        label = x.label + other.label;
        
        for(ClockConstraint cc: other.invariant)
            if (!invariant.contains(cc))
                invariant.add(new ClockConstraint(cc));      
        //String concat = invariant.getPredicate().concat(other.invariant.getPredicate());
        if(other.isFinal || x.isFinal)
            isFinal = true;
        
        if(x.isInitial && other.isInitial)
            isInitial = true;
        
        //return this;
        return x;
    } 
    /*   
    public ArrayList<Transition> getOutTransitions()    {
        return outTransitions;
    }*/
    
    public String getLabel()    {
        return label;
    }
    
    public ArrayList<ClockConstraint> getInvariant()    {
        return invariant;
    }
    
    public Boolean isFinalState()    {
        return isFinal;
    }
    
    public Boolean isInitialState()    {
        return isInitial;
    }

    @Override
    public String toString()  {
        return "State: "+label+" "+isInitial+" "+isFinal+" "+invariant.toString();
        /*outTransitions.forEach((_item) -> {    
            _item.print();
        });*/
    }
    
    
    
   
    
}
