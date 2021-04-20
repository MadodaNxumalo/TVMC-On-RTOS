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
    
    
    //private Queue<State> stateQueue;
    
     
    
    TimedAutomata(ArrayList<State> s, ArrayList<TimedAction> a, ArrayList<Clock> c, ArrayList<ClockConstraint> cc, ArrayList<Transition> tr){//, Queue<State> q)   {
        stateSet = s;
        alphabet = a;
        clocks = c;
        acc = cc;
        transitions = tr;
        //stateQueue = q;    
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
        
        other.stateSet.forEach((i) -> {
            a.stateSet.forEach((j) -> {
                
                State p = new State(i);
                p = p.appendState(j);
                
                int countU =0, countR = 0;
                countR = ( p.getLabel().split("Run", -1).length ) - 1; 
                countU = ( p.getLabel().split("InUse", -1).length ) - 1; 
                
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
                
                if(i.getLabel().contains("Available") || i.getLabel().contains("InUse") )  {
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
            	
            	//System.out.println("Other: "+i.toString());
            	//System.out.println("A:     "+j.toString());
            	//System.out.println();
            	
                State x = new State(i.getSourceState());
                x.appendState(j.getSourceState());
                
                Transition p = new Transition();
                Transition p1 = new Transition();
                Transition p2 = new Transition();
                
                int countU =0, countR = 0;
                countR = ( x.getLabel().split("Run", -1).length ) - 1; 
                countU = ( x.getLabel().split("InUse", -1).length ) - 1; 
                
                
                if( x.getLabel().contains("Available") || x.getLabel().contains("InUse") )  {
                    if(countR==countU)    {
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
            	
                String substrI = i.getTimedAction().getSymbol().substring(0,4);
                String substrJ = j.getTimedAction().getSymbol().substring(0,4);
                
                if(substrI.equals(substrJ) && substrJ.contains("acqu") || substrI.equals(substrJ) && substrJ.contains("rele"))    {
                    p.getTimedAction().setSymbol(j.getTimedAction().getSymbol());
                    p.getTimedAction().setInstance(j.getTimedAction().getElapse());
                    p.setClockResets(i.getClockResetS());
                    for(ClockConstraint g:i.getGuard())
                        if(!p.getGuard().contains(g))   {
                            if(g.getDiffBound().getBound()<0 && (!p.getDestinationState().getLabel().contains("Err")))
                                continue;
                            p.getGuard().add(g);         
                        }                        
                    State y = new State(i.getDestinationState());
                    y.appendState(j.getDestinationState());
                    p.getDestinationState().appendState(y);
                    addValidTransition(t.transitions, p);

                } else  {
                    p1.getTimedAction().setSymbol(i.getTimedAction().getSymbol());
                    p1.getTimedAction().setInstance(i.getTimedAction().getElapse());
                    p1.setClockResets(i.getClockResetS());
                    for(ClockConstraint g:i.getGuard())
                        if(!p1.getGuard().contains(g))  {
                            if(g.getDiffBound().getBound()<0 && (!p1.getDestinationState().getLabel().contains("Err")))
                                continue;
                            p1.getGuard().add(g);
                        }
                    p1.setGuard(i.getGuard());                 
                    State y1 = new State(i.getDestinationState());
                    y1.appendState(j.getSourceState());
                    p1.getDestinationState().appendState(y1);  
                    addValidTransition(t.transitions, p1);
                    
                    p2.getTimedAction().setSymbol(j.getTimedAction().getSymbol());
                    p2.getTimedAction().setInstance(j.getTimedAction().getElapse());
                    p2.setClockResets(j.getClockResetS());
                    p2.setGuard(j.getGuard());
                    for(ClockConstraint g:i.getGuard())
                        if(!p2.getGuard().contains(g))  {
                            if(g.getDiffBound().getBound()<0 && (!p.getDestinationState().getLabel().contains("Err")))
                                continue;
                            p2.getGuard().add(g);
                        }
                    State z = new State(i.getSourceState());
                    z.appendState(j.getDestinationState());                                 
                    p2.getDestinationState().appendState(z);
                    addValidTransition(t.transitions, p2);
                    }    
                } 
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
        		p.getTimedAction().setInstance(j.getTimedAction().getElapse());
        		p.setClockResets(j.getClockResetS());
        		for(ClockConstraint g:j.getGuard())
        			if(!p.getGuard().contains(g))   {
        				if(g.getDiffBound().getBound()<0 && (!p.getDestinationState().getLabel().contains("Err")))
        					continue;
        				p.getGuard().add(g);         
        			}                        
        		State y = new State(tm);
        		y.appendState(j.getDestinationState());
            
        		p.getDestinationState().appendState(y);
        		if(!p.getTimedAction().getSymbol().contains("releasePr"))
        			addValidTransition(t.transitions, p);
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
        		p.getTimedAction().setInstance(j.getTimedAction().getElapse());
        		p.setClockResets(j.getClockResetS());
        		for(ClockConstraint g:j.getGuard())
        			if(!p.getGuard().contains(g))   {
        				if(g.getDiffBound().getBound()<0 && (!p.getDestinationState().getLabel().contains("Err")))
        					continue;
        				p.getGuard().add(g);         
        			}                        
        		State y = new State(j.getDestinationState());
        		y.appendState(tm);
            
        		p.getDestinationState().appendState(y);
        		if(!p.getTimedAction().getSymbol().contains("releasePr"))
        			addValidTransition(t.transitions, p);
			}
        });
        
        return t;
    }
    
   
    
    void addValidTransition(ArrayList<Transition> t, Transition p)  {
        int countU =0, countR = 0;
        countR = ( p.getDestinationState().getLabel().split("Run", -1).length ) - 1; 
        countU = ( p.getDestinationState().getLabel().split("InUse", -1).length ) - 1;
        
        if( p.getDestinationState().getLabel().contains("Available") || p.getDestinationState().getLabel().contains("InUse") )  {
            if(countR == countU)  {
                //System.out.println(p.toString()+"  P R==U");
                if(!transContains(p,t))
                    t.add(p);
                }
        } else  {
            //System.out.println(p.toString()+"  P R!=U");
                if(!transContains(p,t))
                    t.add(p);
        }
    }
    
    
    
    
    public boolean transContains(Transition tr, ArrayList<Transition> transSet) {
        return transSet.stream().anyMatch(t -> (t.equals(tr)));
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
    
    
    public PathRunLocation descreteTransition(PathRunLocation sourceLoc, TimedAction symbol)  {
        PathRunLocation successorLoc = new PathRunLocation();
        PathRunLocation failLoc = new PathRunLocation();
        clocks = sourceLoc.getClockValuations();
        //NTA.clockValuation must be sourceLoc.clocks
                            //Task x = abstractQueue.peek();
                    //if (outgoingLocation.getAction()=="")   
        for(Transition outTrans: transitions)   {
            if((outTrans.getSourceState()==sourceLoc.getPathState()) && (symbol==outTrans.getTimedAction()) )  {
                
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
                for(ClockConstraint inv: successorLoc.getPathState().getInvariant()) {
                    int x = clocks.indexOf(inv.getClock());
                    if(clocks.get(x).getValue() > inv.db.getBound())  { //Evaluation should be on sourceLoc.getClocks
                        //System.out.println("DT - Invariant CC Fail: " + inv.toString());
                        return failLoc;
                    }
                }
                //System.out.println("DT - Transition Success: ");
                
                successorLoc.setPathState(outTrans.getDestinationState());
                successorLoc.setPathClock(clocks);
                //successorLoc.setPathClock(sourceLoc.getClockValuations());
                break;
            }
        }
        return successorLoc;
    }


    public PathRunLocation takeDescreteTransition(PathRunLocation sourceLoc, Transition outTrans, TimedAction symbol)  {
        PathRunLocation successorLoc = new PathRunLocation();
        PathRunLocation failLoc = new PathRunLocation();
        clocks = sourceLoc.getClockValuations();
        
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
                
            for(ClockConstraint inv: successorLoc.getPathState().getInvariant()) {
                int x = clocks.indexOf(inv.getClock());
                if(clocks.get(x).getValue() > inv.db.getBound())    {//Evaluation should be on sourceLoc.getClocks
                    //System.out.println("DT - Invariant CC Fail: " + inv.toString());
                    return failLoc;
                }
            }
            //System.out.println("DT - Transition Success: ");
                
            successorLoc.setPathState(outTrans.getDestinationState());
            successorLoc.setPathClock(clocks);
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
        	boolean b = false;
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
        	boolean b = false;
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
        	boolean b = false;
            if(outTrans.getSourceState().getLabel().equals(loc.getZoneLocation().getLabel()))   {
            		outLocations.add(outTrans);
            }
        }
        
        return outLocations;
    }
    
    
    //Returns a set of target locations whose source location is loc
    public ArrayList<Transition> getOutTransition(Queue<Task> abstractQ, Queue<Task> localQ, StateZone loc) {
    	
        ArrayList<Transition> outLocations = new ArrayList<>();
        ArrayList<Transition> abortLocation = new ArrayList<>();
        int counter =0, aboCounter = 0;
        for(Transition outTrans: transitions)   {
            if(outTrans.getSourceState().getLabel().equals(loc.getZoneLocation().getLabel()))   {
            	String label=outTrans.getTimedAction().getSymbol().substring(0, 3);
                boolean b = false;
                boolean b2 = false;
                switch(label)
                {
                	case "enq":
                		b = enqueueAction(abstractQ, localQ, outTrans, loc); 	
                		break;
                	case "acq":
                		b = acquireAction(localQ, outTrans);
                		break;
                	case "rel":
                		b= releaseAction(outTrans);
                		break;
                	case "abo":
                		b2 = abortAction(outTrans);
                		break;
                	default:
                		System.out.println("Default ");
                }
                
                if(b)	{
                	counter++;
                	outLocations.add(outTrans);
                }
                if(b2)	{
                	aboCounter++;
                	abortLocation.add(outTrans); 	
                }
            }
        }
        //System.out.println("Counter: "+counter+" "+aboCounter);
        if(counter==0)	
        	for(Transition p: abortLocation)   {
        		outLocations.add(p);
        	}
        
        return outLocations;
    }
    
     public boolean releaseAction(Transition t)	{
      	t.getTimedAction().setCommand(false);
      	if (t.getSourceState().getLabel().contains("InUse"))	{
      		t.getTimedAction().setCommand(true);
      		return true;
      	}
      	t.getTimedAction().setCommand(false);
      	return false;
      }
      
      public boolean abortAction(Transition t)	{
      	t.getTimedAction().setCommand(false);
      	if (t.getSourceState().getLabel().contains("InUse") //&& t.getSourceState().getLabel().contains("Run")
      			)	{
      		t.getTimedAction().setCommand(true); 
      		return true;
      	}
      	t.getTimedAction().setCommand(false);
      	return false;
      }
      
      
      
      public boolean enqueueAction(Queue<Task> p, Queue<Task> q, Transition t, StateZone sz) { //boolean de)	{
        	t.getTimedAction().setCommand(false);
        	
        	if(!p.isEmpty())	{
        		Task ts = p.peek();
        		//System.out.println("Before TRange "+sz.getTimeRange()+ " Occu: "+ts.getOccurance());
        		double diff = ts.getOccurance() - sz.getTimeRange();
        		if(diff > 0)	{
        			System.out.println("Call Again: "+sz.getTimeRange()+ " Occurance: "+ts.getOccurance());
        			sz.setTimeRange(ts.getOccurance());
        			return enqueueAction(p, q, t, sz);
        		}
        		if (t.getTimedAction().getSymbol().contains(ts.getLabel()) ) { 
        				//&& highClock>=p.peek().getOccurance())	{
        			//if(sz.getTimeRange() > ts.getOccurance())
        			ts = p.remove();
        			q.add(ts);
        			t.getTimedAction().setCommand(true);
        			System.out.println("Time Range "+sz.getTimeRange()+ " Occurance: "+ts.getOccurance());
        			//System.out.println("Zone Now is: "+sz.toString());
        			//System.out.println("Added Transition: "+ts.toString()+" Qsize: "+q.size());
        			//de = false;
        			return true;
        		}
        	}
        	t.getTimedAction().setCommand(false);	
        	return false;
        }
        
        public boolean acquireAction(Queue<Task> q, Transition t) {  //, boolean da)	{
        	t.getTimedAction().setCommand(false);
        	if(!q.isEmpty())	{
        		Task ts = q.peek();
        		//System.out.println("Acq: Ppeek: "+ts.getLabel()+" TAct: "+t.getTimedAction().getSymbol());
        		if (!t.getTimedAction().getSymbol().contains(ts.getLabel()))
        			q.remove();
        		if (t.getSourceState().getLabel().contains("Avail")    //)  	
        			&& t.getTimedAction().getSymbol().contains(ts.getLabel()))	{
        			//q.remove(); 		//sort this q.remove line
        			t.getTimedAction().setCommand(true);
        			//da = false;
        			return true;
        		}
        	}
        	t.getTimedAction().setCommand(false);	
        	return false;
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
  
    
    
    //Returns a set of target locations whose source location is loc
    public ArrayList<Transition> getOutTransition(Queue<Task> p, StateZone loc) {
    	
        ArrayList<Transition> outLocations = new ArrayList<>();
        
        for(Transition outTrans: transitions)   {
        	boolean b = false;
            if(outTrans.getSourceState().getLabel().equals(loc.getZoneLocation().getLabel()))   {
            	//String label=outTrans.getTimedAction().getSymbol().substring(0, 2);
                /*switch(label)
                {
                	case "enq":
                		b = enqueueAction(p, q, outTrans, hiC);
                		break;
                	case "acq":
                		b = acquireAction(q, outTrans);
                		break;
                	case "rel":
                		b= releaseAction(outTrans);
                		break;
                	case "abo":
                		b = abortAction(outTrans);
                		break;
                	//case "pre":
                	//	System.out.println("Case4 ");
                	//	break;	
                	default:
                		System.out.println("Default ");
                }
            	
            	if(b)*/
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


