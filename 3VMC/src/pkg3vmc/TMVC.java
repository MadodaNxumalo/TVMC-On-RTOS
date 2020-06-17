/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3vmc;

import java.util.ArrayList;
import java.util.Queue;
import TimedAutomata.*;
/**
 *
 * @author Madoda
 */
public class TMVC {
    private ArrayList<State> pathRun;
    private TemporalLogic property;
    
    
    public boolean exploreStateSpace(TimedAutomata nta, Queue<Task> abstractQueue)  {
        boolean threeValue = true;
        State pauseState=null;
        
        int currentStateIndex = nta.findStateIndexInitial();//Find start state
        
        pathRun.add(nta.getStateState().get(currentStateIndex));
        int currentPathIndex = 0;
        
        while(!abstractQueue.isEmpty() && threeValue==true) {
            for(int i=0; i<=nta.getAlphabetSet().size(); i++)   {
                for(Transition j : nta.getStateState().get(currentStateIndex).getOutTransitions())  
                    if(j.getAction().equals(nta.getAlphabetSet().get(i)))    {
                        int targetStateIndex = nta.takeTransition(currentStateIndex, i, -1); //delay is -1 ==> no change in clock
                        State nextState = nta.getStateState().get(targetStateIndex);
                        threeValue = threeValue && property.satisfy(nextState);
                        pathRun.add(nextState);
                    }
            }
            for(int i=currentPathIndex+1; i<pathRun.size();i++){
                if(pathRun.get(i)== pauseState){
                    saveIncompletePath(pauseState);
                }
                else {
                    //currentState = pathRun.get(i);
                    currentPathIndex = i;
                    currentStateIndex = nta.findStateIndex(pathRun.get(currentPathIndex));
                    break;
                }
            }
        }
        if(threeValue==false)
            return threeValue;
        //else if (abstractQueue.isEmpty() && pathRun.contains(pauseState))
        //    return threeValue;
        else
            return true;//return Unknown;
    }
    
    public void saveIncompletePath(State s){
        
    }
            
    public void printRun()  {
    }
}
//