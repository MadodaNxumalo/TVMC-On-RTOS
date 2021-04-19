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
//A clock constraint is a boolean variable
public class ClockConstraint  {
    
    private String label;
    private Clock clock;
    private Clock clock2;
    DifferenceBound db;
    //private boolean ccEval;
    //private Variable logicVar;
    
    public ClockConstraint(String l,Clock c, Clock c2, DifferenceBound dbound) {
        label = l;
        setClocks(c,c2,dbound);
        
    }
    
    public ClockConstraint() {
        label = new String();
        clock = new Clock();
        clock2 = new Clock();
        db = new DifferenceBound(0.0,true);
    }
    
    public ClockConstraint(ClockConstraint other) {
        label = other.label;
        clock = new Clock(other.clock);
        clock2 = new Clock(other.clock2);
        db = new DifferenceBound(other.db);
    }
    
    public ClockConstraint subSetting(ClockConstraint other)   {
        if(label.equals(other.label))
            if(other.clock.getValue() < clock.getValue())
                return other; 
        //else
        return this;        
    }
    
    public void setClocks(Clock x, Clock y, DifferenceBound dbound)    {
        /*if(dbound.getBound()<0) {
            clock = y;
            clock2 = x;
            double negBound = -1*dbound.getBound();
            db = new DifferenceBound();
            db.setBound(negBound, !dbound.getLessEqualTo()); 
        } else {*/
            clock = x;
            clock2 = y;
            db = new DifferenceBound(dbound);
        //}
    }
    
    public String getLabel()  {
        return label;
    }
    
    public Clock getClock()  {
        return clock;
    }
    
      public Clock getClock2()  {
        return clock;
    }
    
    public DifferenceBound getDiffBound()  {
        return db;
    }
    /*
    ArrayList<ClockConstraint> reduceClockConstraint(ArrayList<ClockConstraint> acc)   {
        ArrayList<ClockConstraint> cc = new ArrayList<>();
        for (ClockConstraint x : acc)
            for (ClockConstraint y : acc)   {
               if(x.equals(y))
                   cc.add(x);
               if(x.clock.equals(y.clock) && x.clock2.equals(y.clock2))
                   if (x.db.getBound()<=y.db.getBound()) 
                        cc.add(x);
                    else 
                       cc.add(y);
            }
            
        return cc;
    }*/
    
    
    @Override
     public String toString()  {
        return clock.toString()+" "+clock2.toString()+" "+db.toString();   
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
        ClockConstraint o = (ClockConstraint) obj;
        return clock.equals(o.clock) && db.equals(o.db); //&& (clock==o.clock);
    }
}