/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCTL;

/**
 *
 * @author Madoda
 *
 **/



public class ThreeValuedLogic {
    private boolean u;
    private boolean pt;
    private boolean pu;
    
    public enum ThruthValue3 {
        T("True"),
        F("False"),
        U("Unknown");
 
        public final String label;
 
        private ThruthValue3(String label) {
            this.label = label;
        }
    }
    
    
    public boolean trueP(Predicate p)  {
        return (pt && (!pu));
    }
    
    public boolean falseP(Predicate p)  {
        return (!pt && (!pu));
    }
    
    public boolean unknownP(Predicate p)  {
        return pu;
    }
    
    public boolean P(Predicate G)  {
        return (pt && (!pu)) || (u && pu);
    }
    
    public boolean notP(Predicate p)  {
        return ((!pt) && (!pu)) || (u && pu);
    }
   /* 
    public ThruthValue3 satG()   {
        if(SAT(Gf)==true)
            return ThruthValue3.T;
        else if (SAT(Gt)==false)
            return ThruthValue3.F;
        else 
            return ThruthValue3.U;
    }*/
}


/*Hi Madoda,
there are not any mature three-valued logic Java classes available, but three-valued logic formulas can be easily represented/reduced to Boolean Logic. For Boolean logic I can recommend the following Java class, which we already used before: https://github.com/logic-ng/LogicNG
Now if you want to represent some three-valued formula in Boolean logic, proceed as follows:
How to represent a three-valued logic formula F as a Boolean formula G:
- For each three-valued predicate/variable p, introduce Boolean predicates pt and pu
- Introduce the special predicate/variable u
- If you want to represent the fact (p = true), then you use pt AND not(pu) in G
- If you want to represent the fact (p = false), then you use not(pt) AND not(pu) in G
- If you want to represent the fact (p = unknown), then you use pu in G
- If you want to represent p (which may result in true, false, or unknown), then you use (pt AND not(pu)) OR (u AND pu) in G
- If you want to represent not(p), then you use (not(pt) AND not(pu)) OR (u AND pu) in G
- Now you have a formula G that is parameterised by u: G(u)
- You can now check the 3-satisfiability value of G(u) as follows:
If SAT(G(false)) =  true, then true
If SAT(G(true)) =  false, then false
Otherwise, unknown
*/