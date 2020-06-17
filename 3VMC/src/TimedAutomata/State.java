/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TimedAutomata;

import TCTL.Clock;
import java.util.ArrayList;

/**
 *
 * @author Madoda
 */
public class State {
    private String label;
    private ClockConstraint invariant; //implement a ClockConstraint type
    private Boolean isInitial;
    private Boolean isFinal;
    private ArrayList<Transition> outTransitions;

    public State(String name, ClockConstraint inv, Boolean init, Boolean isfinal, ArrayList<Transition> trans){
        label = name;
        invariant = inv;
        System.out.print(inv);
        isInitial = init;
        isFinal = isfinal;
        outTransitions = new ArrayList<>(trans);
        for(Transition tr : trans)
            if(tr.getSourceState().label.equals(this.label))
                outTransitions.add(tr);
    }
    
    public State(){
        this.label = new String();
        this.invariant = new ClockConstraint();
        isInitial = false;
        isFinal = false;
        outTransitions = new ArrayList<>();
    }
    
    public State(State other){
        this.label = other.label;
        this.invariant = other.invariant;
        isInitial = other.isInitial;
        isFinal = other.isFinal;
        outTransitions = new ArrayList<>();
        other.outTransitions.forEach((i) -> {
            outTransitions.add(i);
        });
        
    }

    
    public void appendState(State other)   {
        //State x = this;
        label = label+other.label;
        invariant.conjunctCC(other.invariant);
        //String concat = invariant.getPredicate().concat(other.invariant.getPredicate());
        if(other.isFinal || isFinal)
            isFinal = true;
        if(isInitial && other.isInitial)
            isInitial = true;
        other.outTransitions.forEach((i) -> {
            outTransitions.add(i);
        });
        //return this;
    }
    
    
    
    public ArrayList<Transition> getOutTransitions()    {
        return outTransitions;
    }
    
    public String getLabel()    {
        return label;
    }
    
    public ClockConstraint getInvariant()    {
        return invariant;
    }
    
    public Boolean isFinalState()    {
        return isFinal;
    }
    
    public Boolean isInitialState()    {
        return isInitial;
    }

    public void print()  {
        System.out.println("");
        System.out.println("State: "+label+" "+isInitial+" "+isFinal);
        outTransitions.forEach((_item) -> {    
            _item.print();
        });
    }
}
