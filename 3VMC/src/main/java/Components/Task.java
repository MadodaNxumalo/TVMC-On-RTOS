/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

import TimedAutomata.*;
import java.util.ArrayList;
import java.util.Queue;
//import org.logicng.formulas.*;
/**
 *
 * @author Madoda
 */
public final class Task {
    private String label;
    private double wcet;
    private double period;
    private double deadline;
    private TimedAutomata taskAutomata;
    //private Clock processorClock;
    
    public Task(String s, double w, double p, double d) {
        label = s;
        setWCET(w);
        setPeriod(p);
        setDeadline(d);
        taskAutomata = new TimedAutomata();
    }
    
    public Task(Queue<Task> conreteQueue)   {

        double sumDeadline=0, sumWECT=0, sumPeriod=0;
        double leastWCET= 100, leastPeriod=100, diffDeadline=100, taskClock=0;
        for(Task i: conreteQueue)   {
            sumDeadline = sumDeadline + i.deadline;
            sumWECT = sumWECT + i.wcet;
            sumPeriod = sumPeriod + i.period;
            
            if(leastWCET > i.wcet )
                leastWCET=i.wcet;
            
            if(leastWCET > i.period )
                leastWCET=i.period;
            
            double diffI= i.deadline - i.taskAutomata.getClocks().get(0).getValue();
            if(diffDeadline > diffI)
                diffDeadline = diffI;
        }
        
        setWCET(leastWCET);
        setPeriod(leastPeriod);
        setDeadline(diffDeadline);
        setAbstracTaskAutomata();
    }
    
    
    public void setAbstracTaskAutomata()   {
        if(!(taskAutomata==null))
            taskAutomata = new TimedAutomata();
        
        Clock clock = new Clock(0.0, "clock"+label);
        taskAutomata.getClocks().add(clock);
        
        Alphabet enq = new Alphabet("enqueue"+label);   //0
        Alphabet acq = new Alphabet("acquire_r"+label); //1
        
        ClockConstraint unknownGuard = new ClockConstraint("unknown", clock, deadline, "<=");
        taskAutomata.getClockConstraint().add(unknownGuard);
        
        ArrayList<ClockConstraint> unknownCCs = new ArrayList<>();
        unknownCCs.add(taskAutomata.getClockConstraint().get(0));
        
        State init= new State("Init"+label, true, false); //c>T
        State inQ= new State("InQ"+label, false, false);
        State pause= new State("Pause"+label,false,true);
        
        taskAutomata.getStateSet().add(init);   //0
        taskAutomata.getStateSet().add(inQ);    //1
        taskAutomata.getStateSet().add(pause);    //2
        
        ArrayList<Clock> noDelay = new ArrayList<>();
        noDelay.add(clock);
        
        Transition initEnq = new Transition(taskAutomata.getStateSet().get(0), taskAutomata.getStateSet().get(1),
                null, taskAutomata.getAlphabetSet().get(0), noDelay); //enq
        taskAutomata.getTransitions().add(initEnq);
        
        Transition enqPause = new Transition(taskAutomata.getStateSet().get(1), taskAutomata.getStateSet().get(2),
                unknownCCs, taskAutomata.getAlphabetSet().get(1), noDelay); //enq
        taskAutomata.getTransitions().add(enqPause);
        
    }
    
    
    
