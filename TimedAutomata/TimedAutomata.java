/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TimedAutomata;

import java.util.*;
//import org.logicng.formulas.Formula;
//import org.logicng.formulas.FormulaFactory;
//import org.logicng.formulas.Literal;

/**
 *
 * @author Madoda
 */
public class TimedAutomata {
    private final ArrayList<State> stateSet;
    private ArrayList<Clock> clocks;
    private final ArrayList<ClockConstraint> acc; //set of atomic clock constraints in guard or state invariant
    private final ArrayList<Alphabet> alphabet;
    private final ArrayList<Transition> transitions;
    
    //private Queue<State> stateQueue;
    
     
    
    TimedAutomata(ArrayList<State> s,ArrayList<Alphabet> a, ArrayList<Clock> c, ArrayList<ClockConstraint> cc, ArrayList<Transition> tr){//, Queue<State> q)   {
        stateSet = s;
        alphabet = a;
        clocks = c;
        acc = cc;
        transitions = tr;
        //stateQueue = q;
        
    }

    
    
    public TimedAutomata(TimedAutomata other)  {
        stateSet = other.stateSet;
        alphabet = other.alphabet;
        clocks = other.clocks;
        acc = other.acc;
        transitions = other.transitions;
        //stateQueue = other.stateQueue;
        
    }
    
    public TimedAutomata()  {
        stateSet = new ArrayList<>();
        alphabet = new ArrayList<>();
        clocks = new ArrayList<>();
        acc = new ArrayList<>();
        transitions = new ArrayList<>();
        //stateQueue = new LinkedList<State>();
    }
    
