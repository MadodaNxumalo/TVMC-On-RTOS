/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TVMC;

import Components.Task;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import TimedAutomata.*;

/**
 *
 * @author Madoda
 */
public class TMVC {
    //private final ArrayList<PathRunLocation> pathRun; //state and time (PathFormula/StateFormula)
    //private final ArrayList<PathRunLocation> abstractPathRun;
    private final ArrayList<StateZone> pathRunZone; 
    public double timeline; 
            
    public TMVC() {
    	timeline = 0.0;
        //pathRun = new ArrayList<>();
        //abstractPathRun = new ArrayList<>();
        pathRunZone = new ArrayList<>();
    }
    
    
    
    
  public int threeVReachability(TimedAutomata nta, Queue<Task> abstractQ)    {//abstractQ-->abstractBuffer
	    
	  	System.out.println("*****************Iteration Starts**************");  
        ArrayList<StateZone> wait = new ArrayList<>();
        ArrayList<StateZone> paused = new ArrayList<>();
        ArrayList<StateZone> passed = new ArrayList<>();
        boolean inUse = false;
        //Queue<Task> localQ = new LinkedList<>();
        ClockZone initialConstraint = new ClockZone(nta.getClocks());
        
        
        StateZone readZone = new StateZone(nta.getStateSet().get(0), initialConstraint, timeline);
//        StateZone currentZone = new StateZone(nta.getStateSet().get(0), initialConstraint, timeline);
        wait.add(readZone);
//        int m = abstractQ.size();
//        double dbmValue = readZone.getZone().getDBM()[0][1].getBound();
        while(!wait.isEmpty())    {
        	readZone = wait.remove(0); //get (l;D) from Waiting
//        	System.out.println();      	
        	//if(currentZone.getZoneLocation().getLabel().contains("acq"))
//        	System.out.println(readZone.getZoneLocation().toString());
        	System.out.println("NEW While WAIT LOOP: CurrentZone DBM: ");
        	readZone.getZone().printDBM();
//        	System.out.println("ABSTRACT Queue Size: "+ abstractQ.size());
//        	if(!abstractQ.isEmpty())
//        		System.out.println(" PEEKED:  "+abstractQ.peek().getLabel());
        	
//        	if(m != abstractQ.size())	{
//        		System.out.println("REDUCED ABSTRACT QUEUE:");
//        		m = abstractQ.size();
//        	} 
        	
//        	System.out.println("WAIT SIZE Queue Size: "+ wait.size());
        	if (!readZone.getZoneLocation().getLabel().contains("Err") 
//        		&& dbmValue < readZone.getZone().getDBM()[0][1].getBound() 
        		&& readZone.getZone().getDBM()[0][0].getBound() < 0 	 
        		)	{ 
//        		System.out.println("Iteration Ends - Return 0");
//        		System.out.println("At State: "+readZone.getZoneLocation().getLabel()
//        				+ " Zone Validity: "+ readZone.getZone().getDBM()[0][0].getBound() );
//        		System.out.println(readZone.getZoneLocation().toString());
        		return 0;
        	}
 //       	dbmValue = readZone.getZone().getDBM()[0][1].getBound();
        	//(currentZone.getZone().zoneIntersection( new ClockZone(nta.getClocks(), nta.getStateSet().get(4).getInvariant()))) )
           
        	boolean y = true;
            for(StateZone x : passed)  //Some old zones are size 2 and new zone is size 1?
                if(!readZone.getZone().relation(x.getZone()) && readZone.getZoneLocation().equals(x.getZoneLocation()) )    //if (! pathRunZone_i.getZoneCc().subset(currentZone.getZoneCc()))
                    y = false;
            
//            System.out.println("RELATION: "+y);
            
            if(y)   {
                pathRunZone.add(readZone); //add (l;D) to Passed
                passed.add(readZone);     //Succ:=f(ls;Ds) : (l;D)_k (ls;Ds) \land Ds != \emptyset;
                
                //PathRunLocation currentLoc = new PathRunLocation(currentZone.getZoneLocation(), nta.getClocks());
                //ArrayList<Transition> outTrans = nta.getOutTransition(currentZone); //getOutTransition(PathRunLocation loc, double hiC, Queue<Task> q)
 
                ArrayList<Transition> outTrans = nta.getOutTransition(abstractQ, readZone, 1);
//                System.out.println("Out Transition Size: "+ outTrans.size());
                
                for(Transition t:outTrans)  {
//                	System.out.println(t .toString());	
                	StateZone sZ = new StateZone(readZone);
                	
 //               	System.out.println("ZONE BEFORE CLOCK UPDATE: "+ t.getTimedAction().getElapse()
//               			+" FOR SYMBOL: " + t.getTimedAction().getSymbol());
//                	sZ.getZone().printDBM();
                	
                	sZ.invariantZoneCheck(t.getSourceState().getInvariant(), t.getTimedAction().getElapse());
//                	System.out.println("ZONE BEFORE SUCCESSOR ZONE: ");
//                    sZ.getZone().printDBM();
                	sZ.successorZone(t);
                    
//                    System.out.println("ZONE AFTER SUCCESSOR ZONE CLOCK UPDATES: ");
//                    sZ.getZone().printDBM();
                    
                    timeline = sZ.getZone().getDBM()[1][0].getBound();
                    
                    if(t.getDestinationState().getLabel().contains("Pause"))   {
                    //if(sZ.getZoneLocation().getLabel().contains("Pause"))   {
                        paused.add(sZ);
                        //return 1;  //Shall we??
                    }
                    
                    else //if(!readZone.equals(sZ))
                    {  	
//                    	System.out.println("Transition with WAIT.ADD Zone: ");
//                    	System.out.println(t.toString());
                        wait.add(sZ); 
                    }
 
                }
            }
       }
        //abstractQ.clear();
        System.out.println("MC Iteration Ends - Return 1");
       return 1;
    }
    
  
  
  
  
    
    public int threeV_Checker(TimedAutomata nta, Queue<Task> abstractQueue)    {
        
       ArrayList<StateZone> wait = new ArrayList<>();
       ArrayList<StateZone> paused = new ArrayList<>();  
       int threeVal = 1;
       //StateZone(State s, CZone z, double r)
       //Zone currentZone = new CZone(ArrayList<Clock> c, CZone other)
       ClockZone currentZone = new ClockZone(nta.getClocks()); //nta.getClocks()
       //CZone currentZone = new CZone();
       StateZone currentLocation = new StateZone(nta.getStateSet().get(0), currentZone); // nta.getClocks());
       wait.add(currentLocation);   
       while(!wait.isEmpty())   {
           
            currentLocation = wait.remove(0);
            
            System.out.println("Current Location: "+currentLocation.getZoneLocation().toString()+" SIZE IS "+wait.size());
            
            if (!pathRunZone.contains(currentLocation))  {
                pathRunZone.add(currentLocation);
            }
            
            ArrayList<Transition> targetsOfCurrent = nta.getOutTransition(currentLocation);
            //getOutTransition(PathRunLocation loc, double hiC, Queue<Task> q)
            for(int i=0;i<targetsOfCurrent.size();i++)
                System.out.println("Target Locations: "+targetsOfCurrent.get(i).getSourceState().toString()+"--->"+targetsOfCurrent.get(i).getDestinationState().toString());
            
            for(Transition outgoingLocation : targetsOfCurrent)     {
            
                if(!outgoingLocation.getDestinationState().getLabel().contains("Pause")) {
                    TimedAction action = new TimedAction();
                    action.setSymbol(outgoingLocation.getTimedAction().getSymbol());
                    action.setElapse(0);
                    //if(action.getReadSymbol().getAlphabet().contains("acqu"))    
                    //    action.setReadInstance(); //check queue front
                    //if(action.getReadSymbol().gphabet().contains("relea")) 
                    //action.setReadInstance(nta.getClockConstraint().get(1).getBound()); //task deadline
                    
                    StateZone transLocation = nta.takeDescreteTransition(currentLocation, outgoingLocation, action);
                    //nta.takeDescreteTransition(null, outgoingLocation, action)
                    transLocation = nta.delayTransition(transLocation, 0.0);
                    
                    //if(!(transLocation SAT tempLogic))  {
                    //    threeVal = 0;
                    //    break;
                    //}
                    wait.add(transLocation);    
                } else  {
                    paused.add(currentLocation);
                }
            }
       }

       if(paused.isEmpty())  {
           return -1; //MUST BE return UNKNOWN;
       }
       return threeVal;
    }
    
    
    
