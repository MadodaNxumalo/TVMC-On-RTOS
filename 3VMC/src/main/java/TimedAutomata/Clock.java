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
public class Clock {
    private double clock;
    private String label; 
    
    public Clock(double c, String l)  {
        clock = c;
        label = l;  
    }
    
    public Clock()  {
        clock = 0.0;
        label = "";  
    }
    
    public Clock(Clock other)  {
        clock = other.clock;
        label = other.label;
    }
    
    public Clock(String s)  {
        clock = 0.0;
        label = s;  
    }

    public double getValue()    {
        return this.clock;
    }
    
    public String getLabel()    {
        return label;
    }
    
    public void setClock(double c)  {
        if (c < 0){
            throw new IllegalArgumentException("Clock value must a positive value:"+c);
        }
        clock = c;
    }
    
    
    
    public void update(double delay)    {
        if (delay < 0){
            throw new IllegalArgumentException("Clock value must be greater than zero:"+delay);
        }
        clock = clock + delay;
    }
    
    public void reset()    {
        clock = 0;
    }
    
    public void print() {
        System.out.println(label+" "+clock);
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
        Clock o = (Clock) obj;
        return label.equals(o.label); //&& (clock==o.clock);
    }
    
    @Override
    public String toString() {
        return " "+label+" "+clock+" ";
    }
}
