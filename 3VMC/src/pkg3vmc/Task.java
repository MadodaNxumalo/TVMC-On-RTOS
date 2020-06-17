/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3vmc;

import TimedAutomata.Alphabet;
import TCTL.Clock;
import TimedAutomata.ClockConstraint;
import TimedAutomata.State;
import TimedAutomata.TimedAutomata;
import TimedAutomata.Transition;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Madoda
 */
public final class Task {
    private double wcet;
    private double period;
    private double deadline;
    TimedAutomata taskAutomata;
    //private Clock processorClock;
    
    public Task(double w, double p, double d) {
        setWCET(w);
        setPeriod(p);
        setDeadline(d);
        taskAutomata = new TimedAutomata();
    }
    
    public void setAbstractTaskAutomata()   {
        
    }
    
    public void setTaskAutomata()   {
        
        //TimedAutomata tempTask = new TimedAutomata();
        
        Clock clock = new Clock(0.0, "clock_ti");
        taskAutomata.getClocks().add(clock);
        
        Alphabet enq = new Alphabet("enqueue"); //0
        Alphabet acq = new Alphabet("acquire"); //1
        Alphabet pre = new Alphabet("preempt"); //2
        Alphabet abo = new Alphabet("abort");  //3
        Alphabet rel = new Alphabet("release");  //4
        
        taskAutomata.getAlphabetSet().add(enq);
        taskAutomata.getAlphabetSet().add(acq);
        taskAutomata.getAlphabetSet().add(pre);
        taskAutomata.getAlphabetSet().add(abo);
        taskAutomata.getAlphabetSet().add(rel);
        
        List<State> stateList = new ArrayList<>(taskAutomata.getStateState());
        
        ArrayList<Transition> initTransList = new ArrayList<>();
        ArrayList<Transition> enqTransList = new ArrayList<>();
        ArrayList<Transition> runTransList = new ArrayList<>();
        ArrayList<Transition> errTransList = new ArrayList<>();
        
        ClockConstraint ccInv1 = new ClockConstraint(clock, period, ">=");
        ClockConstraint ccGuard1 = new ClockConstraint(clock, deadline, ">=");
        ClockConstraint ccGuard2 = new ClockConstraint(clock, wcet, ">=");
        
        State init= new State("Init",ccInv1, true, true, initTransList); //c>T
        State inQ= new State("InQ",null, false, false, enqTransList);
        State err= new State("Err",null,false,true, errTransList);
        State run= new State("Run",null,false,true, runTransList);
        
        taskAutomata.getStateState().add(init); //0
        taskAutomata.getStateState().add(inQ);  //1
        taskAutomata.getStateState().add(run);  //2
        taskAutomata.getStateState().add(err);  //3
        
        //x= c-e_t <= D cc.
        //y=e_t <= W 
        //Transition(State destination, String guard, String act, String assign)
        
        
        
        Transition initEnq = new Transition(taskAutomata.getStateState().get(1),
                null, "enqueue", "delay = 0"); //enq
        taskAutomata.getStateState().get(0).getOutTransitions().add(initEnq);
        
        Transition enqErr = new Transition(taskAutomata.getStateState().get(3),
                ccGuard1, "abort", "delay = -1");       //abortQueue //not x
        taskAutomata.getStateState().get(1).getOutTransitions().add(enqErr);
        
        Transition enqRun = new Transition(taskAutomata.getStateState().get(2), //"clock_ti < deadline and e_t(clock_ti) < wect"
                ccGuard2, "acquire", "delay = -1");     //acq  //x and y
        taskAutomata.getStateState().get(1).getOutTransitions().add(enqRun);
        
        Transition runEnq = new Transition(taskAutomata.getStateState().get(1), 
                ccGuard2, "preempt", "delay = -1");     //preem //a and y
        taskAutomata.getStateState().get(2).getOutTransitions().add(runEnq);
        
        Transition runErr = new Transition(taskAutomata.getStateState().get(3),
                ccGuard2, "abort", "delay = 0.1"); //abortRunenqTransList.add(enqRun); //not x or not y
        taskAutomata.getStateState().get(2).getOutTransitions().add(runErr);
        
        Transition runInit = new Transition(taskAutomata.getStateState().get(0),
                ccGuard2, "release", "delay = 0.2");       //rel //x and y
        taskAutomata.getStateState().get(2).getOutTransitions().add(runInit);
        
        
        
        
        //tempTask.getTransitions().add(initEnq);
        //tempTask.getTransitions().add(enqErr);
        //tempTask.getTransitions().add(runEnq);
        //tempTask.getTransitions().add(runErr);
        //tempTask.getTransitions().add(runInit);
        
        //return tempTask;
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
