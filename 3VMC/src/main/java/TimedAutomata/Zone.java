/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TimedAutomata;

import java.util.ArrayList;

/**
 *
 * @author User
 */
public class Zone {
    //A zone is a pair (s; ') for a location s and a clock zone '.
    //A zone is a clock constraint. Strictly speaking, a zone is the solution 
    //set of a clock constraint, that is the maximal set of clock assignments 
    //satisfying the constraint. It is well-known that such sets can be 
    //efficiently represented and stored in memory as DBMs 
    //(Difference Bound Matrices) [Bel57].
    private State zoneLocation;
    private ClockZone zone;
    //private int kClockCeiling;
    
    
    public Zone()   {
        zoneLocation = new State();
        zone = new ClockZone();
        //kClockCeiling = 0;
    }
    
    public Zone(Zone other)   {
        zoneLocation = new State(other.zoneLocation);
        zone = new ClockZone(other.zone);
        //kClockCeiling = other.kClockCeiling;
    }
    
    public Zone(State s, ClockZone z)   {
        zoneLocation = s;
        zone = z;
        //kClockCeiling = k;
    }
    
    public boolean hasSubset(ArrayList<Zone> others)    {
        for(Zone other:others)  {
            if(! (zoneLocation.equals(other.zoneLocation) && zone.intersection(other.zone)) )
                return false;
        }
        return true;
    }
    
    public ArrayList<Zone> successorZone(ArrayList<Transition> outTrans)    {
        
        //PathRunLocation loc = new PathRunLocation(zoneLocation,zone.getClocks());
        ArrayList<Zone> nextZones = new ArrayList<>();
        //
        
        //System.out.println("Zone NOW: "+.toString());
        
        Zone sourceZone = new Zone(this);
        System.out.println("SOURCE ZONE: "+zoneLocation.toString());
        zone.printDBM();
        
        
        for(Transition tr: outTrans)    {
            Zone sZ = new Zone(sourceZone);
            System.out.println("SZ BEFORE sZ: "+zoneLocation.toString());
            sZ.zone.printDBM();
            
            sZ.invariantZoneCheck(tr.getSourceState().getInvariant(), tr.getTimedAction().getElapse()); 
            //System.out.println("ZONE AFTER INVARIENT: "+zoneLocation.toString());
            //zone.printDBM();
            
            
            sZ = successorZone(tr); 
            
            if (! (sZ.zone.getDBM()[0][0].getBound() == -1.0))    {
                nextZones.add(sZ);
                System.out.println("CONSISTANT ZONE: "+sZ.zoneLocation.toString());
                sZ.zone.printDBM();
            } //else  {
                //System.out.println("SUCCESSOR AFTER Consistant Zone: "+sZ.zoneLocation.toString());
                //sZ.zone.printDBM();
                //
            //}
        }
        return nextZones;
        
    }
    
    public void invariantZoneCheck(ArrayList<ClockConstraint> cc, double m) {
        //delayTransition(PathRunLocation sourceLoc, double delay)
        delayTransition(m);
        zone.elapseUp(m);
        zone.and(cc);
    } 
    
    public void delayTransition(double delay)  {
        zone.getClocks().forEach((c) -> {
            c.update(delay);
        });
    }

    
    public Zone successorZone(Transition edge)    {
        //System.out.println("SUCCESSOR ZONE CALL AND 1:");
        zone.and(edge.getGuard());
        zone.reset(edge.getClockResetS(), 0);
        //System.out.println("SUCCESSOR ZONE CALL AND 22222:");
        zone.and(edge.getDestinationState().getInvariant());
        
        zoneLocation = edge.getDestinationState();
        
        //zone.printDBM();
        
        return this;

//https://www.cmi.ac.in/~sri/Courses/TIME2013/Slides/zones.pdf
        //return (currentZone.getZone() && edge.getTargetState().getInvariant()) && edge.getSourceState().getInvariant() && edge.getGuard()[edge.getClockResetS()]       
        
        
        
    /*Let D be a DBM in canonical form. We want to compute the successor
    of D w.r.t to a transition e = (l, l′, a, λ, φ), let us denote it as succ(D, e).
    The clock zone succ(D, e) can be obtained using a number of elementary DBM
    operations which can be described as follows.
    1. Let an arbitrary amount of time elapse on all clocks in D. In a DBM
    this means all elements Di,0 are set to ∞. We will use the operator ⇑ to
    denote the time elapse operation.
    2. Take the intersection with the invariant of location l to find the set of
    possible clock assignments that still satisfy the invariant.
    3. Take the intersection with the guard φ to find the clock assignments that
    are accepted by the transition.
    4. Canonicalize the resulting DBM and check the consistency of the matrix.
    5. Set all the clocks in λ that are reset by the transition to 0.
    6. Take the intersection with the location invariant of the target location l′.
    7. Canonicalize the resulting DBM.
    8. Extrapolate and canonicalize the resulting zone at the target location l′
    and check the consistency of the matrix.
    Combining all of the above steps into one formula, we obtain
    succ(D, e) = (Canon(Extra(Canon((Canon(((D⇑) ∧ I(l)) ∧ φ)[λ := 0]) ∧ I(l′))))) 
    where Extra represents an extrapolation function that takes as input a DBM
    and returns the M-form of the matrix, while Canon represents a canonicalization
    function that takes as input a DBM and returns a canonicalized matrix in the
    sense that each atomic constraint in the matrix is in the tightest form, I(l) is the
    invariant at location l, and ⇑ denotes the elapse of time operation.*/
    }
    
    public State getZoneLocation()   {
        return zoneLocation;
    }
    
    public ClockZone getZone()   {
        return zone;
    }
    
    @Override
    public String toString()  {
        //zone.printDBM();
        return "Zone ToString: "+zoneLocation.toString();
    }
}
