/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3vmc;


import TimedAutomata.Alphabet;
import TCTL.Clock;
import TCTL.Predicate;
import TimedAutomata.ClockConstraint;
import TimedAutomata.State;
import TimedAutomata.TimedAutomata;
import TimedAutomata.Transition;
import java.util.ArrayList;


/**
 *
 * @author Madoda
 */

public final class Processor {
    //private Clock processorClock;
    private double quantumSlice;
    TimedAutomata processorAutomata;
    
    public Processor(double q) {
        setQuantumSlice(q);
        processorAutomata = setProcessorAutomata();
    }
    
    private void setQuantumSlice(double x)    {
        quantumSlice = x;
    }
    
    public TimedAutomata setProcessorAutomata()   {
        
        TimedAutomata tempTA = new TimedAutomata();
        
        Clock clock = new Clock(0.0, "c_resource");
        tempTA.getClocks().add(clock);
        
        Alphabet acq = new Alphabet("acquire_r"); //0
        Alphabet rel = new Alphabet("release_r");  //1
        
        tempTA.getAlphabetSet().add(acq);
        tempTA.getAlphabetSet().add(rel);
        
        
        //List<String> alphaList = new ArrayList<>(tempTA.getAlphabetSet());
        
        ArrayList<Transition> acqTransList = new ArrayList<>();
        ArrayList<Transition> relTransList = new ArrayList<>();
        
        ClockConstraint window = new ClockConstraint(clock, quantumSlice, "<=");
        
        
        
        State avail= new State("Available",null, true, false, acqTransList);
        State use= new State("InUse", window, false, false, acqTransList);
        //(State destination, String guard, AlphabetSet action, String assignments)
        //ArrayList<Predicate> assign;
        
        //Transition(State destination, ClockConstraint guard, String act, ArrayList<Clock> assign)
        //Transition(State destination, ArrayList<ClockConstraint> guard, Alphabet act, ArrayList<Double> assign) {
        Transition relAcq = new Transition(use, window, "acquire_r", "delay = 2");   
        Transition acqRel = new Transition(avail, null, "release_r", "delay = -1");      //acquire 
       
        avail.getOutTransitions().add(acqRel);
        use.getOutTransitions().add(relAcq);
        
        
        
        tempTA.getStateState().add(avail); //0
        tempTA.getStateState().add(use);  //1
     
        //tempTA.getTransitions().add(acqRel);
        //tempTA.getTransitions().add(relAcq);
        
        return tempTA;
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
