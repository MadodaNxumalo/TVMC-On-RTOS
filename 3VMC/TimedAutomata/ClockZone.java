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
public class ClockZone {
    //A clock zone ' is a set of clock interpretations described by conjunction 
    //of constraints each of which puts a lower or upper bound on a clock or on 
    //dierence of two clocks. If A has k clocks, then the set ' is a convex 
    //set in the k-dimensional euclidean space.
    
    
    final static double INF = 99999.99;
    
    private static Clock zeroClock = new Clock();
    //private ArrayList<ClockConstraint> clockCc;
    private ArrayList<Clock> clocks;
    private static int size;
    private DifferenceBound dbm[][];
    
    public ClockZone()   {
        //zoneLocation = new State();
        size = 0;
        //clockCc = new ArrayList<>();
        clocks.add(zeroClock);
        clocks = new ArrayList<>();
        dbm = new DifferenceBound[0][0];
    }
    
    public ClockZone(ArrayList<Clock> c)   {
        clocks = c;
        size = clocks.size()+1;
        clocks.add(zeroClock);
         clocks.addAll(c);
        dbm = new DifferenceBound[size][size];
        initialZoneDBM();
    }
    
    public ClockZone(ArrayList<Clock> c, ArrayList<ClockConstraint> cc)    {
        clocks = c;
        size = clocks.size()+1;
        clocks.add(zeroClock);
        clocks.addAll(c);
        dbm = new DifferenceBound[size][size];
        createZoneDBM();
        canonicalZoneGraphFW();
    }
    
    public ArrayList<Clock> getClocks()   {
        return clocks;
    }
    
    public void setZoneDBM(DifferenceBound[][] other)   {
        dbm = other;
    }
            
 
    public boolean guardIntersection(ArrayList<ClockConstraint> other)    {
        //For two clock zones ' and  , '^  denotes the intersection of the two zones.
        //for(ClockConstraint cc: other) {
        //ClockZone o = new ClockZone(other, clocks);
        //this.zoneIntersection(o);
        //https://seg.nju.edu.cn/uploadPublication/copyright/ZhaoJianhua_IPL_2005.pdf 
        //INPUT: A canonical DBM MD. It is stored in M initially;
/*A time guard g, which is stored as two arrays:
OUTPUT: Return true if the intersection of D and g is not empty; return false otherwise.
The canonical DBM representing the intersection is in M.
S1: for each inequation of the form xi ≺ c in g (1  i  n) and ≺∈{<,} do
if (c,≺ < M[i, 0]) then M[i, 0] := c,≺
S2: for each inequation of the form xi ≺ c in g (1  i  n and ≺∈{<,}) do
for j := 1 to n do
if (M[j, i]+c,≺ < M[j, 0]) then M[j, 0] := M[j, i]+c,≺
S3: for each inequation of the form c ≺ xi in g (1  i  n and ≺∈{<,}) do
if (−c,≺ < M[0, i]) then M[0, i] := −c,≺
S4: for each inequation of the form c ≺ xi in g (1  i  n and ≺∈{<,}) do
for j := 1 to n do
if (−c,≺ + M[i, j ] < M[0, j ]) then M[0, j ] := −c,≺ + M[i, j ]
S5: for i := 1 to n do
for j := 1 to n do
if (M[i, 0] + M[0, j ] < M[i, j ]) then M[i, j ] := M[i, 0] + M[0, j ]
S6: for i := 0 to n do
if (M[i, i] < 0,) return false;
return true;*/
            
        //for(int i=0; i < size; i++) {
        //ClockZone otherZone = new ClockZone
        //for(ClockConstraint cc: other)
        //    if(clocks.contains(cc.getClock()))  {
        //        ClockConstraint subSetting(ClockConstraint other);
        //     }
        //}


        int i=0, j=0;
        //S1
        for(ClockConstraint o:other)    {
            //if(clocks.contains(o.getClock()))
            i = clocks.indexOf(o.getClock());
            if(i != -1)   {
                if(o.getClock().getValue() < dbm[i][0].getBound() )
                    dbm[i][0].setBound(o.getBound());
            }
        }
        
        //S2
        for(ClockConstraint o:other)    {
            i = clocks.indexOf(o.getClock());
            if(i != -1)   {
            for(j=0; j<size; j++)  {
                if (o.getClock().getValue()+dbm[j][i].getBound()  < dbm[j][0].getBound() )
                    dbm[j][0].setBound(o.getClock().getValue() + dbm[j][i].getBound());
            }
            }
        }
        
        //S3
        for(ClockConstraint o:other)    {
            i = clocks.indexOf(o.getClock());
            if(i != -1)   {
                double x = -1*o.getClock().getValue();
                if(x<dbm[0][i].getBound() )
                    dbm[0][i] = new DifferenceBound();//o
            }
        }
        //S4
        for(ClockConstraint o:other)    {
            for(j=0; j<size; j++)  {
                double x = -1*o.getClock().getValue(); //DEFINE i here
                if (x+dbm[i][j].getBound()  < dbm[0][j].getBound() )
                    dbm[0][j].setBound(x + dbm[i][j].getBound());
            }
        }
        //S5
        for (i=0; i <size; i++)
            for(j=0; j<size; j++)
                if(dbm[i][0].getBound() + dbm[0][j].getBound()  < dbm[i][j].getBound())
                    dbm[i][j].setBound(dbm[i][0].getBound() +dbm[0][j].getBound()) ;
                    
        
        //S6
        for(i=0; i<size; i++)
            if(dbm[i][i].getBound() < 0.0)
                return false;
        return true;
    }
    
