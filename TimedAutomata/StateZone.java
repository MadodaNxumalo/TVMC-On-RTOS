/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TimedAutomata;

import java.util.ArrayList;
import java.util.Queue;

import Components.Task;

/**
 *
 * @author User
 */
public class StateZone {
    //A zone is a pair (s; ') for a location s and a clock zone '.
    //A zone is a clock constraint. Strictly speaking, a zone is the solution 
    //set of a clock constraint, that is the maximal set of clock assignments 
    //satisfying the constraint. It is well-known that such sets can be 
    //efficiently represented and stored in memory as DBMs 
    //(Difference Bound Matrices) [Bel57].
    private State zoneLocation;
    private CZone zone;
    private double range;
    //private int kClockCeiling;
    
    
    public StateZone()   {
        zoneLocation = new State();
        zone = new CZone();
        range = 0;
    }
    
    public StateZone(StateZone other)   {
        zoneLocation = new State(other.zoneLocation);
        zone = new CZone(other.zone);
        range = other.range;
    }
    
    public StateZone(State s, CZone z)   {
        zoneLocation = s;
        zone = z;
        range= 0;
    }
    
    public StateZone(State s, CZone z, double r)   {
        zoneLocation = s;
        zone = z;
        for(int i = 1; i<zone.getDbmSize();i++)
        	zone.getDBM()[i][0].setBound(r, false);
        range= r;
    }
    
    public boolean hasSubset(ArrayList<StateZone> others)    {
        for(StateZone other:others)  {
            if(! (zoneLocation.equals(other.zoneLocation) && zone.intersection(other.zone)) )
                return false;
        }
        return true;
    }
    
    public ArrayList<StateZone> successorZone(ArrayList<Transition> outTrans)    {
    	
        ArrayList<StateZone> nextZones = new ArrayList<>();
        
        StateZone sourceZone = new StateZone(this);
        System.out.println("SOURCE Transitions: "+outTrans.size());
        //zone.printDBM();
        
        
        for(Transition tr: outTrans)    {
        	StateZone sZ = new StateZone(sourceZone);
            //System.out.println("SZ BEFORE sZ: "+zoneLocation.toString());
            //sZ.zone.printDBM();
            
            sZ.invariantZoneCheck(tr.getSourceState().getInvariant(), tr.getTimedAction().getElapse()); 
            //System.out.println("ZONE AFTER INVARIENT: "+zoneLocation.toString());
           // zone.printDBM();
            
            
            sZ = successorZone(tr); 
            
            if (!(sZ.zone.getDBM()[0][0].getBound() < 0))    {
                nextZones.add(sZ);
                System.out.println("CONSISTANT ZONE: "+sZ.zoneLocation.toString());
                sZ.zone.printDBM();
            } 
            //else  {
                //System.out.println("SUCCESSOR AFTER Consistant Zone: "+sZ.zoneLocation.toString());
                //sZ.zone.printDBM();
                //
            //}
        }
        System.out.println("Output ZONE: "+nextZones.size());
        return nextZones;
        
    }
    
    public StateZone successorZone(Transition edge)    {
        //System.out.println("AT GUARD TRANSITION: ");
    	//double miniff = input.peek().getOccurance() - this.range;
    	//System.out.println("Input TRANS: "+edge.toString());
    	//System.out.println("Input ZONE: "+this.toString());
        //zone.printDBM();
        
        zone.and(edge.getGuard());
        zone.reset(edge.getClockResetS(), 0);
        zone.and(edge.getDestinationState().getInvariant());
        zoneLocation = edge.getDestinationState();
        
        
    	//System.out.println("Output ZONE: "+this.toString());
        //zone.printDBM();
        
        return this;
    }
    
    public void invariantZoneCheck(ArrayList<ClockConstraint> cc, double m) {
        for(int i=1; i<zone.getClocks().size();i++)	{
            zone.getClocks().get(i).update(m);            //updateAllClocks(m);
        }
        range = range + m;
        for(ClockConstraint c:cc)   {
            int indC1 = zone.getClocks().indexOf(c.getClock());
            c.setClocks(zone.getClocks().get(indC1), zone.getClocks().get(0), c.getDiffBound());
        }
        //for(ClockConstraint x:cc)
        //    System.out.println("Inv CCs: "+x.toString());
        //System.out.println("AT INVARIANT TRANSITION: ");
        zone.elapseUp(m);
        zone.and(cc);
    } 
    
    

    
    
//https://www.cmi.ac.in/~sri/Courses/TIME2013/Slides/zones.pdf
        //return (currentZone.getZone() && edge.getTargetState().getInvariant()) && edge.getSourceState().getInvariant() && edge.getGuard()[edge.getClockResetS()]       
        
        
        
    /*Let D be a DBM in canonical form. We want to compute the successor
    of D w.r.t to a transition e = (l, lâ€², a, Î», Ï†), let us denote it as succ(D, e).
    The clock zone succ(D, e) can be obtained using a number of elementary DBM
    operations which can be described as follows.
    1. Let an arbitrary amount of time elapse on all clocks in D. In a DBM
    this means all elements Di,0 are set to âˆž. We will use the operator â‡‘ to
    denote the time elapse operation.
    2. Take the intersection with the invariant of location l to find the set of
    possible clock assignments that still satisfy the invariant.
    3. Take the intersection with the guard Ï† to find the clock assignments that
    are accepted by the transition.
    4. Canonicalize the resulting DBM and check the consistency of the matrix.
    5. Set all the clocks in Î» that are reset by the transition to 0.
    6. Take the intersection with the location invariant of the target location lâ€².
    7. Canonicalize the resulting DBM.
    8. Extrapolate and canonicalize the resulting zone at the target location lâ€²
    and check the consistency of the matrix.
    Combining all of the above steps into one formula, we obtain
    succ(D, e) = (Canon(Extra(Canon((Canon(((Dâ‡‘) âˆ§ I(l)) âˆ§ Ï†)[Î» := 0]) âˆ§ I(lâ€²))))) 
    where Extra represents an extrapolation function that takes as input a DBM
    and returns the M-form of the matrix, while Canon represents a canonicalization
    function that takes as input a DBM and returns a canonicalized matrix in the
    sense that each atomic constraint in the matrix is in the tightest form, I(l) is the
    invariant at location l, and â‡‘ denotes the elapse of time operation.*/
    
    
    public State getZoneLocation()   {
        return zoneLocation;
    }
    
    public CZone getZone()   {
        return zone;
    }
    
    public double getTimeRange()   {
        return range;
    }
    
    public void setTimeRange(double tl)	{
    	if(tl>=0)
    		range = tl;
    }
    
    @Override
    public String toString()  {
        //zone.printDBM();
        return "Zone: "+range+" "+zoneLocation.toString();
    }
}
