/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    
    public DifferenceBound(double d, boolean b )    {
        bound = d;
        lessEqualTo = b;
    }
    
    public DifferenceBound(DifferenceBound other )    {
        bound = other.bound;
        lessEqualTo = other.lessEqualTo;
    }
    
    public double getBound()    {
        return bound;
    }
    
    public boolean getLessEqualTo() {
        return lessEqualTo;
    }
    
    public void setBound(double d, boolean b)  {
        bound = d;
        lessEqualTo = b;
    }
    
    public void setLessEqualTo(boolean b)  {
        lessEqualTo = b;
    }
    @Override
     public String toString()  {
         
        return lessEqualTo ? "("+bound+" ,<=)" : "("+bound+" ,<)" ;   
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
    
    //@Override
    public boolean lessThan(Object obj){
        if (obj == null || obj.getClass() != this.getClass()) { 
            return false; 
        }
        DifferenceBound o = (DifferenceBound) obj;
        if (lessEqualTo && bound < o.bound)
            return true; 
        return false;
    }
    
}
