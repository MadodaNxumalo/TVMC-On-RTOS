/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TimedAutomata;

/**
 *
 * @author Madoda
 */
public class PathRunTransition {
    private PathRunLocation sourceLoc;
    private PathRunLocation destinationLoc;
    private Alphabet action;
    
    public PathRunTransition()  {
        sourceLoc = new PathRunLocation();
        destinationLoc = new PathRunLocation();
        action = new Alphabet();
    }
    
    public PathRunTransition(PathRunLocation s, PathRunLocation d, Alphabet a)  {
        sourceLoc = s;
        destinationLoc = d;
        action = a;
    }
    
    public PathRunLocation getSourceLocation()  {
        return sourceLoc;
    }
    
    public PathRunLocation getDestinationLocation()  {
        return destinationLoc;
    }
    
    public Alphabet getAction()  {
        return action;
    }
    
    
    @Override
    public String toString()    {
        return getSourceLocation().toString() + " ----["+getAction().toString()+"]----> " + getDestinationLocation().toString();
    }
    
}
