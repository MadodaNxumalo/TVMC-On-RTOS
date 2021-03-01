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



public class ZoneGraph {
    
    //We build a transition system whose states are zones. Consider a zone 
    //(s; ') and a switch e = (s; a;  ; ; s0) of A. Let succ('; e) be the set 
    //of clock interpretations 0 such that for some  2 ', the state (s0; 0) 
    //can be reached from the state (s; ) by letting time elapse and executing 
    //the switch e. That is, the set (s0; succ('; e))describes the successors 
    //of the zone (s; ') under the switch e. The set succ('; e) can be computed 
    //using the three operations on clock zones as follows:
    //succ('; e) = (((' ^ I(s)) *) ^ I(s) ^  )[ := 0]
    
    //A zone automaton has edges between zones (s; ') and (s0; succ('; e)). 
    //For a timed automaton A, the zone automaton Z(A) is a transition system: 
    //states of Z(A)are zones of A, for every initial location s of A, the zone 
    //(s; [X := 0]) is an initial location of Z(A), and for every switch 
    //e = (s; a;  ; ; s0) of A and every clock zone ', there is a transition 
    //((s; '); a; (s0; succ('; e))).
    
    private ArrayList<ZoneTransition> zoneTransitions;
    private ArrayList<Zone> zoneNodes;
    
    public ZoneGraph()  {
        zoneTransitions = new ArrayList<>();
        zoneNodes = new ArrayList<>();
    }
    
    public ZoneGraph(TimedAutomata ta)  {
        //Zone initialZone = new Zone(ta.getStateSet().get(0), new ClockZone(ta.getStateSet().get(0).getInvariant()), 0);
        Zone targetZone = new Zone();
    }
    
    public void createTimedAutomataZoneGraph() {
    
    }
    public ArrayList<Zone> getZoneNodes()   {
        return zoneNodes;
    }
    
    public ArrayList<ZoneTransition> getZoneTransition()   {
        return zoneTransitions;
    }
    
    
    
    public void symbolicTransitions(TimedAutomata ta)    {
    //    Zone initialZone = new Zone(ta.getStateSet().get(0), new ClockZone(ta.getStateSet().get(0).getInvariant()), 0);
    //    Zone targetZone = new Zone();
    }
    
    
    //public ZoneGraph addZoneGraph(ArrayList<ZoneGraph> others)   {
        //TimedAutomata temp = new TimedAutomata();
     //   others.forEach((i) -> {
    //        addZoneGraph(i);
    //    });
    //    return this;
    //}
     
     void normalizedZoneGraph()  {
        
    }
}
