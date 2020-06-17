/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TimedAutomata;

import TCTL.Clock;
import java.util.*;

/**
 *
 * @author Madoda
 */
public class TimedAutomata {
    private ArrayList<State> stateSet;
    
    private ArrayList<Clock> clocks;
    private ArrayList<ClockConstraint> acc; //set of atomic clock constraints in guard or state invariant
    private ArrayList<Alphabet> alphabet;
    //private ArrayList<Transition> transitions;
    
    //private State initialState;
    //private final Set<State> finalStateSet;
    //private Set<String> invarients;
    //private ArrayList<String> guards;
     
    public TimedAutomata networkTimedAutomata(ArrayList<TimedAutomata> automataSet){
        TimedAutomata network = new TimedAutomata();
        automataSet.forEach((automataSet1) -> {
            this.addTimedAutomata(automataSet1);
            //TimedAutomata ta = automataSet.get(i); 
            //network.alphabet = this.alphabet.add(i)
        }); 
        return network;
    }
    
    //public void print()  {
    //    transitions.forEach((i) -> {
    //        i.print();
            //System.out.println("Task Automata: "+" ");
    //    });
    //}
   
    //public ArrayList<Clock> valuation()   { //TODO
    //    Clock x = this;
    //    return null;
    //}
    
    public void resetClocks(ArrayList<Clock> resetClockSet)   { //TODO
        resetClockSet.forEach((i) -> {
            int index = clocks.indexOf(i);
            clocks.get(index).reset();
        });
    }
    
    public void resetAllClocks()   { //TODO
        clocks.forEach((i) -> {
            i.reset();
        });
    }
    
    public void updateAllClocks(double delay)   { //TODO
        clocks.forEach((i) -> {
            i.update(delay);
        });
    }
    
    
    public void union(ArrayList<Clock> other)   {
        other.forEach((i) -> {
            this.clocks.add(i);
        });
    }
    
    
    public TimedAutomata(ArrayList<State> s,ArrayList<Clock> c, ArrayList<Alphabet> a)  {
        stateSet = s;
        alphabet = a;
        clocks = c;
        //transitions = t;
    }
    
   
    
    public TimedAutomata(TimedAutomata other)  {
        stateSet = other.stateSet;
        alphabet = other.alphabet;
        clocks = other.clocks;
        //transitions = t;
    }
    
    public TimedAutomata()  {
        stateSet = new ArrayList<>();
        alphabet = new ArrayList<>();
        clocks = new ArrayList<>();    
    }
    
    //public TimedAutomata(TimedAutomata ta)  {
    //}
    
    public void addTimedAutomata(TimedAutomata other) { 
        
        other.alphabet.forEach((i) -> {
            alphabet.add(i);
        });
        
        other.clocks.forEach((i) -> {
            clocks.add(i);
        });
       
        ArrayList<State> x = new ArrayList<>(stateSet);
        stateSet.clear();
        x.forEach((i) -> {
            other.stateSet.forEach((j) -> {
                State p = new State();
                p.appendState(i);
                p.appendState(j);
                stateSet.add(p);        
            });
        });    
    }
    
    //int targetStateIndex = nta.takeTransition(currentStateIndex, i);
    public int takeTransition(int indexSource, int symbolIndex, int delay)  {
        
        
        /* Discrete Transition: <l,n>--a,d--><l',n'>
        a. if there is a trainsition <l,g,a,D,l'> in TA
        b. n SAT g
        c. n'= reset D\in n
        d. n'SAT inv(l')
        e. if n+d SAT inv(l)
        */
        /*Then, the product, denoted S1kS2, is hQ1 × Q2, Q01 × Q02, Σ1 ∪ Σ2,→i where
        (q1, q2) a → (q01, q02) iff either (i) a ∈ Σ1 ∩ Σ2 and q1a→1 q01 and 
        q2a→2 q02,or (ii) a ∈ Σ1 \ Σ2 and q1a→1 q01 and q02 = q2, or 
        (iii) a ∈ Σ2 \ Σ1 and q2a→2 q02and q01 = q1. 
        */
        int index = -1, indexDesti = -1;
        for (Transition outTransition : stateSet.get(indexSource).getOutTransitions()) {
            if(outTransition.getAction().equals(alphabet.get(symbolIndex))) {//outTransition.getAction() == null ? alphabet.get(symbolIndex) == null : outTransition.getAction().equals(alphabet.get(symbolIndex)))
                index = stateSet.indexOf(outTransition.getDestinationState());
                //indexDesti = outTransition
            }
        }
        
        
        
        
        //stateSet.get(index).getOutTransitions()
        //n Sat(g); //n is a clock valuation
        
        if (delay == 0)
            resetAllClocks();
        
        //clockConstraint(n') Sat inv(l');
        
        if (delay > 0)
            updateAllClocks(delay);
        //if(delay = -1)
        
        return -1;
           
    }
    
    //Find start state
    public int findStateIndexInitial()  {
        return -1;
    }
    
        //Find start state
    public int findStateIndex(State other)  {
        return -1;
    }
    
    //Find start state
    public ArrayList<String> findFinalStatesIndices()  {
        return null;
    }

    
    //public State getInitialState()  {
        //return init;
    //}
    
    public ArrayList<State> getStateState()  {
        return stateSet;
    }
     
    //public Set<State> getFinalStateSat()  {
        //return finalStateSet;
    //} 
    
     public ArrayList<Clock> getClocks()  {
        return clocks;
    }
     
     public ArrayList<Alphabet> getAlphabetSet()  {
        return alphabet;
    }
    
    //public ArrayList<Transition> getTransitions() {
    //    return transitions;
    //} 
    
    //public void setInitialState(State i) {
    //    initialState = i;
    //}
    
    
    
}
