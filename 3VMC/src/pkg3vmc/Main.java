/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3vmc;

import TimedAutomata.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Madoda
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Hello World!" );
        
        Task task1 = new Task(3.1,4.4,5.1);
        task1.setTaskAutomata();
        Task task2 = new Task(6.3,12.0,13.6);
        task2.setTaskAutomata();
        //public TimedAutomata(Set<State> s,ArrayList<Clock> c, Set<AlphabetSet> a, Set<Transition> t); 
        
        TimedAutomata added, added2;
        added = new TimedAutomata(task1.getTaskAutomata().getStateState(),task1.getTaskAutomata().getClocks(),task1.getTaskAutomata().getAlphabetSet());
        
        added2 = new TimedAutomata(task2.getTaskAutomata().getStateState(),task2.getTaskAutomata().getClocks(),task2.getTaskAutomata().getAlphabetSet());
        
        
        System.out.println("ADDED PRINT");
        List<State> stateList = new ArrayList<>(added.getStateState());
        stateList.forEach((stateList1) -> {
            stateList1.print();
        });
        
        System.out.println("ADDED 2 PRINTEER");
        List<State> stateListX = new ArrayList<>(added2.getStateState());
        stateListX.forEach((stateListY) -> {
            stateListY.print();
        });
        
        //TimedAutomata added2 = new TimedAutomata();
        System.out.println("ADDED INCREASED NOW");
        added.addTimedAutomata(added2);
        List<State> stateL = new ArrayList<>(added.getStateState());
        stateL.forEach((stateListY) -> {
            stateListY.print();
        });

        
        
        QueueAbstractor qa = new QueueAbstractor(5,10);
        qa.generateRandomConcreteQueue(10);
        //qa.generateRandomProcessorSet(1);
        //qa.generateAbstractQueue();
        boolean result;// = qa.queueAbstraction();
        result = qa.queueAbstraction();
        qa.print();
        
        
    }
    
}
