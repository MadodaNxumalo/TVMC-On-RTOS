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
    private StateZone source;
    private StateZone target;
    private TimedAction action;
    //private ClockConstraint guard;
    
    public ZoneTransition()    {
        source = new StateZone();
        target = new StateZone();
        action = new TimedAction();
    }
    
    public ZoneTransition(StateZone sZL, StateZone tZL, TimedAction a)    {
        source = sZL;
        target = tZL;
        action = a;
    }
  
    
    public StateZone getSourceZone()   {
        return source;
    }
    
    public StateZone getTargetZone()   {
        return target;
    }
    
    
}
