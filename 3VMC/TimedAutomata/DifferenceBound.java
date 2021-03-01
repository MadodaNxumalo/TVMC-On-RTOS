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
public class DifferenceBound {
    private double bound;
    private boolean lessEqualTo; //False if less than, 1 if lessOrEqualTo
    
    public DifferenceBound()    {
        bound = 0.0;
        lessEqualTo = true;
    }
    
    public DifferenceBound(double b, boolean l )    {
        bound = 0.0;
        lessEqualTo = l;
    }
    
    public double getBound()    {
        return bound;
    }
    
    public boolean getLessEqualTo() {
        return lessEqualTo;
    }
    
    public void setBound(double d)  {
        bound = d;
    }
    
    public void setLessEqualTo(boolean b)  {
        lessEqualTo = b;
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
        DifferenceBound o = (DifferenceBound) obj;
        return bound==bound && lessEqualTo==o.lessEqualTo;
    }
    
}