    public boolean intersection(ClockZone other)    { 
        return true;
    }
//(1) intersecting D with the time guard g,
//(2) resetting clocks in r,
//(3) time-elapsing, and
//(4) intersecting with the location invariant of l'   
    public boolean zoneIntersection(ClockZone other)    {
       /*(The intersection operation). We define D = D1 ∧ D2. Let D1i,j = (c1,≺1) 
        and D2i,j = (c2,≺2). Then Di,j = (min(c1, c2),≺) where ≺ is defined 
        as follows. 
        ≺=
        ≺1 if c1 < c2,
        ≺2 if c2 < c1,
        ≺1 if c1 = c2 ∧ ≺1=≺2,
        < if c1 = c2 ∧ ≺16=≺2,
        */
       
        for(int i=0; i<size ;i++)
            for (int j=0; j<size; j++)    {
                if(other.dbm[i][j].getBound() < dbm[i][j].getBound())   {
                    dbm[i][j].setBound(other.dbm[i][j].getBound()); //,other.dbm[i][j].getBound());
                    dbm[i][j].setLessEqualTo(other.dbm[i][j].getLessEqualTo());
                }
                if(dbm[i][j].getBound()==other.dbm[i][j].getBound()) //&&
                    if(other.dbm[i][j].getLessEqualTo()==dbm[i][j].getLessEqualTo())
                        dbm[i][j].setLessEqualTo(dbm[i][j].getLessEqualTo());
                    else 
                        dbm[i][j].setLessEqualTo(other.dbm[i][j].getLessEqualTo());
            }
        return true;
    }
    
    public ClockZone reset(ArrayList<Clock> clockResets)    {
        //For a subset  of clocks and a clock zone ', '[ := 0] denotes 
        //the set of clock interpretations [ := 0] for  2 '.
        /*The reset operation assumes that the given matrix D is in canonical 
        form. For each clock xi, reset by this operation, we copy the 0-th 
        column of the DBM to the i-th column, and the 0-th row to the i-th row. 
        The resulting matrix will be in canonical form [Yov98].
        
        (The Reset operation). With the reset operation, the values of clocks 
        can be set to zero. Let λ be the set of clocks that should be reset. 
        We can define D′= D[λ := 0] as follows. D′j,k =
        (0,≤) if xj ∈ λ and xk ∈ λ,
        D0,k if xj ∈ λ and xk 6∈ λ,
        Dj,0 if xj 6∈ λ and xk ∈ λ,
        Dj,k if xj 6∈ λ and xk 6∈ λ
        */
        
        for(int j=0; j<size ;j++)
          for (int k=0;k<size; k++)    {
              boolean containsJ = clockResets.contains(clocks.get(j));
              boolean containsK = clockResets.contains(clocks.get(k));
              if( containsJ && containsK)
                  dbm[j][k].setBound(0);
              else if(containsJ && !containsK)
                  dbm[j][k].setBound(dbm[0][k].getBound());
              else if(!containsJ && containsK)
                  dbm[j][k].setBound(dbm[j][0].getBound());
              //else
                  //dbm[j][k]=dbm[j][k];
          }
        return this;
    }
    
    public ClockZone elapse(double delta)    {
        //For a clock zone ', ' * denotes the set of interpretations  +  
        //for  2 ' and  2 IR.
        /*(The delay operation). Elapsing time means that the upper bounds of 
        the clocks are set to infinity. That is, after that operation 
        ∀x∈X : x − x0 < ∞ holds. Let D′ = D ⇑, then:D′ i,j = ((∞,<) for any 
        i 6= 0 and j = 0,(Di,j ) if i = 0 or j 6= 0
        Lemma 2.3. [BY04] The time elapse operation does not break the canonical
        form of the matrix.
        */
        for(int i=0; i<size ;i++)
          for (int j=0; j<size; j++)    {
              if(i!=0 && j==0)
                  dbm[i][j].setBound(delta); //dbm[i][j].setBound(INF);
              else if(i==0 || j!=0)
                dbm[i][j]=dbm[i][j]; 
          }
        return this;
    }
    

    
    
    public boolean zoneSubset(ClockZone other)    {
        return true;
    }
    
