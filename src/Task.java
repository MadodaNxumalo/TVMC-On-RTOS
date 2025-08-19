/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.Queue;

/**
 *
 * @author Madoda
 */
public final class Task implements Comparable<Task> {
    private String label;
    private double wcet;
    private double period;
    private double deadline;
    private double occurance;
    private double responseRatio;
    private double etVar;
    private TimedAutomata taskAutomata;
    //private Clock processorClock;
    
    public Task(String s, double w, double p, double d, double o) {
        label = s;
        setWCET(w);
        setPeriod(p);
        setDeadline(d);
        occurance = o;
        if (wcet == 0)
        	responseRatio = 0;
        else
        	responseRatio = (deadline - occurance + wcet)/wcet;
        etVar = 0;
        taskAutomata = new TimedAutomata();
    }
    
    public Task() {
        label = "Default";
        setWCET(0);
        setPeriod(0);
        setDeadline(0);
        etVar = 0;
        occurance = 0;
        if (wcet <= 0)
        	responseRatio = 0;
        else
        	responseRatio = (deadline - occurance + wcet)/wcet;
        taskAutomata = new TimedAutomata();
    }
    
    public Task(Task other) {
        label = other.label;
        wcet = other.wcet;
        period = other.period;
        deadline = other.deadline;
        occurance = other.occurance;
        if (wcet == 0)
        	responseRatio = 0;
        else
        	responseRatio = (deadline - occurance + wcet)/wcet;
        etVar = other.etVar;
        taskAutomata = new TimedAutomata(other.taskAutomata);
    }
    
    
    public Task(Queue<Task> concreteQueue)   {
    	
        double leastWCET= concreteQueue.peek().wcet;
        double leastPeriod = concreteQueue.peek().deadline;
        double leastOccu = concreteQueue.peek().occurance;
        double diffDeadline = 1000, taskClock=0;
        
        for(Task i: concreteQueue)   {
        	
            if(taskClock > i.getTaskAutomata().getClocks().get(0).getValue())
                taskClock = i.getTaskAutomata().getClocks().get(0).getValue(); 
            
            if(leastWCET > i.wcet )
                leastWCET = i.wcet;
            
            if(leastPeriod > i.period )
                leastPeriod = i.period;
            
            if(leastOccu > i.occurance )	
                leastOccu = i.occurance;
            
            
            double diffI = i.deadline - i.taskAutomata.getClocks().get(0).getValue();
            //double diffDeadline = x.getDeadline() - minClockValue;
            if(diffDeadline > diffI)
                diffDeadline = diffI;
        }
        
        label = "Sh";
        setWCET(leastWCET);
        setPeriod(leastPeriod);
        this.setOccurance(leastOccu);
        setDeadline(diffDeadline);
        setAbstracTaskAutomata();
        etVar = 0;
    }
    
    

    

    
    public void setAbstracTaskAutomata()   {
        taskAutomata = new TimedAutomata();
        
        Clock clock = new Clock(0, "abstractClock"+label);
        Clock clock2 = new Clock(0.0, "clockZero");
        //taskAutomata.getClocks().add(clock2);  //0
        taskAutomata.getClocks().add(clock);  //0
        
        TimedAction enq = new TimedAction("enqueue"+label, 0.0, true);   //0
        TimedAction acq = new TimedAction("acquire"+label,0.0, true); //1
        taskAutomata.getTimedAction().add(enq);
        taskAutomata.getTimedAction().add(acq);
        DifferenceBound dbD = new DifferenceBound(wcet, true);
        
//        ClockConstraint unknownGuard = new ClockConstraint("unknown", taskAutomata.getClocks().get(0), clock2, dbD);
//        taskAutomata.getClockConstraint().add(unknownGuard);
        
        ArrayList<ClockConstraint> unknownCCs = new ArrayList<>();
//        unknownCCs.add(taskAutomata.getClockConstraint().get(0));
        
//        State init= new State("Init"+label, false, false); //c>T  0 init 0
        State inQ= new State("InQ"+label, true, false);   //1 init 0
        State pause= new State("Pause"+label,false,true);  //2 paue 1
        
//        taskAutomata.getStateSet().add(init);   //0
        taskAutomata.getStateSet().add(inQ);    //1   //0
        taskAutomata.getStateSet().add(pause);    //2   //1
        
        
        ArrayList<Clock> noDelay = new ArrayList<>();
        noDelay.add(clock);
        
//        Transition initEnq = new Transition(taskAutomata.getStateSet().get(0), taskAutomata.getStateSet().get(1),
//                unknownCCs, taskAutomata.getTimedAction().get(0), noDelay); //enq
//        taskAutomata.getTransitions().add(initEnq);
        Transition enqPause = new Transition(taskAutomata.getStateSet().get(0), taskAutomata.getStateSet().get(1), //"clock_ti < deadline and e_t(clock_ti) < wect"
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
        Clock clock = new Clock(0, "clock"+label);
        Clock clockZero = new Clock(0, "clockZero");
        taskAutomata.getClocks().add(clock); //0
        
        
        TimedAction enq = new TimedAction("enqueue"+label,0.0, false);   //0
        TimedAction acq = new TimedAction("acquire"+label,0.0,false); //1
        TimedAction pre = new TimedAction("preempt"+label,0.0, false);   //2
        TimedAction abo = new TimedAction("abort"+label,0.0, false);     //3
        TimedAction rel = new TimedAction("release"+label,0.0, false); //4
        
        taskAutomata.getTimedAction().add(enq);
        taskAutomata.getTimedAction().add(acq);
        taskAutomata.getTimedAction().add(pre);
        taskAutomata.getTimedAction().add(abo);
        taskAutomata.getTimedAction().add(rel);
        
        //DifferenceBound dbP = new DifferenceBound(period, true);
        //DifferenceBound dbO = new DifferenceBound(occurance, true);
        DifferenceBound dbD = new DifferenceBound(deadline, true);
        DifferenceBound dbW = new DifferenceBound(wcet, true);
        DifferenceBound dbDnot = new DifferenceBound(deadline, true);
        DifferenceBound dbWnot = new DifferenceBound(wcet, true);
        
        
        //ClockConstraint ccPeriod = new ClockConstraint("ccInv", taskAutomata.getClocks().get(0), clockZero, dbP);
        ClockConstraint ccDeadline = new ClockConstraint("x:=c<=D", taskAutomata.getClocks().get(0), clockZero, dbD);
        ClockConstraint ccWCET = new ClockConstraint("y:=e<=W", taskAutomata.getClocks().get(0), clockZero, dbW);
        ClockConstraint notCcDeadline = new ClockConstraint("neg x:=D<c", clockZero, taskAutomata.getClocks().get(0), dbDnot);
        ClockConstraint notCcWCET = new ClockConstraint("neg y=D>e", clockZero, taskAutomata.getClocks().get(0), dbWnot);
        //ClockConstraint ccOccurance = new ClockConstraint("c<=0", taskAutomata.getClocks().get(0), clockZero, dbO);
        
        taskAutomata.getClockConstraint().add(ccDeadline);  	//0
        taskAutomata.getClockConstraint().add(ccWCET);     	 	//1
        taskAutomata.getClockConstraint().add(notCcDeadline);	//2
        taskAutomata.getClockConstraint().add(notCcWCET);   	//3
        //taskAutomata.getClockConstraint().add(ccOccurance);   //4
        
        ArrayList<ClockConstraint> invGuard = new ArrayList<>();
        invGuard.add(taskAutomata.getClockConstraint().get(0)); 
        //InvGuard must be wcet guard, for run to have enough available time run  
        
        ArrayList<ClockConstraint> wcetGuard = new ArrayList<>();
        invGuard.add(taskAutomata.getClockConstraint().get(1));
        
        
        ArrayList<ClockConstraint> deadlineGuard = new ArrayList<>();
        deadlineGuard.add(taskAutomata.getClockConstraint().get(0));
        //xyGuard.add(taskAutomata.getClockConstraint().get(2));
        
        ArrayList<ClockConstraint> notDeadlineGuard = new ArrayList<>();
        notDeadlineGuard.add(taskAutomata.getClockConstraint().get(2));
        //notXyGuard.add(taskAutomata.getClockConstraint().get(4));

        
        State init= new State("Init"+label, false, false); //c>T
        State inQ= new State("InQ"+label, true, false);
//        State run= new State("Run"+label, invGuard, false,false);
        State run= new State("Run"+label, false,false);
        State term= new State("Term"+label,false,false);
        State err= new State("Err"+label,false,true);
        
        
 //       taskAutomata.getStateSet().add(init);   //0 init
        taskAutomata.getStateSet().add(inQ);    //1 inQ 0
        taskAutomata.getStateSet().add(run);    //2 run 1
        taskAutomata.getStateSet().add(term);   //3 term 2
        taskAutomata.getStateSet().add(err);    //4 error 3
        
        
        ArrayList<Clock> resets = new ArrayList<>();
        ArrayList<Clock> noResets = new ArrayList<>();
        resets.add(clock);
    
        //(State source, State destination, ArrayList<ClockConstraint> guard, TimedAction act, ArrayList<Clock> resets)
        //enqueue
//        Transition initInq = new Transition(taskAutomata.getStateSet().get(0), taskAutomata.getStateSet().get(1),
//                xyGuard, taskAutomata.getTimedAction().get(0), resets); //init 0 enq inQ 1
//        taskAutomata.getTransitions().add(initInq);
        
        //abort from queue 
        Transition inqErr = new Transition(taskAutomata.getStateSet().get(0), taskAutomata.getStateSet().get(3),
                notDeadlineGuard, taskAutomata.getTimedAction().get(3), noResets);  //1 inq ---- 4 err     //abortQueue //not x
        taskAutomata.getTransitions().add(inqErr);
        
        //dequeu to run
        Transition inqRun = new Transition(taskAutomata.getStateSet().get(0), taskAutomata.getStateSet().get(1), //"clock_ti < deadline and e_t(clock_ti) < wect"
                deadlineGuard, taskAutomata.getTimedAction().get(1), noResets);   //1 inq ---- 2 run  //acq  //x and y
        taskAutomata.getTransitions().add(inqRun);
        
        //terminated 
        Transition runTerm = new Transition(taskAutomata.getStateSet().get(1), taskAutomata.getStateSet().get(2), 
        		deadlineGuard, taskAutomata.getTimedAction().get(4), noResets);  //2 run --- 3 term   //preem //a and y
        taskAutomata.getTransitions().add(runTerm);
        
        //abort from run
        Transition runErr = new Transition(taskAutomata.getStateSet().get(1), taskAutomata.getStateSet().get(3),
        		notDeadlineGuard, taskAutomata.getTimedAction().get(3), noResets);  //term ---- err //abortRunenqTransList.add(enqRun); //not x or not y
        taskAutomata.getTransitions().add(runErr);
        
        //preemption
        Transition runInq = new Transition(taskAutomata.getStateSet().get(2), taskAutomata.getStateSet().get(1),
               xyGuard, taskAutomata.getTimedAction().get(2), noResets);  //run --- inQ     //rel //x and y
        taskAutomata.getTransitions().add(runInq);
        
        Transition termInit = new Transition(taskAutomata.getStateSet().get(3), taskAutomata.getStateSet().get(0), 
               xyGuard, taskAutomata.getAlphabetSet().get(5), delay2);     //preem //a and y
       taskAutomata.getTransitions().add(runTerm);
      
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
    public void setOccurance(double o){
        deadline = (o >=0) ? o : 0;
    }
    
    public void setEtVar(double o){
        deadline = (o >=0) ? o : 0;
    }
    
    public void setResponseRatio(double o){
    	responseRatio = (o >=0) ? o : 0;
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
     
     public double getEtVar(){
         return etVar;
     }
     public double getOccurance(){
         return occurance;
     }
     
     public double getResponseRatio(){
         return responseRatio;
     }
     
    public TimedAutomata getTaskAutomata()  {
        return taskAutomata;
    } 
     
    
    @Override
    public int compareTo(Task o) {
        return o.deadline < deadline ? 1 : -1;
    }
   /* @Override
    public int compareTo(Task o) {
        return o.occurance < occurance ? 1 : -1;
    }*/
    
    
    @Override
    public String toString() {
        return label+" "+(int)wcet+" "+(int)deadline+" "+(int)period;//+" O: "+occurance;
    }
    //@Override
    //public int compareTo(CustomerOrder o) {
    //    return o.orderId > this.orderId ? 1 : -1;
    //}
}