    public boolean threeValFwdReachability(TimedAutomata nta, Queue<Task> abstractQueue)    {
       ArrayList<StateZone> passed = new ArrayList<>();
       ArrayList<StateZone> wait = new ArrayList<>();
       ArrayList<StateZone> waitAbstract = new ArrayList<>();
       ArrayList<PathRunTransition> runMap = new ArrayList<>();
        //Map<PathRunLocation,PathRunLocation> runMap = new HashMap<PathRunLocation,PathRunLocation>();
       
       ClockZone currentZone = new ClockZone(nta.getClocks()); //nta.getClocks()
       //CZone currentZone = new CZone();
       StateZone currentLocation = new StateZone(nta.getStateSet().get(0), currentZone);
       
       //StateZone initialLocation = new StateZone(nta.getStateSet().get(0), nta.getClocks());
       wait.add(currentLocation);
       
       while(!wait.isEmpty())   {
    	   currentLocation = wait.remove(0);
            
            //if(currentLocation.getPathState().isFinalState() && zone.satisfies(currentLocation))    {
            //    return true;
            //}  
            ArrayList<Transition> targetsOfCurrent = nta.getOutTransition(currentLocation);
   
            for(Transition outgoingLocation : targetsOfCurrent)     {
            	//StateZone descreteTransition(StateZone sourceLoc, TimedAction symbol)
                StateZone transLocation = nta.descreteTransition(currentLocation, outgoingLocation.getTimedAction());
                //transLocation = nta.delayTransition(transLocation, 0.0);//miniDelay
                
                //PathRunTransition prt = new PathRunTransition(currentLocation, transLocation, outgoingLocation.getTimedAction());
                //PathRunTransition prt = new PathRunTransition(currentLocation, transLocation, outgoingLocation.getTimedAction());
                //runMap.add(prt);
            } 
            if(!passed.contains(currentLocation))  {
                passed.add(currentLocation);
               
                for(Transition outgoingLocation : targetsOfCurrent) { //check for outgoing state from current state
                	//StateZone(State s, CZone z);
                	ClockZone outZone = new ClockZone(outgoingLocation.getClockResetS());
                    StateZone outLoc = new StateZone(outgoingLocation.getDestinationState(), outZone);
                    if(!passed.contains(outLoc) && !wait.contains(outLoc))  {
                       wait.add(outLoc);
                    }
                    else {
                        //Add some content later
                    }
                }                 
           }
       }
        
        //System.out.println("Passed Size: " + passed.size()); 
        System.out.println("NTA Size: " + nta.getStateSet().size());
        wait.forEach((waiting) -> { 
            System.out.println("WAITING: " + waiting.toString());
        });
        System.out.println();
        passed.forEach((explored) -> { 
            System.out.println("EXPLORED: " + explored.toString());
        });
        System.out.println();
       
       if(!waitAbstract.isEmpty())  {
           System.out.println("+++++++++++UNKNOWN RESULT++++++++++++++ ");
           return true; //MUST BE return UNKNOWN;
       }
       return false;
    }
    
    
  //ArrayList<StateZone> sZs = currentZone.successorZone(outTrans);
    //System.out.println("Current Timeline "+sZ.toString());
  	/*String label=t.getTimedAction().getSymbol().substring(0, 3);
      boolean b = false;
      boolean decideEnqueue = true;
      boolean decideAcquire = true;
      switch(label)
      {
      	case "enq":
      		b = enqueueAction(abstractQ, localQ, t, sZ,decideEnqueue); 	
      		break;
      	case "acq":
      		b = acquireAction(localQ, t,decideAcquire);
      		break;
      	case "rel":
      		b= releaseAction(t);
      		break;
      	case "abo":
      		b = abortAction(t);
      		System.out.println("ABORT ZONE: "+t.toString());
      		System.out.println("ABORT ZONE: "+sZ.toString());
      		break;
      	//case "pre":
      	//	System.out.println("Case4 ");
      	//	break;	
      	default:
      		System.out.println("Default ");
      }*/
    //timeline = sZ.getZone().getDBM()[1][0].getBound(); //[1][0] is always available
    //timeline = sZ.getTimeRange();
    //else if(t.getTimedAction().getCommand())	{	
    //	System.out.println(t.toString());
   // 	System.out.println("Zone Waiting: "+sZ.toString());
   // } 
    
    
    
