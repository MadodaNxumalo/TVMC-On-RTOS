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
    
    public Task(Queue<Task> concreteQueue)   {

        double sumDeadline=0, sumWECT=0, sumPeriod=0;
        double leastWCET=concreteQueue.peek().getWCET();
        double leastPeriod=concreteQueue.peek().getPeriod();
        double diffDeadline=100, 
        taskClock=0;
        for(Task i: concreteQueue)   {
            if(taskClock > i.getTaskAutomata().getClocks().get(0).getValue())
                taskClock = i.getTaskAutomata().getClocks().get(0).getValue(); 
            
            sumDeadline = sumDeadline + i.deadline;
            sumWECT = sumWECT + i.wcet;
            sumPeriod = sumPeriod + i.period;
            
            if(leastWCET > i.wcet )
                leastWCET=i.wcet;
            
            if(leastWCET > i.period )
                leastWCET=i.period;
            
            double diffI = i.deadline - i.taskAutomata.getClocks().get(0).getValue();
            //double diffDeadline = x.getDeadline() - minClockValue;
            if(diffDeadline > diffI)
                diffDeadline = diffI;
        }
            
        /*for(Task x :concreteTaskQueue) {    
            if(minDeadline > diffDeadline)
                minDeadline = diffDeadline;
        }*/
        
        setWCET(leastWCET);
        setPeriod(leastPeriod);
        setDeadline(diffDeadline);
        setAbstracTaskAutomata(taskClock);
    }
    
    
    ClockConstraint adjustCC(Queue<Task> conreteQueue, double diff, Clock c)    {
        double cP, diffP;
        for(Task i: conreteQueue)   {
            cP = i.getTaskAutomata().getClockConstraint().get(0).getClock().getValue()-c.getValue();
            diffP=i.getTaskAutomata().getClockConstraint().get(0).getDiffBound().getBound()-diff;
        }
        ClockConstraint ccP = new ClockConstraint();
        return ccP;
    }
    

    
    public void setAbstracTaskAutomata(double cl)   {
        //if(taskAutomata==null)
        taskAutomata = new TimedAutomata();
        
        Clock clock = new Clock(cl, "abstractClock"+label);
        Clock clock2 = new Clock(0.0, "clockZero");
        taskAutomata.getClocks().add(clock);
        
        TimedAction enq = new TimedAction("enqueue"+label, 0.0);   //0
        TimedAction acq = new TimedAction("acquire_r"+label,0.0); //1
        taskAutomata.getTimedAction().add(enq);
        taskAutomata.getTimedAction().add(acq);
        DifferenceBound dbD = new DifferenceBound(wcet, true);
        
        ClockConstraint unknownGuard = new ClockConstraint("unknown", clock, clock2, dbD);
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
                unknownCCs, taskAutomata.getTimedAction().get(0), noDelay); //enq
        taskAutomata.getTransitions().add(initEnq);
        Transition enqPause = new Transition(taskAutomata.getStateSet().get(1), taskAutomata.getStateSet().get(2), //"clock_ti < deadline and e_t(clock_ti) < wect"
                unknownCCs, taskAutomata.getTimedAction().get(1), noDelay);     //acq  //x and y
        taskAutomata.getTransitions().add(enqPause);
        
    }
    
    
    
    public void setTaskAutomata()   {
        
        /*final FormulaFactory f = new FormulaFactory();
        final Variable a = f.variable("A");
        final Variable b = f.variable("B");
        final Literal notC = f.literal("C", false);
        final Formula formula = f.and(a, f.not(f.or(b, notC)));
        */
        //if(!(taskAutomata==null))
        taskAutomata = new TimedAutomata();
        
        Clock clock = new Clock(0.0, "clock"+label);
        Clock clockZero = new Clock(0.0, "clockZero");
        taskAutomata.getClocks().add(clock);
        
        
        TimedAction enq = new TimedAction("enqueue"+label,0.0);   //0
        TimedAction acq = new TimedAction("acquire",0.0); //1
        TimedAction pre = new TimedAction("preempt"+label,0.0);   //2
        TimedAction abo = new TimedAction("abort"+label,0.0);     //3
        TimedAction rel = new TimedAction("release",wcet); //4
        TimedAction req = new TimedAction("request"+label,0.0); //5
        taskAutomata.getTimedAction().add(enq);
        taskAutomata.getTimedAction().add(acq);
        taskAutomata.getTimedAction().add(pre);
        taskAutomata.getTimedAction().add(abo);
        taskAutomata.getTimedAction().add(rel);
        taskAutomata.getTimedAction().add(req);
        
        DifferenceBound dbP = new DifferenceBound(period, true);
        DifferenceBound dbD = new DifferenceBound(deadline, true);
        DifferenceBound dbW = new DifferenceBound(wcet, true);
        DifferenceBound dbDnot = new DifferenceBound(-deadline, false);
        DifferenceBound dbWnot = new DifferenceBound(-wcet, false);
        
        
        ClockConstraint ccPeriod = new ClockConstraint("ccInv", clock, clockZero, dbP);
        ClockConstraint ccDeadline = new ClockConstraint("x=c<D", clock, clockZero, dbD);
        ClockConstraint ccWCET = new ClockConstraint("y=e<W", clock, clockZero, dbW);
        ClockConstraint notCcDeadline = new ClockConstraint("x'=D<c", clock, clockZero, dbDnot);
        ClockConstraint notCcWCET = new ClockConstraint("y'=D>e", clock, clockZero, dbWnot);
        
        taskAutomata.getClockConstraint().add(ccDeadline);  //0
        taskAutomata.getClockConstraint().add(ccWCET);      //1
        taskAutomata.getClockConstraint().add(notCcDeadline);//2
        taskAutomata.getClockConstraint().add(notCcWCET);   //3
        //taskAutomata.getClockConstraint().add(ccPeriod);    //4
        
        //State take a list of clock constraint index for guard, update
        ArrayList<ClockConstraint> noConstraints = new ArrayList<>();
        
        ArrayList<ClockConstraint> invGuard = new ArrayList<>();
        invGuard.add(taskAutomata.getClockConstraint().get(0));
        
        ArrayList<ClockConstraint> xyGuard = new ArrayList<>();
        xyGuard.add(taskAutomata.getClockConstraint().get(0));
        //xyGuard.add(taskAutomata.getClockConstraint().get(2));
        
        ArrayList<ClockConstraint> notXyGuard = new ArrayList<>();
        notXyGuard.add(taskAutomata.getClockConstraint().get(2));
        //notXyGuard.add(taskAutomata.getClockConstraint().get(4));

        
        State init= new State("Init"+label, true, false); //c>T
        State inQ= new State("InQ"+label, false, false);
        State run= new State("Run"+label, invGuard, false,false);
        State term= new State("Term"+label,false,false);
        State err= new State("Err"+label,false,true);
        
        
        taskAutomata.getStateSet().add(init);   //0
        taskAutomata.getStateSet().add(inQ);    //1
        taskAutomata.getStateSet().add(run);    //2
        taskAutomata.getStateSet().add(term);   //3
        taskAutomata.getStateSet().add(err);    //4
        ArrayList<Clock> resets = new ArrayList<>();
        ArrayList<Clock> noResets = new ArrayList<>();
        resets.add(clock);
        
        
        
        //(State source, State destination, ArrayList<ClockConstraint> guard, TimedAction act, ArrayList<Clock> resets)
        //enqueue
        Transition initInq = new Transition(taskAutomata.getStateSet().get(0), taskAutomata.getStateSet().get(1),
                xyGuard, taskAutomata.getTimedAction().get(0), resets); //enq
        taskAutomata.getTransitions().add(initInq);
        
        //abort from queue 
        Transition inqErr = new Transition(taskAutomata.getStateSet().get(1), taskAutomata.getStateSet().get(4),
                notXyGuard, taskAutomata.getTimedAction().get(3), noResets);       //abortQueue //not x
        taskAutomata.getTransitions().add(inqErr);
        
        //dequeu to run
        Transition inqRun = new Transition(taskAutomata.getStateSet().get(1), taskAutomata.getStateSet().get(2), //"clock_ti < deadline and e_t(clock_ti) < wect"
                xyGuard, taskAutomata.getTimedAction().get(1), noResets);     //acq  //x and y
        taskAutomata.getTransitions().add(inqRun);
        
        //terminated 
        Transition runTerm = new Transition(taskAutomata.getStateSet().get(2), taskAutomata.getStateSet().get(3), 
                xyGuard, taskAutomata.getTimedAction().get(4), noResets);     //preem //a and y
        taskAutomata.getTransitions().add(runTerm);
        
        //abort from run
        Transition runErr = new Transition(taskAutomata.getStateSet().get(3), taskAutomata.getStateSet().get(4),
                notXyGuard, taskAutomata.getTimedAction().get(4), noResets);  //abortRunenqTransList.add(enqRun); //not x or not y
        taskAutomata.getTransitions().add(runErr);
        
        //preemption
        Transition runInq = new Transition(taskAutomata.getStateSet().get(2), taskAutomata.getStateSet().get(0),
                xyGuard, taskAutomata.getTimedAction().get(2), noResets);       //rel //x and y
        taskAutomata.getTransitions().add(runInq);
        
        //Transition termInit = new Transition(taskAutomata.getStateSet().get(3), taskAutomata.getStateSet().get(0), 
        //        xyGuard, taskAutomata.getAlphabetSet().get(5), delay2);     //preem //a and y
       // taskAutomata.getTransitions().add(runTerm);
      
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
