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

import Components.Task;

/**
 *
 * @author Madoda
 */
public class TimedAutomata {
    private final ArrayList<State> stateSet;
    private ArrayList<Clock> clocks;
    private final ArrayList<ClockConstraint> acc; //set of atomic clock constraints in guard or state invariant
    private final ArrayList<TimedAction> alphabet;
    private final ArrayList<Transition> transitions;
    private boolean inUse;
    
    
    //private Queue<State> stateQueue;
    
     
    
    TimedAutomata(ArrayList<State> s, ArrayList<TimedAction> a, ArrayList<Clock> c, ArrayList<ClockConstraint> cc, ArrayList<Transition> tr){//, Queue<State> q)   {
        stateSet = s;
        alphabet = a;
        clocks = c;
        acc = cc;
        transitions = tr;
        //stateQueue = q;
        inUse = false;
    }

    
    
    public TimedAutomata(TimedAutomata other)  {
        stateSet = new ArrayList<>();
        stateSet.addAll(other.stateSet);
        
        alphabet = new ArrayList<>();
        alphabet.addAll(other.alphabet);
        
        clocks = new ArrayList<>();
        clocks.addAll(other.clocks);
        
        acc = new ArrayList<>();
        acc.addAll(other.acc);
        
        transitions = new ArrayList<>();
        transitions.addAll(other.transitions);
        
        inUse = other.inUse;
    }
    
