/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TimedAutomata;
import java.util.*;
/**
 *
 * @author Madoda
 */
public class Alphabet {
    private String alphabet;
    
    public Alphabet(String alphabet)   {
        this.alphabet = alphabet;
    }
    
    public Alphabet()   {
        this.alphabet = new String();
    }
   
    
    public Alphabet(Alphabet other)    {
        this.alphabet = other.alphabet;
    }
    
    public void setAlphabet(String s)   {
        alphabet = s;
    }
    
    public String getAlphabet() {
        return alphabet;
    }
    
    public Set<Alphabet> unionClocks(Set<Alphabet> other)   {
        Alphabet x = this;
        other.add(x);
        return other;
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
        Alphabet o = (Alphabet) obj;
        return alphabet == o.alphabet;
    }

    
    @Override
    public String toString()  {
        return "Alphabet: "+ alphabet;
    }
}
