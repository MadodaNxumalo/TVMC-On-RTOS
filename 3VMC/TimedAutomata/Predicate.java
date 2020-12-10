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
public class Predicate {
    private String predicate;
    
    private int lhs;
    private int rhs;
    
    @Override
    public boolean equals(Object obj)   {
        return true;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.lhs;
        hash = 67 * hash + this.rhs;
        return hash;
    }
    
    public Predicate(String pred)   {
        this.predicate = pred;
    }
    
    public Predicate()   {
        this.predicate = new String();
    }
   
    
    public Predicate(Predicate other)    {
        this.predicate = other.predicate;
    }
    
    public Predicate lessThan(Predicate other)  {//lessThan(Clock other)
        return null;
    } 
    
    public Predicate lessThanEqualTo(Predicate other)  {
        return null;
    }
    
     public Predicate greaterThan(Predicate other)  {
        return null;
    }
     
    public Predicate greaterThanEqualTo(Predicate other)  {
        return null;
    }
    
    public Predicate andConjunction(Predicate other)  {
        return null;
    }
    
    public String getPredicate() {
        return predicate;
    }
    
    //public Set<Alphabet> unionClocks(Set<Alphabet> other)   {
    //    Alphabet x = this;
    //    other.add(x);
    //    return other;
    //}
    
    @Override
    public String toString()  {
        String s = new String(predicate);
        return s;
    }
}