    public boolean isEmptyClockZone(ClockZone other)   {
        //The Dbm D is satisfiable if it represents a nonempty clock zone.
        //Every satisfiable Dbm has an equivalent canonical Dbm.
        //Every satisfiable Dbm has an equivalent canonical Dbm.
        //We use canonical Dbms to represent clock zones. Given a Dbm, using 
        //classical algorithms for computing all-pairs shortest paths, we check 
        //whether the Dbm is satisable, and if so, convert it into a canonical 
        //form. Two canonical Dbms D and D0 are equivalent i Dij = D0 ij 
        //for all 0  i; j  k. This test can be used during the search to 
        //determine if a zone has been visited earlier. The representation 
        //using canonical DBMs supports the required operations of conjunction,
        //*, and  [ := 0] eciently
        return true;
        //e.g. x<4 and x >4 
    }
    
    boolean isRedundantZone(ClockZone other) {
        if(true) 
            return true;
        else 
            return false;
        //e.g. x<3 and x < 4
    }
    
    ZoneGraph clockZoneGraph()  {
        return new ZoneGraph();
    }
    
    private void initialZoneDBM()  {
        for(int i = 0; i<size;i++)
            for(int j = 0; j<size;j++)
                dbm[i][j]=new DifferenceBound();
    }
    
    
    private void createZoneDBM()  {
        //double graph[][] = new double[clockSize][clockSize]; 
        //Suppose the timed automaton A has k clocks, x1; : : : xk. Then a clock
        //zone is represented by a (k + 1)  (k + 1) matrix D.
        //For each i, the entry Di0 gives an upper bound on the clock xi, and the 
        //entry D0i gives a lower bound on the clock xi. For every pair i; j, the 
        //entry Dij gives an upper bound on the difference of the clocks xi and xj.
        
        for(int i = 0; i<size;i++)
            for(int j = 0; j<size;j++)
                //dbm[i][0] = new DifferenceBound(0.0,true);
                if (j==0)
                    if(clocks.get(i).getValue() <= clocks.get(j).getValue())
                        dbm[i][0].setBound(clocks.get(i).getValue());
                    else 
                        dbm[i][0].setBound(INF);
                else if (i==0)
                    if(clocks.get(j).getValue() >= clocks.get(j).getValue())
                        dbm[j][0].setBound(clocks.get(j).getValue());
                    else 
                        dbm[j][0].setBound(INF);
                else if(i==j)
                    dbm[i][j].setBound(0.0);        
                else 
                    dbm[i][j].setBound(INF);
        //For every pair i; j, the entry Dij gives an upper bound on the 
            //difference of the clocks xi and xj.                    
        //return graph;
    }
  // Implementing floyd warshall algorithm
    void canonicalZoneGraphFW() {
        //matrix D, while the matrix D0 is obtained from the matrix D by 
        //tightening" all the constraints. Such a tightening is obtained by 
        //observing that sum of the upper bounds on the clock dierences xi −xj 
        //and xj −xl is an upper bound on the dierence xi − xl (for this 
        //purpose, the operations of + and < are extended to the domain IK of 
        //bounds). Matrices like D0 with tightest possible constraints are 
        //called canonical .
        /*As mentioned before intersection does not preserve canonical form and 
        the best way to put the matrix back on canonical form is to use the 
        Floyd-Warshall algorithm. However, the work in [ZLZ05] presents an 
        algorithm that improves the canonicalization of the matrix after the 
        intersection operation which has a time complexity of O(n2)*/
        int i, j, k;
        for (k = 0; k < size; k++) {
            for (i = 0; i < size; i++) {
                for (j = 0; j < size; j++) {
                    if (dbm[i][k].getBound() + dbm[k][j].getBound() < dbm[i][j].getBound())
                        dbm[i][j].setBound(dbm[i][k].getBound()+dbm[k][j].getBound());
                }
            }
        }
        //printDBM();
    }
    
    /*void specialCanonicalZone() {
        for(int p=0; p<size; p++)
            for(int q=0; q<size; q++)
                if(p==i ||q==i) //what is i
                    for(int k=0; k<size; k++)   
                        dbm[p][q] = min(dbm[p][q], dbm[p][k]+dbm[k][q])
                else
                    for(int k=0; k<size; k++)   
                        if(k!=i)
                            dbm[p][q] = min(dbm[p][q],dbm[k][q])
    }*/
    
    
    public void printDBM() {
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (dbm[i][j].getBound() == INF)
                    System.out.print("INF ");
                else
                System.out.print(dbm[i][j].getBound() + "  ");
            }
            System.out.println();
        }
    }

    void canonicalDBM() {
        /*A zone may be transformed to a weighted graph where the clocks in are 
        *nodes and the atomic constraints are edges labeled with bounds. 
        *A constraint in the form of will be converted to an edge from node
        *to node labeled with , namely the distance from to is bounded by.
`       */
    }
    
    void minimumClosedDBM() {
        
    }
}
