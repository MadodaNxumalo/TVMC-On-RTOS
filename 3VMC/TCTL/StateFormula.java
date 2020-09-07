/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTL;

/**
 *
 * @author Madoda
 */

//TCTL Formula Grammer \psi := p| not \psi | psi and \phi | there is[\psi until_{time} \phi] | for all [\psi until_{time} \phi]
//p \in AP,
//\approx \in {<,<=,>,>=}

public class StateFormula {
    StateFormula trueStateFormula() {
        return null;
    }
    
     StateFormula apStateFormula(AtomicProposition ap) {
        return null;
    }
    
    StateFormula andStateFormula(StateFormula other) {
        return null;
    }
    
    StateFormula negStateFormula() {
        return null;
    }
    
    StateFormula existStateFormula(PathFormula path) {
        return null;
    }
    
    StateFormula allStateFormula(PathFormula path) {
        return null;
    }
    
}