    public boolean exploreStateSpace(TimedAutomata nta, Queue<Task> abstractQueue)  {
        
        State pauseState = new State(); //pauseState must be last state from abstractQueue
        State currentState = nta.getStateSet().get(0);  //Find start state
        //System.out.print("CurrentIndex: " + currentStateIndex);
        //System.out.print(nta.getStateState().get(currentStateIndex).toString());
        nta.print();
        ClockZone currentZone = new ClockZone(nta.getClocks());
        //CZone(ArrayList<Clock> c, CZone other)
        //CZone(ArrayList<Clock> c, ArrayList<ClockConstraint> cc)
        StateZone currentLocation = new StateZone(currentState, currentZone, timeline);
        //StateZone currentLocation = new StateZone(currentState, nta.getClocks());
        pathRunZone.add(currentLocation);
        
        boolean threeValue=true;
        //Delay comes from the word read.
        //int x = 0;
        //while(!abstractQueue.isEmpty() && threeValue==true) {
        while(!abstractQueue.isEmpty() && threeValue==true) {
            Task queueFront = abstractQueue.remove(); //abstractQueue contains the word??
            double delay;             
            for(TimedAction action : nta.getTimedAction()) {
                if (queueFront.getLabel().equals(action))
                    delay = queueFront.getWCET();
                else  
                    delay=0;
                if(nta.checkTransition(currentState, action))  {
                    System.out.println("Cur");
                    StateZone target = nta.descreteTransition(currentLocation, action);
                    target = nta.delayTransition(target, delay);
                    System.out.println("YYYY");
                    System.out.println();
                    //delayTarget = nta.delayTransition(descreteTarget, delay);
                    //PathRunLocation delayTarget = new PathRunLocation();
                    //threeValue = Satatisfied(currentLocation,queueFront.getDeadline()); 
                            
                    //PathRunLocation newLocation = new PathRunLocation(delayTarget.getPathState(), nta.getClocks());
                    StateZone newLocation = target;//new PathRunLocation(target.getPathState(), target.getClockValuations());
                    pathRunZone.add(newLocation);
                }
            }
            System.out.println(pathRunZone.indexOf(currentLocation)+" GGGGG "+pathRunZone.size());
            for(int i=(pathRunZone.indexOf(currentLocation)+1); i<pathRunZone.size();i++)   {
                //System.out.println(pathRun.get(i).getPathState().getLabel()+"GGGGG ");
                /*if(pathRun.get(i).getPathState().getLabel().contains("Run0"))   {//equals(pauseState))    {
                    System.out.println("SAVE INCOMPLETE PATH");
                    abstractPathRun.add(pathRun.get(i));
                    //pathRun.SaveIncompletePath(nta,abstractQueue); //Create method at patRun
                } else {
                    currentLocation = pathRun.get(i);
                    break;
                }*/
            }
        }
        
        if(threeValue==false)
            return threeValue;
        else if (abstractQueue.isEmpty() && pathRunZone.contains(pauseState))
            return threeValue;
        else
            return true;//return Unknown;
    }
    
    
    
}
//