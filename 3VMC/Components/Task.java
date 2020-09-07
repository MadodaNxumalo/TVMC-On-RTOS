/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

import TimedAutomata.*;
import java.util.ArrayList;
import org.logicng.formulas.*;
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
    
    public void setAbstractTaskAutomata()   {
        
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
        
        
        Alphabet enq = new Alphabet("enqueue"+label); //0
        Alphabet acq = new Alphabet("acquire"+label); //1
        Alphabet pre = new Alphabet("preempt"+label); //2
        Alphabet abo = new Alphabet("abort"+label);  //3
        Alphabet rel = new Alphabet("release"+label);  //4
        taskAutomata.getAlphabetSet().add(enq);
        taskAutomata.getAlphabetSet().add(acq);
        taskAutomata.getAlphabetSet().add(pre);
        taskAutomata.getAlphabetSet().add(abo);
        taskAutomata.getAlphabetSet().add(rel);
        
        //x= c-e_t <= D cc.
        //y=e_t <= W 
        
        ClockConstraint ccInv1 = new ClockConstraint("ccInv", clock, period, "<=");
        ClockConstraint ccGuard1 = new ClockConstraint("ccGuard1", clock, deadline, "<=");
        ClockConstraint ccGuard2 = new ClockConstraint("ccGuard2", clock, wcet, "<=");
        taskAutomata.getClockConstraint().add(ccInv1);
        taskAutomata.getClockConstraint().add(ccGuard1);
        taskAutomata.getClockConstraint().add(ccGuard2);
        
        //State take a list of clock constraint index for guard, update
        ArrayList<ClockConstraint> zeroInd = new ArrayList<>();
        zeroInd.add(taskAutomata.getClockConstraint().get(2));
        ArrayList<ClockConstraint> guard12 = new ArrayList<>();
        guard12.add(taskAutomata.getClockConstraint().get(1));
        guard12.add(taskAutomata.getClockConstraint().get(2));
                
        State init= new State("Init"+label,zeroInd, true, false); //c>T
        State inQ= new State("InQ"+label,zeroInd, false, false);
        State err= new State("Err"+label,zeroInd,false,true);
        State run= new State("Run"+label,zeroInd,false,false);
        
        taskAutomata.getStateState().add(init); //0
        taskAutomata.getStateState().add(inQ);  //1
        taskAutomata.getStateState().add(run);  //2
        taskAutomata.getStateState().add(err);  //3
        
        //ArrayList<Clock> delay2 = new ArrayList<>();
        ArrayList<Clock> noDelay = new ArrayList<>();
        //delay2.add(clock);
        noDelay.add(clock);
        
        
        Transition initEnq = new Transition(taskAutomata.getStateState().get(0), taskAutomata.getStateState().get(1),
                null, taskAutomata.getAlphabetSet().get(0), noDelay); //enq
        taskAutomata.getTransitions().add(initEnq);
        
        Transition enqErr = new Transition(taskAutomata.getStateState().get(1), taskAutomata.getStateState().get(3),
                guard12, taskAutomata.getAlphabetSet().get(3), noDelay);       //abortQueue //not x
        taskAutomata.getTransitions().add(enqErr);
        
        Transition enqRun = new Transition(taskAutomata.getStateState().get(1), taskAutomata.getStateState().get(2), //"clock_ti < deadline and e_t(clock_ti) < wect"
                guard12, taskAutomata.getAlphabetSet().get(1), noDelay);     //acq  //x and y
        taskAutomata.getTransitions().add(enqRun);
        
        Transition runEnq = new Transition(taskAutomata.getStateState().get(2), taskAutomata.getStateState().get(1), 
                guard12, taskAutomata.getAlphabetSet().get(3), noDelay);     //preem //a and y
        taskAutomata.getTransitions().add(runEnq);
        
        Transition runErr = new Transition(taskAutomata.getStateState().get(2), taskAutomata.getStateState().get(3),
                guard12, taskAutomata.getAlphabetSet().get(4), "delay = 0.1"); //abortRunenqTransList.add(enqRun); //not x or not y
        taskAutomata.getTransitions().add(runErr);
        
        Transition runInit = new Transition(taskAutomata.getStateState().get(2), taskAutomata.getStateState().get(0),
                guard12, taskAutomata.getAlphabetSet().get(4), "delay = 0.2");       //rel //x and y
        taskAutomata.getTransitions().add(runInit);
      
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
