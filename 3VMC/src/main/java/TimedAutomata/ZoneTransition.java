/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TimedAutomata;

/**
 *
 * @author User
 */
public class ZoneTransition {
    //We build a transition system whose states are zones.
    private Zone source;
    private Zone target;
    private TimedAction action;
    //private ClockConstraint guard;
    
    public ZoneTransition()    {
        source = new Zone();
        target = new Zone();
        action = new TimedAction();
    }
    
    public ZoneTransition(Zone sZL, Zone tZL, TimedAction a)    {
        source = sZL;
        target = tZL;
        action = a;
    }
  
    
    public Zone getSourceZone()   {
        return source;
    }
    
    public Zone getTargetZone()   {
        return target;
    }
    
    
}