    public TimedAutomata networkTimedAutomata(ArrayList<TimedAutomata> automataSet){
        TimedAutomata network = new TimedAutomata();
        automataSet.forEach((automataSet1) -> {
            this.addTimedAutomata(automataSet1);
            //TimedAutomata ta = automataSet.get(i); 
            //network.alphabet = this.alphabet.add(i)
        }); 
        return network;
    }
    
    
    public void resetClocks(ArrayList<Integer> clockIndices)   { //TODO
        clockIndices.forEach((i) -> {
            clocks.get(i).reset();
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
        
        //other.forEach((i) -> {
            this.clocks.addAll(other);
        //});
    }
    
    
 
    
    public TimedAutomata addTimedAutomata(ArrayList<TimedAutomata> others)   {
        //TimedAutomata temp = new TimedAutomata();
        others.forEach((i) -> {
            addTimedAutomata(i);
        });
        return this;
    }
    
    //public void addTimedAutomata(TimedAutomata other) { 
    public TimedAutomata addTimedAutomata(TimedAutomata other) { 
        TimedAutomata a = this;
        TimedAutomata t = new TimedAutomata();
        t.alphabet.addAll(other.alphabet);
        t.alphabet.addAll(a.alphabet);
  
        t.clocks.addAll(other.clocks);
        t.clocks.addAll(a.clocks);
        
        t.acc.addAll(other.acc);
        t.acc.addAll(a.acc);
        
        other.stateSet.forEach((i) -> {
            a.stateSet.forEach((j) -> {
               
                State p = new State(i);
                p = p.appendState(j);
               
                int countU =0, countR = 0;
                countR = ( p.getLabel().split("Run", -1).length ) - 1; 
                countU = ( p.getLabel().split("InUse", -1).length ) - 1; 
                
                
                if( i.getLabel().contains("Available") || i.getLabel().contains("InUse") )  {
                    if(countR == countU)    {
                        t.stateSet.add(p);
                    }
                } else  {
                    t.stateSet.add(p);
                }
                
            });
        });
        
        other.transitions.forEach((i) -> {
            a.transitions.forEach((j) -> {
                
                Transition p = new Transition();
                Transition p1 = new Transition();
                Transition p2 = new Transition();
                
                State x = new State(i.getSourceState());
                x.appendState(j.getSourceState());
                
                int countU =0, countR = 0;
                countR = ( x.getLabel().split("Run", -1).length ) - 1; 
                countU = ( x.getLabel().split("InUse", -1).length ) - 1; 
                
                
                if( x.getLabel().contains("Available") || x.getLabel().contains("InUse") )  {
                    if(countR == countU)    {
                        p.getSourceState().appendState(x);
                        p1.getSourceState().appendState(x);
                        p2.getSourceState().appendState(x);
                    }
                } else  {
                    p.getSourceState().appendState(x);
                    p1.getSourceState().appendState(x);
                    p2.getSourceState().appendState(x);
                }
     
                
            if(!p.getSourceState().getLabel().isEmpty())    {
                
                String substrI = i.getAction().getAlphabet().substring(0,4);
                String substrJ = j.getAction().getAlphabet().substring(0,4);
                
                if(substrI.equals(substrJ))    {
                    p.getAction().setAlphabet(i.getAction().getAlphabet());
                    
                    State y = new State(i.getDestinationState());
                    y.appendState(j.getDestinationState());
                    p.getDestinationState().appendState(y);
                    
                    countU =0; countR = 0;
                    countR = ( p.getDestinationState().getLabel().split("Run", -1).length ) - 1; 
                    countU = ( p.getDestinationState().getLabel().split("InUse", -1).length ) - 1; 
                
                
                    if( p.getDestinationState().getLabel().contains("Available") || p.getDestinationState().getLabel().contains("InUse") )  {
                        if(countR == countU)  { 
                            t.transitions.add(p);
                        }
                    } else  {
                        t.transitions.add(p);
                    }

                } else  {
                    p1.getAction().setAlphabet(i.getAction().getAlphabet());
                    
                    State y1 = new State(i.getDestinationState());
                    y1.appendState(j.getSourceState());
                    p1.getDestinationState().appendState(y1);
                    
                    
                    countU =0; countR = 0;
                    countR = ( p1.getDestinationState().getLabel().split("Run", -1).length ) - 1; 
                    countU = ( p1.getDestinationState().getLabel().split("InUse", -1).length ) - 1; 
                
                
                    if( p1.getDestinationState().getLabel().contains("Available") || p1.getDestinationState().getLabel().contains("InUse") )  {
                        if(countR == countU)  { 
                            t.transitions.add(p1);
                        }
                    } else  {
                        t.transitions.add(p1);
                    }
                    
                    
                    
                    p2.getAction().setAlphabet(j.getAction().getAlphabet());
                    State z = new State(i.getSourceState());
                    z.appendState(j.getDestinationState());                                 
                    p2.getDestinationState().appendState(z);
                    
                    countU =0; countR = 0;
                    countR = ( p2.getDestinationState().getLabel().split("Run", -1).length ) - 1; 
                    countU = ( p2.getDestinationState().getLabel().split("InUse", -1).length ) - 1; 
                
                
                    if( p2.getDestinationState().getLabel().contains("Available") || p2.getDestinationState().getLabel().contains("InUse") )  {
                        if(countR == countU)    {
                            //p2.getDestinationState().appendState(z);
                            t.transitions.add(p2);
                        }
                    } else  {
                        //p2.getDestinationState().appendState(z);
                        t.transitions.add(p2);
                    }
                }    
                     
                    
                }

            });  
        });   
        return t;
    }        


    public boolean checkTransition(State source, Alphabet symbol)  {
        return transitions.stream().anyMatch((outTrans) -> ((outTrans.getSourceState()==source) && (symbol==outTrans.getAction()) ));
    }
    //if (n.getSourceState().equals(otherSource) && tempAction.equals(n.getAction()))   {
    public int getTransitionIndex(ArrayList<Transition> trans, State source, Alphabet symbol)  {
        for(Transition outTrans : trans)    {
            if ((outTrans.getSourceState().equals(source)) && symbol.equals(outTrans.getAction())) 
                return trans.indexOf(outTrans);
        } 
        return -1;
    }
    


    public PathRunLocation descreteTransition(PathRunLocation sourceLoc, Alphabet symbol)  {
        PathRunLocation successorLoc = new PathRunLocation();
        PathRunLocation failLoc = new PathRunLocation();
        clocks = sourceLoc.getClockValuations();
        //NTA.clockValuation must be sourceLoc.clocks
        for(Transition outTrans: transitions)   {
            if((outTrans.getSourceState()==sourceLoc.getPathState()) && (symbol==outTrans.getAction()) )  {
                
                for(ClockConstraint g: outTrans.getGuard())
                    if(!g.getEvaluation())  {//Evaluation must be on sourceLoc.getClocks  {
                        System.out.println("DT - GUARD CC Fail: " + g.toString());
                        return failLoc;
                    }
                
                outTrans.getClockResetS().forEach((clockResets) -> {
                    clockResets.reset();  //Resets must be on sourceLoc.getClocks as per guard
                });
                for(ClockConstraint inv: successorLoc.getPathState().getInvariant()) {
                    if(!inv.getEvaluation())  { //Evaluation should be on sourceLoc.getClocks
                        System.out.println("DT - Invariant CC Fail: " + inv.toString());
                        return failLoc;
                    }
                }
                System.out.println("DT - Transition Success: ");
                
                successorLoc.setPathState(outTrans.getDestinationState());
                successorLoc.setPathClock(clocks);
                //successorLoc.setPathClock(sourceLoc.getClockValuations());
                break;
            }
        }
        return successorLoc;
    }
    
    public PathRunLocation delayTransition(PathRunLocation sourceLoc, double delay)  {
        clocks = sourceLoc.getClockValuations();
        sourceLoc.getClockValuations().forEach((c) -> {
            c.update(delay);
        });
        sourceLoc.setPathClock(clocks);
        sourceLoc.setPathClock(sourceLoc.getClockValuations());
        
        return sourceLoc;
    }
    
    //Returns a set of target locations whose source location is loc
    public ArrayList<Transition> getOutTransition(PathRunLocation loc) {
        
        ArrayList<Transition> outLocations = new ArrayList<>();
        
        for(Transition outTrans: transitions)   {
            if(outTrans.getSourceState().getLabel().equals(loc.getPathState().getLabel()))   {
                outLocations.add(outTrans);
            }
        }
        return outLocations;
    }
    //Elapse of time: for a state (s, ν) and a real-valued time increment δ ≥ 0,(s, ν) δ → (s, ν + δ) 
        //if for all 0 ≤ δ0 ≤ δ, ν + δ0 satisfies the invariant I(s).
    
    public ArrayList<Transition> currentStateTransitions(State s)   {
        ArrayList<Transition> sourceStateTrans = new ArrayList<>();
        transitions.stream().filter((i) -> (i.getSourceState().getLabel().equals(s.getLabel()))).forEachOrdered((i) -> {
            sourceStateTrans.add(i);
        });
        /*for(Transition i : transitions) {
            if(i.getSourceState().getLabel().equals(s.getLabel()))
                sourceStateTrans.add(i);
        }*/
        return sourceStateTrans;
    }
    
    /*public Formula formulaFormat(ArrayList<ClockConstraint> cc) {
        FormulaFactory f = new FormulaFactory("ccValuation");
        ArrayList<Literal> ccLiterals = new ArrayList<>();
        for(ClockConstraint xcc : cc)    {
            boolean x =xcc.switchOperation();
            final Literal xx = f.literal(xcc.getLabel(), xcc.getEvaluation());
            ccLiterals.add(xx);
        }        
        //final Formula formula = f.and(lit.get(0),lit.get(1));
        final Formula formula = f.and(ccLiterals);
        return formula;
    } */
    

    public boolean timeElapseRun(State source, double delay)    {
        //clockConstraint(n') Sat inv(l');
        if (delay > 0)
            updateAllClocks(delay);
        for(ClockConstraint i: source.getInvariant()) {   
            //acc.get(i).switchOperation();            
            if (!i.getEvaluation())
                return false;
        }
        return true;
    }    
    
    public void runTimedAutomata(ArrayList<TimedAction> timedWord)   { //timedWord has increasing seq of non negative numbers
    }
    
    public void print()  {
        alphabet.forEach((a) -> {
            System.out.println(a.toString());
        });
        System.out.println();
        clocks.forEach((c) -> {
            c.print();
        });
        System.out.println();
        acc.forEach((cc) -> {
            System.out.println(cc.toString());
        });
        System.out.println();
        stateSet.forEach((s) -> {
            System.out.println(s.toString());
        });
        System.out.println();
        for(Transition tr : transitions)    {
            System.out.println(tr.toString());
        }
        System.out.println();
        System.out.println();
           
    }
    
    public ArrayList<State> getStateSet()  {
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
    
    public ArrayList<ClockConstraint> getClockConstraint(){
        return acc;
    } 
     public ArrayList<Transition> getTransitions()  {
         return transitions;
     }
     

//Find start state
    public int findStateIndexInitial()  {
        for(State s:stateSet)   {
            if(s.isInitialState())
                return stateSet.indexOf(s);
        }
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
}

    
    //public State getInitialState()  {
        //return init;
    //}
    
        //Location switch: for a state (s, ν) and a switch hs, a, ϕ, λ, s0 i such that ν satisfies ϕ, (s, ν) a→ (s0, ν[λ := 0]).
    /*public int stateSwitchRun(PathRunLocation indexSource, Transition trans, double delay)    {
        //state@IndexSource Sat property guards property: tLogic
        int index = -1;
        index = stateSet.indexOf(trans.getDestinationState());
        //for (Transition outTransition : stateSet.get(indexSource).getOutTransitions()) {
            //if(outTransition.getAction().equals(alphabet.get(symbolIndex))) {//outTransition.getAction() == null ? alphabet.get(symbolIndex) == null : outTransition.getAction().equals(alphabet.get(symbolIndex)))
            //    index = stateSet.indexOf(outTransition.getDestinationState());
            //} else 
            //    return -1;
            
        for (ClockConstraint g: trans.getGuard()) { //n SAT g
            g.switchOperation();
            if(g.getEvaluation() == false)  
                return -1;
            resetClocks(trans.getClockResetS());
        }
        //tLogic.satisfy(stateSet.get(indexSource));
        //if (delay == 0)   //n'= reset D\in n
         //Clock reset is based on transition

        if (!timeElapseRun(trans.getSourceState(), delay))
            return -1;
        return index;
    }*/
    
    
    //public void setInitialState(State i) {
    //    initialState = i;
    //}
    //int targetStateIndex = nta.takeTransition(currentStateIndex, i);
    //public int takeTransition(int indexSource, int symbolIndex, double delay)  {
    //public int takeTransition(int indexSource, TimedAction symbolIndex, int delay)  {    
        
        /* Discrete Transition: <l,n>--a,d--><l',n'>
        a. if there is a trainsition <l,g,a,D,l'> in TA
        b. n SAT g
        c. n'= reset D\in n
    
        d. n'SAT inv(l')
        
        e. if n+d SAT inv(l)  :::: time elapse transition
        */
        
        /*

        int index = -1, indexDesti = -1;
        for (Transition outTransition : stateSet.get(indexSource).getOutTransitions()) {
            if(outTransition.getAction().equals(alphabet.get(symbolIndex))) {//outTransition.getAction() == null ? alphabet.get(symbolIndex) == null : outTransition.getAction().equals(alphabet.get(symbolIndex)))
                index = stateSet.indexOf(outTransition.getDestinationState());
                //indexDesti = outTransition
            }
            
            for (ClockConstraint clc: outTransition.getGuard()) { //n SAT g
                if(clc.ccEval == false)
                    return -1;
            }
            
            if (delay == 0)   //n'= reset D\in n
                resetAllClocks();
            
            //clockConstraint(n') Sat inv(l');
            if (delay > 0)
                updateAllClocks(delay);
            //if(delay = -1) do nothing
            
            for (ClockConstraint clc: outTransition.getGuard()) {
                if(clc.ccEval == false)
                    return -1;
            }
        }
        indexDesti = index;*/
        //return 0;
    //}
    
     /*
                        for(Transition n :other.transitions)    {
                            if (n.getSourceState().equals(otherSource) && tempAction.equals(n.getAction()))   {
                                targetOther = n.getDestinationState().getLabel();
                                
                                if(!(n.getGuard()==null))   {
                                    n.getGuard().forEach((x) -> {
                                        tempGuard.add(x);
                                    }); 
                                }
                                if(!(n.getClockResetS()==null))   {
                                    n.getClockResetS().forEach((x) -> {
                                        tempClockResets.add(x);
                                    });
                                }
                            }
                        }
                        
                          for(Transition m : a.transitions)    {
                            if (m.getSourceState().equals(aSource) && tempAction.equals(m.getAction()))   {
                                targetA = m.getDestinationState().getLabel();
                                
                                if(!(m.getGuard()==null))   {
                                    m.getGuard().forEach((x) -> {
                                        tempGuard.add(x);
                                    });
                                }
                                if(!(m.getClockResetS()==null))   {
                                    m.getClockResetS().forEach((x) -> {
                                        tempClockResets.add(x);
                                    });
                                }
                            }
                        }*/
    
