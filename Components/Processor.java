/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;


import TimedAutomata.*;

import java.util.ArrayList;


/**
 *
 * @author Madoda
 */

public final class Processor {
    //private Clock processorClock;
    private String label;
    private double quantumSlice;
    private TimedAutomata processorAutomata;
    
    public Processor(double q) {
        setQuantumSlice(q);
        setProcessorAutomata();
    }
    
    private void setQuantumSlice(double x)    {
        quantumSlice = x;
    }
    public TimedAutomata getAutomata()   {
        return processorAutomata;
    }
    public void setProcessorAutomata()   {
        
        processorAutomata = new TimedAutomata();
        
        Clock clock = new Clock(0.0, "Pr");
        //processorAutomata.getClocks().add(clock);
        
        TimedAction acq = new TimedAction("acquirePr", 0.0, true); //0
        TimedAction rel = new TimedAction("releasePr", 0.0, true);  //1
        processorAutomata.getTimedAction().add(acq);
        processorAutomata.getTimedAction().add(rel);
        
        
        //ClockConstraint window = new ClockConstraint("proQ", clock, quantumSlice, false);
        //processorAutomata.getClockConstraint().add(window);
        
        
        ArrayList<ClockConstraint> processingTimeSlice = new ArrayList<>(); //We do not use preemtion
        //processingTimeSlice.add(processorAutomata.getClockConstraint().get(0));
        
        State avail= new State("Available",processingTimeSlice, true, false);
        State use= new State("InUse", processingTimeSlice, false, false);
        
        processorAutomata.getStateSet().add(avail);
        processorAutomata.getStateSet().add(use);
        
        //List of clock to reset in a transition
        ArrayList<Clock> resets = new ArrayList<>();
        ArrayList<Clock> noResets = new ArrayList<>();
        resets.add(clock); 
        //delay2.add(2); 
        
        //(State source, State destination, ArrayList<ClockConstraint> guard, TimedAction act, ArrayList<Clock> resets)
        Transition availUse = new Transition(processorAutomata.getStateSet().get(0), processorAutomata.getStateSet().get(1), 
                processingTimeSlice, processorAutomata.getTimedAction().get(0), noResets);   
        Transition useAvail = new Transition(processorAutomata.getStateSet().get(1), processorAutomata.getStateSet().get(0), 
                processingTimeSlice, processorAutomata.getTimedAction().get(1), resets);      //acquire 
       
        processorAutomata.getTransitions().add(availUse);
        processorAutomata.getTransitions().add(useAvail);
    }

    TimedAutomata getProcessorAutomata()    {
        return processorAutomata;
    }
    
    //public void print() {
        //System.out.println("PROCESSOR ATTRIBITES: "+ quantumSlice);
        //processorAutomata.print();
        //System.out.println();
    //} 
}
