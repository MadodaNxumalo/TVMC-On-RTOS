/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTL;

import java.util.Objects;

/**
 *
 * @author Madoda
 */
public class AtomicProposition {
   //private String ap;
    //private char op; //connective
    protected Object lhs;
    protected Object rhs;
    protected boolean value;
    
    @Override
    public boolean equals(Object obj)   {
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.lhs);
        hash = 37 * hash + Objects.hashCode(this.rhs);
        hash = 37 * hash + Objects.hashCode(this.value);
        return hash;
    }
    
    
    
    
}
