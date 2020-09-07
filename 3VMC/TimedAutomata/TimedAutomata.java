/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TimedAutomata;

import java.util.*;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.formulas.Literal;

/**
 *
 * @author Madoda
 */
public class TimedAutomata {
    private ArrayList<State> stateSet;
    private ArrayList<Clock> clocks;
    private ArrayList<ClockConstraint> acc; //set of atomic clock constraints in guard or state invariant
    private ArrayList<Alphabet> alphabet;
    private ArrayList<Transition> transitions;
    
    //private Queue<State> stateQueue;
    
     
    
    TimedAutomata(ArrayList<State> s,ArrayList<Alphabet> a, ArrayList<Clock> c, ArrayList<ClockConstraint> cc, ArrayList<Transition> tr){//, Queue<State> q)   {
        stateSet = s;
        alphabet = a;
        clocks = c;
        acc = cc;
        transitions = tr;
        //stateQueue = q;
        
    }
    /*
    TimedAutomata(ArrayList<State> s,ArrayList<Alphabet> a, ArrayList<Clock> c, ArrayList<ClockConstraint> cc, ArrayList<Transition> tr)   {
        stateSet = s;
        alphabet = a;
        clocks = c;
        acc = cc;
        transitions = tr;
        
    }*/
    
    
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
        other.forEach((i) -> {
            this.clocks.add(i);
        });
    }
    
    
 
    
    public void addTimedAutomata(ArrayList<TimedAutomata> others)   {
        others.forEach((i) -> {
            addTimedAutomata(i);
        });
    }
    
    //public void addTimedAutomata(TimedAutomata other) { 
    public TimedAutomata addTimedAutomata(TimedAutomata other) { 
        TimedAutomata a = this;
        
        ArrayList<Alphabet> tempAlpha = new ArrayList<>();
        other.alphabet.forEach((i) -> {
            tempAlpha.add(i);
        });
        a.alphabet.forEach((i) -> {
            tempAlpha.add(i);
        });
        
        ArrayList<Clock>tempClocks = new ArrayList<>();
        other.clocks.forEach((i) -> {
            tempClocks.add(i);
        });
        a.clocks.forEach((i) -> {
            tempClocks.add(i);
        });
        
        ArrayList<ClockConstraint> tempCC = new ArrayList<>();
        other.acc.forEach((i) -> {
            tempCC.add(i);
        });
        a.acc.forEach((i) -> {
            tempCC.add(i);
        });
        
        ArrayList<State> tempStates = new ArrayList<>();
        a.stateSet.forEach((i) -> {
            other.stateSet.forEach((j) -> {
                State p = i;
                p = p.appendState(j);
                
                if(i.getInvariant()!= null) {        
                    for(ClockConstraint x: i.getInvariant())    {
                        p.addInvariant(x);
                    }
                }
                
                if(j.getInvariant()!= null) {        
                    for(ClockConstraint y: j.getInvariant())    {
                        p.addInvariant(y);
                    }
                }                 
                //p.setFinal(j);
                //p.setInitial(j);
                
                tempStates.add(p);
            });
        });
        
        /*Then, the product, denoted S1kS2, is hQ1 × Q2, Q01 × Q02, Σ1 ∪ Σ2,→i where
        (q1, q2) a → (q01, q02) iff either (i) a ∈ Σ1 ∩ Σ2 and q1a→1 q01 and 
        q2a→2 q02,or (ii) a ∈ Σ1 \ Σ2 and q1a→1 q01 and q02 = q2, or 
        (iii) a ∈ Σ2 \ Σ1 and q2a→2 q02and q01 = q1. 
        */
        ArrayList<Transition> tempTr = new ArrayList<>();
        
        for (State aSource: a.stateSet)   {
            for (State otherSource: other.stateSet) {  
                String sourceStateLabel = aSource.getLabel() + otherSource.getLabel();
                for(Alphabet tempAction : tempAlpha)    {
                        
                    int transA = getTransitionIndex(a.transitions,aSource, tempAction);
                    int transOther = getTransitionIndex(other.transitions,otherSource, tempAction);
                        
                    String targetOther = new String();
                    String targetA = new String();
                        
                    ArrayList<ClockConstraint> tempGuard = new ArrayList<>();
                    ArrayList<Clock> tempClockResets = new ArrayList<>();
                    
                    if(!(transOther == -1)) 
                        targetOther = other.transitions.get(transOther).getDestinationState().getLabel();
                    if(!(transA == -1)) {
                        targetA = a.transitions.get(transA).getDestinationState().getLabel();
                    }
                    
                    String targetStateLabel;
                    if(!targetA.isEmpty() && !targetOther.isEmpty())    {    
                        targetStateLabel = targetA + targetOther;    
                    } else if(!targetA.isEmpty())   {
                        targetStateLabel = targetA + otherSource.getLabel();    
                    } else if (!targetOther.isEmpty()) {
                        targetStateLabel = aSource.getLabel()+ targetOther;
                    } else
                        continue;
                        
                    State targetState = new State();
                    State sourceState = new State();
                        
                    for(State x : tempStates)   {
                        if(x.getLabel().equals(sourceStateLabel))
                            sourceState = x;
                        
                        if(x.getLabel().equals(targetStateLabel))
                            targetState = x;
                    }
                        
                    if(!(transA == -1)) {
                        addArrayElement(a.transitions.get(transA).getGuard(), tempGuard);
                        addArrayElements(a.transitions.get(transA).getClockResetS(), tempClockResets);
                    }
                    if(!(transOther == -1)) {
                        addArrayElement(other.transitions.get(transOther).getGuard(), tempGuard);
                        addArrayElements(other.transitions.get(transOther).getClockResetS(), tempClockResets);        
                    }
                        
                    Transition p = new Transition(sourceState,targetState,tempGuard, tempAction, tempClockResets); 
                    tempTr.add(p);
                }
            } 
        }
        return new TimedAutomata(tempStates,tempAlpha,tempClocks,tempCC, tempTr);
    }
    
    void addArrayElements(ArrayList<Clock> release, ArrayList<Clock> acquireElements)    {
        if(!(release==null))   {
            release.forEach((x) -> {
                acquireElements.add(x);
            });
        } 
    }
    void addArrayElement(ArrayList<ClockConstraint> release, ArrayList<ClockConstraint> acquireElements)    {
        if(!(release==null))   {
            release.forEach((x) -> {
                acquireElements.add(x);
            });
        } 
    }
    
    public boolean checkTransition(State source, Alphabet symbol)  {
        return transitions.stream().anyMatch((outTrans) -> ((outTrans.getSourceState()==source) && (symbol==outTrans.getAction()) ));
    }
    //if (n.getSourceState().equals(otherSource) && tempAction.equals(n.getAction()))   {
     public int getTransitionIndex(ArrayList<Transition> trans, State source, Alphabet symbol)  {
        //ArrayList<Integer> indexList = new ArrayList<>(); 
        //trans.stream().filter((outTrans) -> ((outTrans.getSourceState().equals(source)) && symbol.equals(outTrans.getAction()))).forEachOrdered((outTrans) -> {
        for(Transition outTrans : trans)    {
            if ((outTrans.getSourceState().equals(source)) && symbol.equals(outTrans.getAction())) 
                return trans.indexOf(outTrans);
        }
        //});
        
        return -1;
    }

    public PathRunLocation descreteTransition(PathRunLocation sourceLoc, Alphabet symbol)  {
        PathRunLocation successorLoc = new PathRunLocation();
        PathRunLocation failLoc = new PathRunLocation();
        clocks = sourceLoc.getClockValuations();
        //NTA.clockValuation must be sourceLoc.clocks
        //Tra
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
        //PathRunLocation successorLoc = new PathRunLocation(sourceLoc);
        
        /*for(ClockConstraint inv: sourceLoc.getPathState().getInvariant()) {
            if(!inv.getEvaluation())
                return new PathRunLocation();
        }*/
        /*
        clocks.forEach((c) -> {
            c.update(delay);
        });*/
        
        sourceLoc.getClockValuations().forEach((c) -> {
            c.update(delay);
        });
        
        /*for(ClockConstraint inv: sourceLoc.getPathState().getInvariant()) {
            if(!inv.getEvaluation())
                return new PathRunLocation();
        }*/
        sourceLoc.setPathClock(clocks);
        sourceLoc.setPathClock(sourceLoc.getClockValuations());
        
        return sourceLoc;
    }
    
    //Returns a set of target locations whose source location is loc
    public ArrayList<Transition> getOutTransition(PathRunLocation loc) {
        
        ArrayList<Transition> outLocations = new ArrayList<>();
        
        for(Transition outTrans: transitions)   {
            if(outTrans.getSourceState().equals(loc.getPathState()))   {
                //System.out.println(outTrans.getSourceState().toString()+" ---> "+outTrans.getDestinationState().toString());
                //PathRunLocation outLoc = new PathRunLocation(outTrans.getDestinationState(),clocks);
                
                //PathRunLocation outLoc = descreteTransition(loc, outTrans.getAction());
                //outLoc = delayTransition(outLoc,0); //Edit this 
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
    
    public Formula formulaFormat(ArrayList<ClockConstraint> cc) {
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
    } 
    

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
            System.out.println();
            System.out.print(tr.toString());
            //System.out.print("Source : "+ tr.getSourceState().toString());
            //System.out.print("Destin : "+ tr.getDestinationState().toString());
            //System.out.print("Action : " + tr.getAction().toString());
            /*tr.getGuard().forEach((g) -> {
                acc.get(g).print();
            });*/
        }
        System.out.println();
        System.out.println();
           
    }
    
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
    