    public void setTaskAutomata()   {
        
        /*final FormulaFactory f = new FormulaFactory();
        final Variable a = f.variable("A");
        final Variable b = f.variable("B");
        final Literal notC = f.literal("C", false);
        final Formula formula = f.and(a, f.not(f.or(b, notC)));
        */
        if(!(taskAutomata==null))
            taskAutomata = new TimedAutomata();
        
        Clock clock = new Clock(0.0, "clock"+label);
        taskAutomata.getClocks().add(clock);
        
        
        Alphabet enq = new Alphabet("enqueue"+label);   //0
        Alphabet acq = new Alphabet("acquire_r"+label); //1
        Alphabet pre = new Alphabet("preempt"+label);   //2
        Alphabet abo = new Alphabet("abort"+label);     //3
        Alphabet rel = new Alphabet("release_r"+label); //4
        taskAutomata.getAlphabetSet().add(enq);
        taskAutomata.getAlphabetSet().add(acq);
        taskAutomata.getAlphabetSet().add(pre);
        taskAutomata.getAlphabetSet().add(abo);
        taskAutomata.getAlphabetSet().add(rel);
        
        //x= c-e_t <= D cc.
        //y=e_t <= W 
        
        ClockConstraint ccInv1 = new ClockConstraint("ccInv", clock, period, "<=");
        ClockConstraint ccGuard1 = new ClockConstraint("x=c<D", clock, deadline, "<=");
        ClockConstraint ccGuard2 = new ClockConstraint("y=e<W", clock, wcet, "<=");
        taskAutomata.getClockConstraint().add(ccInv1);
        taskAutomata.getClockConstraint().add(ccGuard1);
        taskAutomata.getClockConstraint().add(ccGuard2);
        
        //State take a list of clock constraint index for guard, update
        ArrayList<ClockConstraint> invGuard = new ArrayList<>();
        invGuard.add(taskAutomata.getClockConstraint().get(2));
        
        ArrayList<ClockConstraint> xyGuard = new ArrayList<>();
        xyGuard.add(taskAutomata.getClockConstraint().get(1));
        xyGuard.add(taskAutomata.getClockConstraint().get(2));
        
        ArrayList<ClockConstraint> NotxyGuard = new ArrayList<>();
        xyGuard.add(taskAutomata.getClockConstraint().get(1));
        xyGuard.add(taskAutomata.getClockConstraint().get(2));

        
        State init= new State("Init"+label,invGuard, true, false); //c>T
        State inQ= new State("InQ"+label, false, false);
        State run= new State("Run"+label,xyGuard,false,false);
        State term= new State("Term"+label,false,true);
        State err= new State("Err"+label,false,true);
        
        
        taskAutomata.getStateSet().add(init);   //0
        taskAutomata.getStateSet().add(inQ);    //1
        taskAutomata.getStateSet().add(run);    //2
        taskAutomata.getStateSet().add(term);   //3
        taskAutomata.getStateSet().add(err);    //4
        ArrayList<Clock> delay2 = new ArrayList<>();
        ArrayList<Clock> noDelay = new ArrayList<>();
        delay2.add(clock);
        noDelay.add(clock);
        
        
        
        
        Transition initEnq = new Transition(taskAutomata.getStateSet().get(0), taskAutomata.getStateSet().get(1),
                null, taskAutomata.getAlphabetSet().get(0), noDelay); //enq
        taskAutomata.getTransitions().add(initEnq);
        
        Transition enqErr = new Transition(taskAutomata.getStateSet().get(1), taskAutomata.getStateSet().get(4),
                NotxyGuard, taskAutomata.getAlphabetSet().get(3), noDelay);       //abortQueue //not x
        taskAutomata.getTransitions().add(enqErr);
        
        Transition enqRun = new Transition(taskAutomata.getStateSet().get(1), taskAutomata.getStateSet().get(2), //"clock_ti < deadline and e_t(clock_ti) < wect"
                xyGuard, taskAutomata.getAlphabetSet().get(1), noDelay);     //acq  //x and y
        taskAutomata.getTransitions().add(enqRun);
        
        Transition runTerm = new Transition(taskAutomata.getStateSet().get(2), taskAutomata.getStateSet().get(3), 
                xyGuard, taskAutomata.getAlphabetSet().get(4), delay2);     //preem //a and y
        taskAutomata.getTransitions().add(runTerm);
        
        Transition runErr = new Transition(taskAutomata.getStateSet().get(2), taskAutomata.getStateSet().get(4),
                NotxyGuard, taskAutomata.getAlphabetSet().get(4), delay2);  //abortRunenqTransList.add(enqRun); //not x or not y
        taskAutomata.getTransitions().add(runErr);
        
        Transition runEnq = new Transition(taskAutomata.getStateSet().get(2), taskAutomata.getStateSet().get(0),
                xyGuard, taskAutomata.getAlphabetSet().get(2), delay2);       //rel //x and y
        taskAutomata.getTransitions().add(runEnq);
      
    }
    
    public void setWCET(double w){
        wcet = (w >=0) ? w : 0;
    }
    public void setPeriod(double p){
        period = (p >=0) ? p : 0;
    }
    public void setDeadline(double d){
        deadline = (d >=0) ? d : 0;
    }
    
    public double getDeadline(){
        return deadline;
    }
    
    public String getLabel(){
        return label;
    }
    
    public double getWCET(){
        return wcet;
    }
     
     public double getPeriod(){
        return period;
    }
     
    public TimedAutomata getTaskAutomata()  {
        return taskAutomata;
    } 
     
    //public void print() {
    //    System.out.println("Task Attributes: "+ wcet+" "+period+" "+deadline);
    //    taskAutomata.print();
    //} 
}
