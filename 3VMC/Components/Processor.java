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
        
        Clock clock = new Clock(0.0, "c_resource");
        processorAutomata.getClocks().add(clock);
        
        Alphabet acq = new Alphabet("acquire_r"); //0
        Alphabet rel = new Alphabet("release_r");  //1
        processorAutomata.getAlphabetSet().add(acq);
        processorAutomata.getAlphabetSet().add(rel);
        
        
        ClockConstraint window = new ClockConstraint("proQ", clock, quantumSlice, "<=");
        
        processorAutomata.getClockConstraint().add(window);
        
        ArrayList<ClockConstraint> zeroIndex = new ArrayList<>();
        zeroIndex.add(processorAutomata.getClockConstraint().get(0));
        
        State avail= new State("Available",zeroIndex, true, false);
        State use= new State("InUse", zeroIndex, false, false);
        
        processorAutomata.getStateState().add(avail);
        processorAutomata.getStateState().add(use);
        
        
        ArrayList<Clock> delay2 = new ArrayList<>();
        ArrayList<Clock> noDelay = new ArrayList<>();
        delay2.add(clock); //delay2.add(2); 
        noDelay.add(clock); 
        
        Transition relAcq = new Transition(processorAutomata.getStateState().get(1), processorAutomata.getStateState().get(0), 
                zeroIndex, processorAutomata.getAlphabetSet().get(0), delay2);   
        Transition acqRel = new Transition(processorAutomata.getStateState().get(0), processorAutomata.getStateState().get(1), 
                zeroIndex, processorAutomata.getAlphabetSet().get(1), noDelay);      //acquire 
       
        processorAutomata.getTransitions().add(acqRel);
        processorAutomata.getTransitions().add(relAcq);
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
