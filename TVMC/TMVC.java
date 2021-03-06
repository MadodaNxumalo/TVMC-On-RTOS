/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TVMC;

import Components.Processor;
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
    private final ArrayList<PathRunLocation> pathRun; //state and time (PathFormula/StateFormula)
    private final ArrayList<PathRunLocation> abstractPathRun;
    private final ArrayList<StateZone> pathRunZone; 
    public double timeline; 
            
    public TMVC() {
    	timeline = 0.0;
        pathRun = new ArrayList<>();
        abstractPathRun = new ArrayList<>();
        pathRunZone = new ArrayList<>();
    }
    
    
    
    
  public int threeVReachability(TimedAutomata nta, Queue<Task> abstractQ)    {//abstractQ-->abstractBuffer
	    
	  	System.out.println("*****************Iteration Starts**************");  
        ArrayList<StateZone> wait = new ArrayList<>();
        ArrayList<StateZone> paused = new ArrayList<>();
        ArrayList<StateZone> passed = new ArrayList<>();
        Queue<Task> localQ = new LinkedList<>();
        CZone initialConstraint = new CZone(nta.getClocks());
        
        StateZone initialZone = new StateZone(nta.getStateSet().get(0), initialConstraint, timeline);
        wait.add(initialZone);
        
        while(!wait.isEmpty())    {
        	StateZone currentZone = wait.remove(0); //get (l;D) from Waiting
        	
        	if (currentZone.getZoneLocation().isFinalState()  			//) 
        		|| currentZone.getZone().getDBM()[0][0].getBound() < 0 )   	
                return 0;
             
        	//(currentZone.getZone().zoneIntersection( new ClockZone(nta.getClocks(), nta.getStateSet().get(4).getInvariant()))) )
           
            boolean y = true;
            for(StateZone x : passed)  //Some old zones are size 2 and new zone is size 1?
                if(!currentZone.getZone().relation(x.getZone()) && currentZone.getZoneLocation().equals(x.getZoneLocation()) )    //if (! pathRunZone_i.getZoneCc().subset(currentZone.getZoneCc()))
                    y = false;
                
            if(y)   {
                pathRunZone.add(currentZone); //add (l;D) to Passed
                passed.add(currentZone);     //Succ:=f(ls;Ds) : (l;D)_k (ls;Ds) \land Ds != \emptyset;
                
                //PathRunLocation currentLoc = new PathRunLocation(currentZone.getZoneLocation(), nta.getClocks());
                //ArrayList<Transition> outTrans = nta.getOutTransition(currentZone); //getOutTransition(PathRunLocation loc, double hiC, Queue<Task> q)
 
                ArrayList<Transition> outTrans = nta.getOutTransition(abstractQ, localQ, currentZone);
                
                for(Transition t:outTrans)  {
                	StateZone sZ = new StateZone(currentZone);
                	sZ.invariantZoneCheck(t.getSourceState().getInvariant(), t.getTimedAction().getElapse());
                    sZ.successorZone(t);
                    
                    timeline = sZ.getZone().getDBM()[1][0].getBound();
                    
                    if(sZ.getZoneLocation().getLabel().contains("Pause"))   {
                        paused.add(sZ);
                    }
                    else //if(t.getTimedAction().getCommand())
                    {  	
                    	System.out.println(t.toString());
                        wait.add(sZ); 
                    }
                    
                  
                }
            }
       }
        
       return 1;
    }
    
  
  
  
  
    
    public int threeV_Checker(TimedAutomata nta, Queue<Task> abstractQueue)    {
        
       ArrayList<PathRunLocation> wait = new ArrayList<>();
       ArrayList<PathRunLocation> paused = new ArrayList<>();  
       int threeVal = 1;
       PathRunLocation currentLocation = new PathRunLocation(nta.getStateSet().get(0), nta.getClocks());
       wait.add(currentLocation);   
       while(!wait.isEmpty())   {
           
            currentLocation = wait.remove(0);
            
            System.out.println("Current Location: "+currentLocation.getPathState().toString()+" SIZE IS "+wait.size());
            
            if (!pathRun.contains(currentLocation))  {
                pathRun.add(currentLocation);
            }
            
            ArrayList<Transition> targetsOfCurrent = nta.getOutTransition(currentLocation);
            //getOutTransition(PathRunLocation loc, double hiC, Queue<Task> q)
            for(int i=0;i<targetsOfCurrent.size();i++)
                System.out.println("Target Locations: "+targetsOfCurrent.get(i).getSourceState().toString()+"--->"+targetsOfCurrent.get(i).getDestinationState().toString());
            
            for(Transition outgoingLocation : targetsOfCurrent)     {
            
                if(!outgoingLocation.getDestinationState().getLabel().contains("Pause")) {
                    TimedAction action = new TimedAction();
                    action.setSymbol(outgoingLocation.getTimedAction().getSymbol());
                    action.setInstance(0);
                    //if(action.getReadSymbol().getAlphabet().contains("acqu"))    
                    //    action.setReadInstance(); //check queue front
                    //if(action.getReadSymbol().gphabet().contains("relea")) 
                    //action.setReadInstance(nta.getClockConstraint().get(1).getBound()); //task deadline
                    
                    PathRunLocation transLocation = nta.takeDescreteTransition(currentLocation, outgoingLocation, action);
                    
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
       ArrayList<PathRunLocation> passed = new ArrayList<>();
       ArrayList<PathRunLocation> wait = new ArrayList<>();
       ArrayList<PathRunLocation> waitAbstract = new ArrayList<>();
       ArrayList<PathRunTransition> runMap = new ArrayList<>();
        //Map<PathRunLocation,PathRunLocation> runMap = new HashMap<PathRunLocation,PathRunLocation>();
       
       PathRunLocation initialLocation = new PathRunLocation(nta.getStateSet().get(0), nta.getClocks());
       wait.add(initialLocation);
       
       while(!wait.isEmpty())   {
            PathRunLocation currentLocation = wait.remove(0);
            
            //if(currentLocation.getPathState().isFinalState() && zone.satisfies(currentLocation))    {
            //    return true;
            //}  
            ArrayList<Transition> targetsOfCurrent = nta.getOutTransition(currentLocation);
   
            for(Transition outgoingLocation : targetsOfCurrent)     {
                PathRunLocation transLocation = nta.descreteTransition(currentLocation, outgoingLocation.getTimedAction());
                //transLocation = nta.delayTransition(transLocation, 0.0);//miniDelay
                
                PathRunTransition prt = new PathRunTransition(currentLocation, transLocation, outgoingLocation.getTimedAction());
                runMap.add(prt);
            } 
            if(!passed.contains(currentLocation))  {
                passed.add(currentLocation);
               
                for(Transition outgoingLocation : targetsOfCurrent) { //check for outgoing state from current state     
                    PathRunLocation outLoc = new PathRunLocation(outgoingLocation.getDestinationState(), outgoingLocation.getClockResetS());
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
        PathRunLocation currentLocation = new PathRunLocation(currentState, nta.getClocks());
        pathRun.add(currentLocation);
        
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
                    PathRunLocation target = nta.descreteTransition(currentLocation, action);
                    target = nta.delayTransition(target, delay);
                    System.out.println("YYYY");
                    System.out.println();
                    //delayTarget = nta.delayTransition(descreteTarget, delay);
                    //PathRunLocation delayTarget = new PathRunLocation();
                    //threeValue = Satatisfied(currentLocation,queueFront.getDeadline()); 
                            
                    //PathRunLocation newLocation = new PathRunLocation(delayTarget.getPathState(), nta.getClocks());
                    PathRunLocation newLocation = target;//new PathRunLocation(target.getPathState(), target.getClockValuations());
                    pathRun.add(newLocation);
                }
            }
            System.out.println(pathRun.indexOf(currentLocation)+" GGGGG "+pathRun.size());
            for(int i=(pathRun.indexOf(currentLocation)+1); i<pathRun.size();i++)   {
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
        else if (abstractQueue.isEmpty() && pathRun.contains(pauseState))
            return threeValue;
        else
            return true;//return Unknown;
    }
    
    
    
}
//