    public TimedAutomata()  {
        stateSet = new ArrayList<>();
        alphabet = new ArrayList<>();
        clocks = new ArrayList<>();
        acc = new ArrayList<>();
        transitions = new ArrayList<>();
        inUse=false;
        //stateQueue = new LinkedList<State>();
    }
    
    
    public TimedAutomata networkTimedAutomata(ArrayList<TimedAutomata> automataSet){
        TimedAutomata network = new TimedAutomata();
        automataSet.forEach((automataSet1) -> {
            this.addTimedAutomata(automataSet1);
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
        others.forEach((i) -> {
            addTimedAutomata(i);
        });
        return this;
    }
    
    public boolean findItem(ArrayList<Object> x, Object o)   {
        for (Object i : x)
            if(i.equals(o))
                return true;
        return false;
    }
    
    public TimedAutomata compositeTimedAutomata(TimedAutomata other) { 
        ArrayList<ArrayList<State>> Q = new ArrayList<>();
        ArrayList<ArrayList<State>> F = new ArrayList<>();
        ArrayList<Transition> E = new ArrayList<>();
        ArrayList<ArrayList<State>> V = new ArrayList<>();
        
        ArrayList<State> p = new ArrayList<>();
        p.add(this.stateSet.get(0));
        p.add(other.stateSet.get(0));
        
        V.add(p);
        
        while(!V.isEmpty()) {
            ArrayList<State> currState = V.remove(0);
            Q.add(currState);
            
            boolean isfinal = true;
            for(State x: currState)
                if(!x.isFinalState())
                    isfinal = false;
            if(isfinal)
                F.add(currState);
            
            ArrayList<Transition> outTransOther = new ArrayList<>();
            ArrayList<Transition> outTrans = new ArrayList<>();
            for(State x: currState) {
                if(this.getStateSet().contains(x))  {
                    PathRunLocation prLoc = new PathRunLocation(x,this.clocks);
                    outTrans = this.getOutTransition(prLoc);
                    //ArrayList<Transition> getOutTransition(PathRunLocation loc)
                }
                if(other.getStateSet().contains(x)) {
                    PathRunLocation prLoc = new PathRunLocation(x,this.clocks);
                    outTransOther = other.getOutTransition(prLoc);
                }
            }
            for(Transition tt: outTrans)    {
                for(Transition ot: outTransOther)   {
                    if(tt.getTimedAction().getSymbol().equals(ot.getTimedAction().getSymbol()))    {
                        ArrayList<State> nextState = new ArrayList<>();
                        nextState.add(tt.getDestinationState());
                        nextState.add(ot.getDestinationState());
                        
                        Transition compTr = new Transition();
                        
                        E.add(compTr);
                        if(!Q.contains(nextState)) {
                            V.add(nextState);
                        } 
                    }
                        
                }
            }
        }
        
        return this;
    }
    
    
    public TimedAutomata addTimedAutomata(TimedAutomata other) { 
        TimedAutomata a = this;
        TimedAutomata t = new TimedAutomata();
        t.alphabet.addAll(other.alphabet);
        t.alphabet.addAll(a.alphabet);
 /*       
        for(TimedAction act:other.alphabet)	{
            if (!act.getSymbol().contains("releasePr"))  {  
                    t.alphabet.add(act);
            }
       }
        
        for(TimedAction act:a.alphabet)	{
            if (!act.getSymbol().contains("releasePr"))  {  
                    t.alphabet.add(act);
            }
       }*/
        
       for(Clock c:other.clocks)	{
            if (!t.clocks.contains(c))  {  
                    t.clocks.add(c);
            }
       }
        
        for(Clock c:a.clocks)
            if (!t.clocks.contains(c))
                t.clocks.add(c);
        
        for(ClockConstraint c:other.acc)    {
            if(!c.getClock().getLabel().equals(c.getClock2().getLabel()))
                if(!t.acc.contains(c))  {
                    t.acc.add(c);
                }
        }
       
        for(ClockConstraint c:a.acc)
            if(!c.getClock().getLabel().equals(c.getClock2().getLabel()))
                if(!t.acc.contains(c))
                    t.acc.add(c);
        
//        System.out.println();
//        System.out.println("Other State: "+ other.getStateSet().toString());
//        System.out.println("a State: "+ a.getStateSet().toString());
//        System.out.println();
        
        
        other.stateSet.forEach((i) -> {
            a.stateSet.forEach((j) -> {
                
                State p = new State(i);
                p = p.appendState(j);
                              
                if(i.isFinalState()) 
                    p.setFinal(i);
                if(j.isFinalState())
                    p.setFinal(j);
                
                if(i.isFinalState() && j.isFinalState())
                    p.setInitial(i);
                
                 for(ClockConstraint c: i.getInvariant())    { 
                    if(!t.acc.contains(c))  {
                        t.acc.add(c);
                    }
                }
                 
                for(ClockConstraint c: j.getInvariant())    { 
                    if(!t.acc.contains(c))  {
                        t.acc.add(c);
                    }
                } 
                
              //Change CountR & CountU conditions
              int countU=0, countR=0;
              
              countR = ( p.getLabel().split("Run", -1).length ) - 1; //"Run"+label
              countU = ( p.getLabel().split("InUsePr", -1).length ) - 1; //InUse+label
                
 //             if(i.getLabel().contains("Avail") || i.getLabel().contains("InUse") )  {
             
            	  if(countR != countU)    {
//            		  System.out.println("countR != countU");
            		  
            		  if(countR == 1)	{
            			  t.stateSet.add(p);
 //           			  System.out.println("countR == 1");
            		  }
//            		  System.out.println("p State: "+ p.toString());
            	  } else  {
 //           		  System.out.println("Otherwise: countR == countU");
 //           		  System.out.println("p State: "+ p.toString());
            	  		t.stateSet.add(p);
            	  }
 //             } 
               //t.stateSet.add(p);
            });
        });
        
        
        
        other.transitions.forEach((i) -> {
            a.transitions.forEach((j) -> {
            	
            	Transition tTransI = new Transition();
            	Transition tTransJ = new Transition();
            	
            	if(i.getDestinationState().getLabel().contains("Term"))	{
            		
            		State tSource =  new State();
                	State tDest = new State();
                	
            		tSource.appendState(i.getDestinationState());
            		tSource.appendState(j.getSourceState());
            		
            		tDest.appendState(i.getDestinationState());
            		tDest.appendState(j.getDestinationState());
            		
            		tTransI.getTimedAction().setSymbol(j.getTimedAction().getSymbol());
                    tTransI.getTimedAction().setElapse(j.getTimedAction().getElapse());
                    tTransI.setClockResets(j.getClockResetS());
                    tTransI.getGuard().addAll(j.getGuard());
                    
                    tTransI.getSourceState().appendState(tSource);
                    tTransI.getDestinationState().appendState(tDest);
 /*                   if(j.getSourceState().getLabel().contains("Run") && j.getTimedAction().getSymbol().contains("rel"))	{
            			System.out.println("i Dest Term: "+i.toString());
            			System.out.println("j: "+j.toString());
            			System.out.println("i Dest tTrans: "+tTransI.toString());
            			System.out.println();                    
            		}*/
                    addValidTransition(t.transitions, tTransI);

            	}
            	if (j.getDestinationState().getLabel().contains("Term") )	{
            		
            		State tSourceJ =  new State();
                	State tDestJ = new State();
            		
            		tSourceJ.appendState(i.getSourceState());
            		tSourceJ.appendState(j.getDestinationState());
            		
            		tDestJ.appendState(i.getDestinationState());
            		tDestJ.appendState(j.getDestinationState());
            		
            		tTransJ.getTimedAction().setSymbol(i.getTimedAction().getSymbol());
                    tTransJ.getTimedAction().setElapse(i.getTimedAction().getElapse());
                    tTransJ.setClockResets(i.getClockResetS());
                    tTransJ.getGuard().addAll(i.getGuard());
                    
                    tTransJ.getSourceState().appendState(tSourceJ);
                    tTransJ.getDestinationState().appendState(tDestJ);
/*                    if(i.getSourceState().getLabel().contains("Run") && i.getTimedAction().getSymbol().contains("rel"))	{
            			System.out.println("i : "+i.toString());
            			System.out.println("j Dest Term:     "+j.toString());
            			System.out.println("j Dest tTrans: "+tTransJ.toString());
            			System.out.println();
                    }*/
                    addValidTransition(t.transitions, tTransJ);
                       
            	}
            	
                 	          	
                State x = new State(i.getSourceState());
                x.appendState(j.getSourceState());
//                boolean stateSetContains(State st, ArrayList<State> staSet)
                
 //               State dest1 = new State(i.getSourceState()); 
                State dest2 = new State(i.getSourceState()); 
                State dest3 = new State(i.getDestinationState()); 
                State dest4 = new State(i.getDestinationState()); 

 //               dest1.appendState(j.getSourceState());  
                dest2.appendState(j.getDestinationState());  
                dest3.appendState(j.getSourceState());
                dest4.appendState(j.getDestinationState());
                
                
            	Transition trans2 = new Transition();
            	Transition trans3 = new Transition();
            	Transition trans4 = new Transition();
                
            	
                trans2.getSourceState().appendState(x);
                trans3.getSourceState().appendState(x);
                trans4.getSourceState().appendState(x);
                
                
                trans2.getTimedAction().setSymbol(j.getTimedAction().getSymbol());
                trans2.getTimedAction().setElapse(j.getTimedAction().getElapse());
                trans2.setClockResets(j.getClockResetS());
                trans2.getGuard().addAll(j.getGuard());
                
                trans3.getTimedAction().setSymbol(i.getTimedAction().getSymbol());
                trans3.getTimedAction().setElapse(i.getTimedAction().getElapse());
                trans3.setClockResets(i.getClockResetS());
                trans3.getGuard().addAll(i.getGuard());
                
                trans4.getTimedAction().setSymbol(j.getTimedAction().getSymbol());
                trans4.getTimedAction().setElapse(j.getTimedAction().getElapse());
                trans4.setClockResets(j.getClockResetS());
                trans4.getGuard().addAll(j.getGuard());
                
                
                
                trans2.getDestinationState().appendState(dest2);
                trans3.getDestinationState().appendState(dest3);
                trans4.getDestinationState().appendState(dest4);

                
                
//                System.out.println("Trans2: "+trans2.toString());
//                System.out.println("Trans3: "+trans3.toString());
//                System.out.println("Trans4: "+trans4.toString());
 //               System.out.println();
                              


            	 
           		 addValidTransition(t.transitions, trans2);
           		 addValidTransition(t.transitions, trans3);
           		 addValidTransition(t.transitions, trans4);		       
                //}
            });
        });
        
        
        
        
        System.out.println();
        return t;
    } 
    
    void addValidTransition(ArrayList<Transition> t, Transition p)  {
        int uCountDest = 0, rCountDest = 0, uCountSource = 0, rCountSource = 0;
        int errCountDest = 0;
        rCountDest = ( p.getDestinationState().getLabel().split("Run", -1).length ) - 1; 
        uCountDest = ( p.getDestinationState().getLabel().split("InUse", -1).length ) - 1;
        
        rCountSource = ( p.getSourceState().getLabel().split("Run", -1).length ) - 1; 
//      rCountSource = ( p.getSourceState().getLabel().split("Avail", -1).length ) - 1;
        uCountSource = ( p.getSourceState().getLabel().split("InUse", -1).length ) - 1;
        errCountDest = ( p.getDestinationState().getLabel().split("Err", -1).length ) - 1;
        
        if( p.getSourceState().getLabel().contains("Term"))	{
        	if(!transContains(p,t))	{
//        	 if(p.getSourceState().getLabel().contains("Run") 
//        			 && p.getTimedAction().getSymbol().contains("rel"))	{
//     			System.out.println("Run Aqc Trans: "+p.toString());
//     			System.out.println();                    
 //    		}
        	
        		if ( !(rCountDest > 1 || errCountDest > 1 
        			|| rCountDest+errCountDest > 1 || rCountSource > 1 
        			|| p.getTimedAction().getSymbol().contains("Pr") 
        				))	{
//        			System.out.println();
//        			System.out.println("Term AddedTrans: "+ p.toString());
        			t.add(p);
        		}
        	}
        }
           
        //Processor and Tasks
        if( p.getDestinationState().getLabel().contains("Avail") || p.getDestinationState().getLabel().contains("InUse") )  {
            if(rCountDest == uCountDest )  { 
                if(!transContains(p,t))	{
                	if (!p.getTimedAction().getSymbol().contains("Pr")) {
                		if (rCountSource != uCountSource) {
                			if (rCountSource == 1 && uCountSource == 1)	{
//                					System.out.println();
//                    				System.out.println("1st AddedTrans: "+p.toString());
                    				t.add(p);
                			}
                		} else {
//                			System.out.println();
//                			System.out.println("2nd AddedTrans: "+p.toString());
                			t.add(p);
                		}
                    }
                }
            }
        } else  { //TaskA and TaskB
            //System.out.println(p.toString()+"  P R!=U");
            if(!transContains(p,t))	{
            	if (rCountSource != uCountSource) {
            		if (rCountSource == 1)	{
            			if(errCountDest==1)	{ 
            				if (!(p.getDestinationState().getLabel().contains("Err") 
                					&& (p.getTimedAction().getSymbol().contains("rel") 
                					|| p.getTimedAction().getSymbol().contains("acq"))))	{
            					if (!(p.getDestinationState().getLabel().contains("Err") &&
            							p.getDestinationState().getLabel().contains("Term")))	{
//            						System.out.println();
//            						System.out.println("3A Added Trans: "+p.toString());
            						t.add(p);
            					}
            				}
            			} else {
            				if (!(p.getDestinationState().getLabel().contains("Err") 
                					&& p.getTimedAction().getSymbol().contains("abo")))	{
            						if(!(p.getSourceState().getLabel().contains("Run") 
            							&& p.getTimedAction().getSymbol().contains("acq")))	{            					
 //           							System.out.println();
 //           							System.out.println("3B Added Trans: "+p.toString());
            							t.add(p);
            						}
            				}
            			}
            		} 
            	} else {
            		if ( !(rCountDest > 1 || errCountDest > 1 || rCountDest+errCountDest > 1) )	{
//            			System.out.println();
 //           			System.out.println("4th Added Trans: "+p.toString());
            			t.add(p);
            		}
        		}
            }
        }
    }
    
     
    
    
    public boolean transContains(Transition tr, ArrayList<Transition> transSet) {
        return transSet.stream().anyMatch(t -> (t.equals(tr)));
    }
    
    public boolean stateSetContains(State st, ArrayList<State> stSet) {
        return stSet.stream().anyMatch(state -> (state.equals(st)));
    }

    public boolean checkTransition(State source, TimedAction symbol)  {
        return transitions.stream().anyMatch((outTrans) -> ((outTrans.getSourceState()==source) && (symbol==outTrans.getTimedAction())));
    }
    //if (n.getSourceState().equals(otherSource) && tempAction.equals(n.getAction()))   {
    public int getTransitionIndex(ArrayList<Transition> trans, State source, TimedAction symbol)  {
        for(Transition outTrans : trans)    {
            if ((outTrans.getSourceState().equals(source)) && symbol.equals(outTrans.getTimedAction())) 
                return trans.indexOf(outTrans);
        } 
        return -1;
    }
    
    //nta.descreteTransition(currentLocation, action);
    public StateZone descreteTransition(StateZone sourceLoc, TimedAction symbol)  {
        StateZone successorLoc = new StateZone();
        StateZone failLoc = new StateZone();
        //clocks = sourceLoc.getClockValuations();
        clocks = sourceLoc.getZone().getClocks();
        //NTA.clockValuation must be sourceLoc.clocks
                            //Task x = abstractQueue.peek();
                    //if (outgoingLocation.getAction()=="")   
        for(Transition outTrans: transitions)   {
            if((outTrans.getSourceState()==sourceLoc.getZoneLocation()) && (symbol==outTrans.getTimedAction()) )  {
                
                for(ClockConstraint g: outTrans.getGuard()) {
                    int x = clocks.indexOf(g.getClock());
                    if(clocks.get(x).getValue()> g.db.getBound())  {//Evaluation must be on sourceLoc.getClocks  {
                        //System.out.println("DT - GUARD CC Fail: " + g.toString());
                        return failLoc;
                    }
                }
                outTrans.getClockResetS().forEach((clockResets) -> {
                    clockResets.reset();  //Resets must be on sourceLoc.getClocks as per guard
                });
                for(ClockConstraint inv: successorLoc.getZoneLocation().getInvariant()) {
                    int x = clocks.indexOf(inv.getClock());
                    if(clocks.get(x).getValue() > inv.db.getBound())  { //Evaluation should be on sourceLoc.getClocks
                        //System.out.println("DT - Invariant CC Fail: " + inv.toString());
                        return failLoc;
                    }
                }
                //System.out.println("DT - Transition Success: ");
                
                successorLoc.getZoneLocation().setState(outTrans.getDestinationState());
                successorLoc.getZone().setClocks(clocks);
                //successorLoc.setPathClock(sourceLoc.getClockValuations());
                break;
            }
        }
        return successorLoc;
    }


    public StateZone takeDescreteTransition(StateZone sourceLoc, Transition outTrans, TimedAction symbol)  {
    	StateZone successorLoc = new StateZone();
        StateZone failLoc = new StateZone();
        //clocks = sourceLoc.getClockValuations();
        clocks = sourceLoc.getZone().getClocks();
        //NTA.clockValuation must be sourceLoc.clocks
                            //Task x = abstractQueue.peek();
                    //if (outgoingLocation.getAction()=="")   
        //for(Transition outTrans: transitions)   {
        
        //if((outTrans.getSourceState()==sourceLoc.getPathState()) && (symbol==outTrans.getAction()) )  {
            for(ClockConstraint g: outTrans.getGuard()) {
                int x = clocks.indexOf(g.getClock());
                if(clocks.get(x).getValue() > g.db.getBound())    {//Evaluation must be on sourceLoc.getClocks  {
                    //System.out.println("DT - GUARD CC Fail: " + g.toString());
                    return failLoc;
                }
            }    
            outTrans.getClockResetS().forEach((clockResets) -> {
                clockResets.reset();  //Resets must be on sourceLoc.getClocks as per guard
            });
                
            for(ClockConstraint inv: successorLoc.getZoneLocation().getInvariant()) {
                int x = clocks.indexOf(inv.getClock());
                if(clocks.get(x).getValue() > inv.db.getBound())    {//Evaluation should be on sourceLoc.getClocks
                    //System.out.println("DT - Invariant CC Fail: " + inv.toString());
                    return failLoc;
                }
            }
            //System.out.println("DT - Transition Success: ");
            //successorLoc.setPathState(outTrans.getDestinationState());
            //successorLoc.setPathClock(clocks);   
            successorLoc.getZoneLocation().setState(outTrans.getDestinationState());
            successorLoc.getZone().setClocks(clocks);
            return successorLoc;
        //}
        
        //return failLoc;
    }
    
    public StateZone delayTransition(StateZone sourceLoc, double delay)  {
        
        clocks = sourceLoc.getZone().getClocks();
        
        sourceLoc.getZone().getClocks().forEach((c) -> {
            c.update(delay);
        });
        sourceLoc.getZone().setClocks(clocks);
        //sourceLoc.setPathClock(sourceLoc.getZone().getClocks());
        
        return sourceLoc;
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
        	System.out.println();
        	System.out.println("OUT TRANS1: "+outTrans.toString());
            if(outTrans.getSourceState().getLabel().equals(loc.getPathState().getLabel()))   {
            		outLocations.add(outTrans);
            }
        }
        
        return outLocations;
    }
    
  //Returns a set of target locations whose source location is loc
    public ArrayList<Transition> getOutTransition(State loc) {
        ArrayList<Transition> outLocations = new ArrayList<>();
        for(Transition outTrans: transitions)   {
        	System.out.println();
        	System.out.println("OUT TRANS2: "+outTrans.toString());
            if(outTrans.getSourceState().getLabel().equals(loc.getLabel()))   {
            		outLocations.add(outTrans);
            }
        }
        
        return outLocations;
    }
    
    
  //Returns a set of target locations whose source location is loc
    public ArrayList<Transition> getOutTransition(StateZone loc) {
    	
        ArrayList<Transition> outLocations = new ArrayList<>();
        for(Transition outTrans: transitions)   {
        	System.out.println();
        	System.out.println("OUT TRANS3: "+outTrans.toString());
            if(outTrans.getSourceState().getLabel().equals(loc.getZoneLocation().getLabel()))   {
            		outLocations.add(outTrans);
            }
        }
        
        return outLocations;
    }

    
  //Returns a set of target locations whose source location is loc
    public ArrayList<Transition> getOutTransition(Queue<Task> abstractQ, StateZone loc) {
    	//Queue<Task> localQ
        ArrayList<Transition> outLocations = new ArrayList<>();
        ArrayList<Transition> abortLocation = new ArrayList<>();
        int counter = 0, aboCounter = 0;
        boolean deque = false;
        for(Transition outTrans: transitions)   {
            if(outTrans.getSourceState().getLabel().equals(loc.getZoneLocation().getLabel()))   {
            	String label=outTrans.getTimedAction().getSymbol().substring(0, 3);
//            	System.out.println("OUT TRANS4: "+outTrans.toString() );
            	outLocations.add(outTrans);
            }
        }
        return outLocations;
    }
/*                boolean b = false;
                boolean b2 = false;
                switch(label)
                {
                	case "enq":
                		b = enqueueAction(abstractQ, outTrans, loc); 	
                		break;
                	case "acq":
                		b = acquireAction(abstractQ, outTrans,deque);
                		break;
                	case "rel":
                		b= releaseAction(outTrans);
                		break;
                	case "abo":
                		b2 = abortAction(abstractQ,outTrans);
                		break;
                	default:
                		System.out.println("Default ");
                }
                
                if(b)	{
                	counter++;
                	outLocations.add(outTrans);
              }
                else {  //if (b2)
                	aboCounter++;
                	abortLocation.add(outTrans); 	
                }
            }
        }*/
          //System.out.println("Counter: "+counter+" "+aboCounter);
//        if(counter==0)	
//        	for(Transition p: abortLocation)   {
//        		outLocations.add(p);
//        	}
        

    
    
    
    //Returns a set of target locations whose source location is loc
    public ArrayList<Transition> getOutTransition(Queue<Task> p, StateZone loc, int l) {
    	
        ArrayList<Transition> outLocations = new ArrayList<>();
        boolean deque = false;
        for(Transition outTrans: transitions)   {

    	   boolean b = false;
//           boolean b2 = false;
           if(outTrans.getSourceState().getLabel().equals(loc.getZoneLocation().getLabel()))   {
//               String label=outTrans.getTimedAction().getSymbol().substring(0, 2);
        	   if(outTrans.getTimedAction().getSymbol().contains("enque"))	{
            		b = enqueueAction(p, outTrans, loc);
            		System.out.println(" Enqueue IF Reached: "+ b);
            	}
            	else if ((outTrans.getTimedAction().getSymbol().contains("acquire")))	{
            		//outLocations.add(outTrans);
            		b = acquireAction(p, outTrans, deque);
            		System.out.println(" Acquire IF Reached: "+ b);
            	}
            	else if ((outTrans.getTimedAction().getSymbol().contains("release"))) {
            		b = releaseAction(outTrans);
            		System.out.println(" Release IF Reached: "+ b);
            	}
            	else if ((outTrans.getTimedAction().getSymbol().contains("abort")))	{
            		b = abortAction(p, outTrans);
            		System.out.println(" Abort IF Reached: "+ b);
            	}
            	else	{
            		System.out.println("Default: "+ outTrans.getTimedAction().getSymbol());
            	}
            	
            	/*if(deque)	{
            		Task ts = new Task();
            		if(!p.isEmpty())
            			ts = p.remove();
            	}*/
            	if(b)	{
            		outLocations.add(outTrans);
            	}
            }
        }
        System.out.println("OUT LOCATION SIZE: "+ outLocations.size() );
        return outLocations;
    }
    
    public boolean acquireAction(Queue<Task> q, Transition t, boolean deque) {  //, boolean da)	{
      	t.getTimedAction().setCommand(false);
      	deque = false;
      	if(!q.isEmpty() && !inUse)	{
      		Task ts = q.peek();
      		//System.out.println("InUse: "+inUse+"  "+t.getSourceState().getLabel()+" Acq: Peek: "+ts.getLabel()+" TAct: "+t.getTimedAction().getSymbol());
      		if (
      				t.getSourceState().getLabel().contains(ts.getLabel()) 
      				&& (t.getSourceState().getLabel().contains("Avail"))  	
      				&& t.getTimedAction().getSymbol().contains(ts.getLabel())
      				)	{
      			ts = q.remove();
      			//q.remove(); 		//sort this q.remove line
      			deque = true;
      			inUse = true;
      			if(!ts.getLabel().contains("Sh"))	//{
      				t.getTimedAction().setElapse(ts.getWCET());
      			t.getTimedAction().setCommand(true);
      			//da = false;
      			//}
      			//System.out.println("From State: "+t.getSourceState().getLabel()+" Reading: "+t.getTimedAction().getSymbol()+" Action Returns: "+ t.getTimedAction().getCommand());
      			return t.getTimedAction().getCommand();
      		}
      	}
      	t.getTimedAction().setCommand(false);
      //	System.out.println("From State: "+t.getSourceState().getLabel()+" Reading: "+t.getTimedAction().getSymbol()+" Action Returns: "+ t.getTimedAction().getCommand());
      	return t.getTimedAction().getCommand();
      }
    
     public boolean releaseAction(Transition t)	{
      	t.getTimedAction().setCommand(false);
      	if (t.getSourceState().getLabel().contains("InUse")
      			&& inUse)	
      	{
      		// && t.getSourceState().getLabel().contains("Run"))	{
      		inUse = false;
      		t.getTimedAction().setCommand(true);
      //		System.out.println("From State: "+t.getSourceState().getLabel()+" Reading: "+t.getTimedAction().getSymbol()+" Action Returns: "+ t.getTimedAction().getCommand());
      		return t.getTimedAction().getCommand();
      	}
      	t.getTimedAction().setCommand(false);
     // 	System.out.println("From State: "+t.getSourceState().getLabel()+" Reading: "+t.getTimedAction().getSymbol()+" Action Returns: "+ t.getTimedAction().getCommand());
      	return t.getTimedAction().getCommand();
      }
      
      public boolean abortAction(Queue<Task> q, Transition t)	{
      	
      	
//      	if (t.getSourceState().getLabel().contains("InUse") 
//      			&& t.getSourceState().getLabel().contains("Run")
//      			//&& t.getTimedAction().getSymbol().contains("abort")
//      			&& inUse
//      			)	{
//      		//inUse = false;
//      		t.getTimedAction().setCommand(true);
 //     		System.out.println("From State: "+t.getSourceState().getLabel()+" Reading: "+t.getTimedAction().getSymbol()+" Action Returns: "+ t.getTimedAction().getCommand());
 //     		return t.getTimedAction().getCommand();
 //     	} 
    	t.getTimedAction().setCommand(false);
      	if(!q.isEmpty()) {
      	//	&& !inUse)	{
      	
      		Task ts = q.peek();
      		//System.out.println("InUse: "+inUse+"  "+t.getSourceState().getLabel()+" Acq: Peek: "+ts.getLabel()+" TAct: "+t.getTimedAction().getSymbol());
      		if (t.getSourceState().getLabel().contains("InQ"+ts.getLabel()) 
      				&& (t.getSourceState().getLabel().contains("Available"))  	
      				&& t.getTimedAction().getSymbol().contains(ts.getLabel()))	{
          		t.getTimedAction().setCommand(true);
          		//System.out.println("From State: "+t.getSourceState().getLabel()+" Reading: "+t.getTimedAction().getSymbol()+" Action Returns: "+ t.getTimedAction().getCommand());
          		return t.getTimedAction().getCommand();
      		} else {
      			t.getTimedAction().setCommand(false);
      			//System.out.println("From State: "+t.getSourceState().getLabel()+" Reading: "+t.getTimedAction().getSymbol()+" Action Returns: "+ t.getTimedAction().getCommand());
      			return t.getTimedAction().getCommand();
      		}
      	}      	
      	t.getTimedAction().setCommand(false);
		//System.out.println("From State: "+t.getSourceState().getLabel()+" Reading: "+t.getTimedAction().getSymbol()+" Action Returns: "+ t.getTimedAction().getCommand());
		return t.getTimedAction().getCommand();
      }
      
      
      
      
      
      
      
      public boolean enqueueAction(Queue<Task> p, Transition t, StateZone sz) { //boolean de)	{
        	t.getTimedAction().setCommand(false);
        	/*
        	if(!p.isEmpty())	{
        		Task ts = p.peek();
        		//System.out.println("Before TRange "+sz.getTimeRange()+ " Occu: "+ts.getOccurance());
        		double diff = ts.getOccurance() - sz.getTimeRange();
        		if(diff > 0)	{
        			System.out.println("Call Again: "+sz.getTimeRange()+ " Occurance: "+ts.getOccurance());
        			sz.setTimeRange(ts.getOccurance());
        			return enqueueAction(p, t, sz);
        		}
        		if (t.getTimedAction().getSymbol().contains(ts.getLabel()) ) { 
        				//&& highClock>=p.peek().getOccurance())	{
        			//if(sz.getTimeRange() > ts.getOccurance())
//        			ts = p.remove(); 
//        			q.add(ts);
        			t.getTimedAction().setCommand(true);
        			System.out.println("Time Range "+sz.getTimeRange()+ " Occurance: "+ts.getOccurance());
        			//System.out.println("Zone Now is: "+sz.toString());
        			//System.out.println("Added Transition: "+ts.toString()+" Qsize: "+q.size());
        			//de = false;
        			System.out.println("EnqueueAction Returns: "+ t.getTimedAction().getCommand());
        			return t.getTimedAction().getCommand();
        		}
        	}*/
        	t.getTimedAction().setCommand(true);
        //	System.out.println("From State: "+t.getSourceState().getLabel()+" Reading: "+t.getTimedAction().getSymbol()+" Action Returns: "+ t.getTimedAction().getCommand());
        	return t.getTimedAction().getCommand();
        }
        
      
      
       
        
        
        
        /*public void preemptAction(Queue<Task> q, Transition t)	{
        	t.getTimedAction().setCommand(false);
        	if (t.getSourceState().getLabel().contains("Run"+q.peek().getLabel().charAt(0)) && 
        			deadline > q.peek().deadline) && 
        			currentLabel.contains("InUse"))	{
        		q.add(this);
        		t.getTimedAction().setCommand(true); 
        	}
        	t.getTimedAction().setCommand(true);	
        }*/
  
    

    
    
 
    //Elapse of time: for a state (s, Î½) and a real-valued time increment Î´ â‰¥ 0,(s, Î½) Î´ â†’ (s, Î½ + Î´) 
        //if for all 0 â‰¤ Î´0 â‰¤ Î´, Î½ + Î´0 satisfies the invariant I(s).
    
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
    

    public void timeElapseRun(State source, double delay)    {
        //clockConstraint(n') Sat inv(l');
        if (delay > 0)
            updateAllClocks(delay);
        for(ClockConstraint i: source.getInvariant()) {   
            //acc.get(i).switchOperation();            
            int x = clocks.indexOf(i.getClock());
            //if(clocks.get(x).getValue() > i.db.getBound())
               // return false;
        }
        //return true;
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
     
    public ArrayList<TimedAction> getTimedAction()  {
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


/* 
if(countR != countU)    {
//	  System.out.println("countR != countU");
	  
	  if(countR == 1)	{
		  t.stateSet.add(p);
//       	System.out.println("countR == 1");
	  }
//	  System.out.println("p State: "+ p.toString());
  } else  {
//       		  System.out.println("Otherwise: countR == countU");
//       		  System.out.println("p State: "+ p.toString());
  		t.stateSet.add(p);
  }*/
                    
                    //Transition p = new Transition();
            		
//            		State x = new State(j.getSourceState());
//            		x.appendState(tm);
//            		p.getSourceState().appendState(x);
            	
//            		p.getTimedAction().setSymbol(j.getTimedAction().getSymbol());
//            		p.getTimedAction().setElapse(j.getTimedAction().getElapse());
//            		p.setClockResets(j.getClockResetS());
//            		p.getGuard().addAll(j.getGuard());

                    /*for(ClockConstraint g:j.getGuard())
            			if(!p.getGuard().contains(g))   {
            				if(g.getDiffBound().getBound()<0 && (!p.getDestinationState().getLabel().contains("Err")))
            					continue;
            				p.getGuard().add(g);         
            			}*/                        
//            		State y = new State(j.getDestinationState());
//            		y.appendState(tm);
                
//            		p.getDestinationState().appendState(y);
//            		if(!p.getTimedAction().getSymbol().contains("acquirePr") 
//            				&& !p.getTimedAction().getSymbol().contains("releasePr"))
//            			addValidTransition(t.transitions, p);
//            		System.out.println("VALID pii: "+p.toString());
                    
                    
                    
                    
//        		}
        			
//        	}
            
//               ArrayList<State> inState = new ArrayList<>();
        /*    boolean conti = true;
            for(State k: this.getStateSet())	{
            	if(!(k.getLabel().equals(x.getLabel())))	{
            		conti = false;
            		//System.out.println(x.getLabel());
            	}
            }*/
            
          
            
            
/*               t.stateSet.forEach((k) ->	{
            	if(x.getLabel() == k.getLabel())
            		b
            });*/
/*                
            Transition p = new Transition();
            Transition p1 = new Transition();
            Transition p2 = new Transition();
            Transition p3 = new Transition();
            
            int countU=0, countR= 0;
            
            
            countR = ( x.getLabel().split("Run", -1).length ) - 1; 
            countU = ( x.getLabel().split("InUsePr", -1).length ) - 1; 
            
            p.getSourceState().appendState(x);
            p1.getSourceState().appendState(x);
            p2.getSourceState().appendState(x);
            p3.getSourceState().appendState(x);             
            
            /*if( x.getLabel().contains("Avail") || x.getLabel().contains("InUse") )  {
                if(countR==countU)    {
                    p.getSourceState().appendState(x);
                    p1.getSourceState().appendState(x);
                    p2.getSourceState().appendState(x);
                    p3.getSourceState().appendState(x);
                }
            } else  {
                p.getSourceState().appendState(x);
                p1.getSourceState().appendState(x);
                p2.getSourceState().appendState(x);
                p3.getSourceState().appendState(x);
            }*/
/*              
        if(!p.getSourceState().getLabel().isEmpty())    {
        	
            String substrI = i.getTimedAction().getSymbol().substring(0,4);
            String substrJ = j.getTimedAction().getSymbol().substring(0,4);

            //THIS CONDITION MUST BE REVISED
            if(i.getTimedAction().getSymbol().contains("acquirePr") && substrJ.contains("acqu") 
            	|| i.getTimedAction().getSymbol().contains("acquirePr") && substrJ.contains("abor")
            	|| i.getTimedAction().getSymbol().contains("releasePr") && substrJ.contains("abor")
         		|| i.getTimedAction().getSymbol().contains("releasePr") && substrJ.contains("rele"))	{

            	p.getTimedAction().setSymbol(j.getTimedAction().getSymbol());
            	p.getTimedAction().setElapse(j.getTimedAction().getElapse());

                p.setClockResets(i.getClockResetS());
                
                p.getGuard().addAll(i.getGuard());
                p.getGuard().addAll(j.getGuard());
                /*for(ClockConstraint g:i.getGuard())
                    //if(!p.getGuard().contains(g))   {
                        //if(g.getDiffBound().getBound()<0 && (!p.getDestinationState().getLabel().contains("Err")))
                     //       continue;
                        p.getGuard().add(g);         
                    }
            	for(ClockConstraint g:j.getGuard())
                //if(!p.getGuard().contains(g))   {
                    //if(g.getDiffBound().getBound()<0 && (!p.getDestinationState().getLabel().contains("Err")))
                 //       continue;
                    p.getGuard().add(g);         
                }*/
/*        
                State y = new State(i.getDestinationState());
                y.appendState(j.getDestinationState());
                p.getDestinationState().appendState(y);
                //addValidTransition(t.transitions, p);
                t.transitions.add(p);
                System.out.println("VALID p: "+p.toString());
                //System.out.println("Trans1 i: "+i.toString());
                //System.out.println("Trans1 j: "+j.toString());
                //System.out.println("Trans1: "+p.toString());
            } else  {
            	//if(i.getTimedAction().getSymbol().contains("Pr"))	{
            	//	p1.getTimedAction().setSymbol(j.getTimedAction().getSymbol());
            	//	p1.getTimedAction().setInstance(j.getTimedAction().getElapse());
            	//} else {
            	p1.getTimedAction().setSymbol(i.getTimedAction().getSymbol());
            	p1.getTimedAction().setElapse(i.getTimedAction().getElapse());
            	//}
                p1.setClockResets(i.getClockResetS());
                /*for(ClockConstraint g:i.getGuard())
                    if(!p1.getGuard().contains(g))  {
                        if(g.getDiffBound().getBound()<0 && (!p1.getDestinationState().getLabel().contains("Err")))
                            continue;
                        p1.getGuard().add(g);
                    }*/
                //p1.setGuard(i.getGuard());
  /*              p1.getGuard().addAll(i.getGuard());
                
                
                State y1 = new State(i.getDestinationState());
                y1.appendState(j.getSourceState());
                p1.getDestinationState().appendState(y1);
                
                //System.out.println("Trans2 i: "+i.toString());
                //System.out.println("Trans2 j: "+j.toString());
//              System.out.println("Trans2: "+p1.toString());
                //if(!p1.getTimedAction().getSymbol().contains("releasePr"))
                addValidTransition(t.transitions, p1);
                System.out.println("VALID p1: "+p1.toString());
                //t.transitions.add(p1);
                                   
                
                
                p2.getTimedAction().setSymbol(j.getTimedAction().getSymbol());
                p2.getTimedAction().setElapse(j.getTimedAction().getElapse());
                p2.setClockResets(j.getClockResetS());
                p2.getGuard().addAll(j.getGuard());
                
                /*p2.setGuard(j.getGuard());
                for(ClockConstraint g:j.getGuard())
                    if(!p2.getGuard().contains(g))  {
                        if(g.getDiffBound().getBound()<0 && (!p2.getDestinationState().getLabel().contains("Err")))
                            continue;
                        p2.getGuard().add(g);
                    }*/
/*                    State z = new State(i.getSourceState());
                //System.out.println("Trans3 i: "+i.toString());
                //System.out.println("Trans3 j: "+j.toString());
                //System.out.println("Trans3: "+p2.toString());
                z.appendState(j.getDestinationState());                                 
                p2.getDestinationState().appendState(z);
               //if(!p2.getTimedAction().getSymbol().contains("releasePr"))
                //t.transitions.add(p2);
                addValidTransition(t.transitions, p2);
                System.out.println("VALID p2: "+p2.toString());
                }    
            }
        //System.out.println();
        });  
    });
    
    
    ArrayList<State> termS = new ArrayList<>();
    for(State x: other.getStateSet())	{
    	if(x.getLabel().contains("Term"))	{
    		termS.add(x);
    		//System.out.println(x.getLabel());
    	}
    }
    
    a.transitions.forEach((j) -> {
    	for(State tm: termS) {

    		Transition p = new Transition();
    		
    		State x = new State(tm);
    		x.appendState(j.getSourceState());
    		p.getSourceState().appendState(x);
    	
    		p.getTimedAction().setSymbol(j.getTimedAction().getSymbol());
    		p.getTimedAction().setElapse(j.getTimedAction().getElapse());
    		p.setClockResets(j.getClockResetS());
    		p.getGuard().addAll(j.getGuard());
    		/*for(ClockConstraint g:j.getGuard())
    			if(!p.getGuard().contains(g))   {
    				if(g.getDiffBound().getBound()<0 && (!p.getDestinationState().getLabel().contains("Err")))
    					continue;
    				p.getGuard().add(g);         
    			}*/                        
/*      		State y = new State(tm);
    		y.appendState(j.getDestinationState());
        
    		p.getDestinationState().appendState(y);
    		if(!p.getTimedAction().getSymbol().contains("acquirePr") 
    				&& !p.getTimedAction().getSymbol().contains("releasePr"))
    			addValidTransition(t.transitions, p);
    		System.out.println("VALID pi: "+p.toString());
		}
    });

    
    
    ArrayList<State> termState = new ArrayList<>();
    for(State x: a.getStateSet())	{
    	if(x.getLabel().contains("Term"))	{
    		termState.add(x);
    		//System.out.println(x.getLabel());
    	}
    }
    
    other.transitions.forEach((j) -> {
    	for(State tm: termState) {

    		Transition p = new Transition();
    		
    		State x = new State(j.getSourceState());
    		x.appendState(tm);
    		p.getSourceState().appendState(x);
    	
    		p.getTimedAction().setSymbol(j.getTimedAction().getSymbol());
    		p.getTimedAction().setElapse(j.getTimedAction().getElapse());
    		p.setClockResets(j.getClockResetS());
    		p.getGuard().addAll(j.getGuard());
    		/*for(ClockConstraint g:j.getGuard())
    			if(!p.getGuard().contains(g))   {
    				if(g.getDiffBound().getBound()<0 && (!p.getDestinationState().getLabel().contains("Err")))
    					continue;
    				p.getGuard().add(g);         
    			}*/                        
/*        		State y = new State(j.getDestinationState());
    		y.appendState(tm);
        
    		p.getDestinationState().appendState(y);
    		if(!p.getTimedAction().getSymbol().contains("acquirePr") 
    				&& !p.getTimedAction().getSymbol().contains("releasePr"))
    			addValidTransition(t.transitions, p);
    		System.out.println("VALID pii: "+p.toString());
		}
    });
    
    System.out.println();
    return t;
}
*/


