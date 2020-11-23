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
    private TemporalLogic property;
    
    public TMVC() {
        pathRun = new ArrayList<>();
        abstractPathRun = new ArrayList<>();
        property = new TemporalLogic(); 
    }
    
    public void addLogic(TemporalLogic propertyTctl)    {
        property = propertyTctl;
    }
    
    
    public int threeV_Checker(TimedAutomata nta, Queue<Task> abstractQueue)    {
       //ArrayList<PathRunLocation> passed = new ArrayList<>();
       ArrayList<PathRunLocation> wait = new ArrayList<>();
       ArrayList<PathRunLocation> paused = new ArrayList<>();
       //ArrayList<PathRunTransition> runMap = new ArrayList<>();
        //Map<PathRunLocation,PathRunLocation> runMap = new HashMap<PathRunLocation,PathRunLocation>();
       int threeVal = 1;
       PathRunLocation currentLocation = new PathRunLocation(nta.getStateSet().get(0), nta.getClocks());
       wait.add(currentLocation);
       //pathRun.add(initialLocation);
       
       System.out.println(currentLocation.getPathState().toString());
       //nta.print();
       
       while(!wait.isEmpty())   {
            currentLocation = wait.remove(0);
            
            System.out.println(currentLocation.getPathState().toString());
            
            if (!pathRun.contains(currentLocation))  {
                pathRun.add(currentLocation);
                //System.out.println(pathRun.contains(currentLocation));
            }
            
            ArrayList<Transition> targetsOfCurrent = nta.getOutTransition(currentLocation);
            
           /*targetsOfCurrent.forEach((t) -> {
                System.out.println(t.getSourceState().toString()+" ---Target--> "+t.getDestinationState().toString());
           });*/
            
            for(Transition outgoingLocation : targetsOfCurrent)     {
            
                if(!outgoingLocation.getDestinationState().getLabel().contains("Pause")) {
                    PathRunLocation transLocation = nta.descreteTransition(currentLocation, outgoingLocation.getAction());
                    transLocation = nta.delayTransition(transLocation, 0.0);//miniDelay
                
                    //PathRunTransition prt = new PathRunTransition(currentLocation, transLocation, outgoingLocation.getAction());
                    //pathRun.add(prt);
                    
                    //threeVal = threeVal && true;
                    
                    wait.add(transLocation);  
                    //System.out.println(wait.isEmpty() + "No Pause");
                } else  {
                    paused.add(currentLocation);
                    //System.out.println(wait.isEmpty() + "No Pause");
                }
            }
            //System.out.println(wait.contains(currentLocation));
            
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
                PathRunLocation transLocation = nta.descreteTransition(currentLocation, outgoingLocation.getAction());
                //transLocation = nta.delayTransition(transLocation, 0.0);//miniDelay
                
                PathRunTransition prt = new PathRunTransition(currentLocation, transLocation, outgoingLocation.getAction());
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
                    //descreteTransition(PathRunLocation sourceLoc, Alphabet symbol); 
                    
                    
                    //if(outgoingLocation.getPathState().getLabel().contains("Run1"))//equals(runAbstractState))
                    //    waitAbstract.add(outgoingLocation);
                    //else    {
                    
                    //if( (!wait.contains(outgoingLocation)) && (!passed.contains(outgoingLocation)) )
                    /*boolean contains = false;
                    for(PathRunLocation x: passed)    {
                        if(outgoingLocation.getPathState().equals(x.getPathState()))
                            contains = true;
                            //System.out.println("Contains: ");
                    }
                    
                    if(contains == false)   {
                        wait.add(outgoingLocation);
                    }*/
                    //hashSet = new LinkedHashSet<>(wait); 
                    //ArrayList<PathRunLocation> noRepeatsWait = new ArrayList<>(hashSet);
                    //wait = new ArrayList<>(hashSet);
                    
                    //}
                   //double delay = 0;
                   //ArrayList<Clock> locClocks = new ArrayList<>();
                   //locClocks = nta.getClocks();
                   //PathRunLocation outgoingLocation = new PathRunLocation(outgoingState, locClocks); //Take list ofo clock values
               
            /*if(currentLocation.getPathState().getLabel().equals("Err0Err1")) //&& SAT(clockProperty,currentLocation.getClockValuations())
            {   
                for(PathRunLocation waiting: wait) 
                    System.out.println("WAITING: " +waiting.toString());
                System.out.println();
                for(PathRunLocation explored: passed) 
                    System.out.println("EXPLORED: " +explored.toString());
                System.out.println();
            
                return true;
            }
            System.out.println();*/    
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
            for(Alphabet action : nta.getAlphabetSet()) {
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