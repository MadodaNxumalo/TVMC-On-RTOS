/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3vmc;

import Components.Task;
import java.util.ArrayList;
import java.util.Queue;
import TimedAutomata.*;

/**
 *
 * @author Madoda
 */
public class TMVC {
    private final ArrayList<PathRunLocation> pathRun; //state and time (PathFormula/StateFormula)
    private final ArrayList<PathRunLocation> abstractPathRun;
    private final ArrayList<Zone> pathRunZone; 
            
    public TMVC() {
        pathRun = new ArrayList<>();
        abstractPathRun = new ArrayList<>();
        pathRunZone = new ArrayList<>();
    }

    
    public int threeVReachability(TimedAutomata nta, Queue<Task> abstractQueue, ArrayList<Zone> passed)    {//ADD Zone Graph for passed tasks
        ArrayList<Zone> wait = new ArrayList<>();
        ArrayList<Zone> paused = new ArrayList<>();
        
        //ClockZone errorZone = new ClockZone(nta.getClockConstraint(), nta.getClocks());//ClockZone(ArrayList<ClockConstraint> cc, ArrayList<Clock> c) 
        ClockZone initialConstraint = new ClockZone(nta.getClocks());
        
        Zone initialZone = new Zone(nta.getStateSet().get(0), initialConstraint);
        System.out.println("Initial Zone Now Is:  "+initialZone.getZoneLocation().toString());
        //if(!passed.isEmpty())
        wait.add(initialZone);
        
        while(!wait.isEmpty())    {
            
            
            Zone currentZone = wait.remove(0); //get (l;D) from Waiting
            System.out.println("Current Zone Now Is:  "+currentZone.getZoneLocation().toString());
            //ArrayList<ClockConstraint> not;
            
            //if (currentZone.getZoneLocation().equals(nta.getStateSet().get(4)) && 
                    //(currentZone.getZone().zoneIntersection( new ClockZone(nta.getClocks(), nta.getStateSet().get(4).getInvariant()))) )
            //if(currentZone.getZoneLocation().isFinalState())    {
            //    System.out.println("Error Zone Entered:  "+currentZone.getZoneLocation().toString());
            //    return 0; //if (l;D) j= ' then return \YES"
            //}
            //System.out.println("Successor Zones: "+currentZone.getZoneLocation().toString());
            //currentZone.getZone().printDBM();
            //if(!pathRunZone.contains(currentZone))   { //if (! pathRunZone_i.getZoneCc().subset(currentZone.getZoneCc()))
                //if D 6 D0 for all (l;D0) 2 Passed
                pathRunZone.add(currentZone); //add (l;D) to Passed
                //System.out.println("Current Zone Added: "+currentZone.getZoneLocation().toString());
                //Succ:=f(ls;Ds) : (l;D)_k (ls;Ds) \land Ds != \emptyset;
                ArrayList<Zone> succZones = currentZone.successorZone(nta);
                //System.out.println("Successor Zonne "+succZones.size());
                
                for(int i=0; i<succZones.size(); i++)   {
                    //System.out.println("Successor Zones: "+succZones.get(i).getZoneLocation().toString());
                    //succZones.get(i).getZone().printDBM();
                    wait.add(succZones.get(i));
                }
                
            //} 
            
            /*for(Zone passed: pathRunZone) {
                if( currentZone.getZoneCc().zoneSubset(przL.getZoneCc()) )    {
                    pathRunZone.add(currentZone);
                    
                    for(int i=0; i<uncoveredZones.size() || i<k; i++)      
                        wait.add(uncoveredZones.get(i));
                }
            }*/
            /*    if(!pathRunZone.contains(currentZone.getZoneCc()))  {
                    pathRunZone.add(currentZone);
                    ArrayList<ZoneLocation> nextZonesOfCurrent = zoneDbm.getOutTransition(currentZone, k);
                    for(ZoneLocation childZone: nextZonesOfCurrent)
                    wait.add(childZone);
                }*/
            
       } 
       System.out.println("Empty Queue Now  "+wait.isEmpty()+" IS EMPTY");
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
                //    action.setReadInstance(nta.getClockConstraint().get(1).getBound()); //task deadline
                    
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
            
            //if(find(currentLocation.getPathState().isFinalState() && zonePirme))    {
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
            
            /*
                    
                 //Task x = abstractQueue.remove();
            //form a subset of all legal transitions with source state s, readng some action a
            //ArrayList<Transition> currentStateBranches = nta.currentStateTransitions(currentLocation.getPathState());        
            //for(Transition j: currentStateBranches) {
                //for(Alphabet i: nta.getAlphabetSet())   {
                //for(Transition j : nta.getStateState().get(currentStateIndex).getOutTransitions()) 
                //for(Transition j : nta.getTransitions() &&)
                //if(j.getAction().equals(nta.getAlphabetSet().get(i)))    {
                    //if(j.getAction().equals(i))    {
                        //int targetStateIndex = nta.takeTransition(currentStateIndex, i, -1); //delay is -1 ==> no change in clock
                int targetStateIndex;
                if(j.getAction().getAlphabet().equals("init"))
                    targetStateIndex = nta.stateSwitchRun(currentLocation, j, abstractQueue.peek().getWCET());//stateSwitchRun(currentStateIndex, i, delay)
            else 
                    targetStateIndex = nta.stateSwitchRun(currentLocation, j, 0);
                        
                //PathRunLocation nextLocation = new PathRunLocation(nta.getStateState().get(targetStateIndex), nta.getClocks());
                //threeValue = threeValue && property.satisfy(nextLocation);
                //pathRun.add(nextLocation);
                    //}
                //}
            }
            /*
            for(int i=1+currentStateIndex; i<pathRun.size();i++){
                if(pathRun.get(i).getPathState()== pauseState){
                    saveIncompletePath(pauseState, currentLocation);
                }
                else {
                    //currentState = pathRun.get(i);
                    currentStateIndex = i;
                    currentStateIndex = nta.findStateIndex(pathRun.get(currentStateIndex).getPathState());
                    break;
                }
            }*/
                
        
   
    
    public void saveIncompletePath(State s, PathRunLocation lastLocation){
        
    }
            
    public void printRun()  {
    }
}
//