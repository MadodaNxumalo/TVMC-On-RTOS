/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TimedAutomata;

import java.util.ArrayList;

/**
 *
 * @author Madoda
 */
public class PathRunLocation {
    private State location;
    private ArrayList<Clock> clockValues;
    
    public PathRunLocation()   {
        location = new State();
        clockValues = new ArrayList();
    }
    
    public PathRunLocation(State s, ArrayList<Clock> cV)   {
        location = new State(s); //PathRunLocation(State s, ArrayList<Clock> cV) 
        clockValues = new ArrayList<>();
        clockValues.addAll(cV);
    }
    
    public PathRunLocation(PathRunLocation other)   {
        location = new State(other.location);
        clockValues = new ArrayList<>();
        clockValues.addAll(other.clockValues);
    }
    
    public void setPathState(State s)  {
        location = s;
    }
    
    public void setPathClock(ArrayList<Clock> s)  {
        clockValues = s;
    }

    
    public void setClockValaution(ArrayList<Clock> c)  {
        clockValues = c;
    }
    
    public State getPathState()    {
        return location;
    }
    
    public ArrayList<Clock> getClockValuations()   {
        return clockValues;
    }
    
    @Override
    public String toString()  {
        String result = location.toString();
        for (Clock cV : clockValues)
            result.concat(cV.toString());
        return result;
    }
    
    @Override
    public boolean equals(Object obj){
        //other.equals(other)
        if (obj == this) { 
            return true; 
        } 
        if (obj == null || obj.getClass() != this.getClass()) { 
            return false; 
        }
        PathRunLocation o = (PathRunLocation) obj;
        return location == o.location;
    }
    
}
