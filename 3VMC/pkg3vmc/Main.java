/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3vmc;
/**
 *
 * @author Madoda
 */


public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //System.out.println("Hello World!" );
          
       
        
        
        
        
        QueueAbstractor qa = new QueueAbstractor(2,2);
        qa.generateFileConcreteQueue("fifoTasks.txt");
        qa.generateRandomProcessorSet(1);
        //qa.generateAbstractQueue();
        boolean result = qa.queueAbstraction();
        
        //System.out.println();
        
        /*
        Task task1 = new Task("1",3.1,4.4,5.1);
        task1.setTaskAutomata();
        Task task2 = new Task("2",6.3,12.0,13.6);
        task2.setTaskAutomata();
        //TimedAutomata(ArrayList<State> s,ArrayList<Alphabet> a, ArrayList<Clock> c, ArrayList<ClockConstraint> cc, ArrayList<Transition> tr) 
        TimedAutomata added, added2;
        added = new TimedAutomata(task1.getTaskAutomata());
       
        
        added2 = new TimedAutomata(task2.getTaskAutomata());
        
        
        System.out.println("ADDED PRINT");
        added.print();
        added2.print();
        System.out.println("ADDED INCREASED NOW");
        TimedAutomata sum = added.addTimedAutomata(added2);
        sum.print();
        System.out.println("TRANSITION SIZE: " + sum.getTransitions().size());
        */

        
        
        /*
        List<State> qaStateList = new ArrayList<>(qa.getAutomataArray().get(0).getStateState());
        qaStateList.forEach((stateListY) -> {
            stateListY.print();
        });
        
        TimedAutomata xx,yy,zz;
        /*xx = new TimedAutomata(qa.getAutomataArray().get(0).getStateState(),
                qa.getAutomataArray().get(0).getClocks(),qa.getAutomataArray().get(0).getAlphabetSet(),
                qa.getAutomataArray().get(0).getClockConstraint());
        yy = new TimedAutomata(qa.getAutomataArray().get(1).getStateState(),
                qa.getAutomataArray().get(1).getClocks(),qa.getAutomataArray().get(1).getAlphabetSet(),
                qa.getAutomataArray().get(1).getClockConstraint());
        zz = new TimedAutomata(qa.getAutomataArray().get(2).getStateState(),
                qa.getAutomataArray().get(2).getClocks(),qa.getAutomataArray().get(2).getAlphabetSet(),
                qa.getAutomataArray().get(2).getClockConstraint());
        xx.addTimedAutomata(yy);
        xx.addTimedAutomata(zz);
        */
        /*
        TimedAutomata sum22 = new TimedAutomata(qa.getAutomataArray().get(0));
        for (int i=0; i<3;i++)  
            sum22 = sum22.addTimedAutomata(qa.getAutomataArray().get(i));  
        
        System.out.println("XX XX INCREASED NOW");
        
        List<State> stateXX = new ArrayList<>(sum22.getStateState());
        stateXX.forEach((stateListY) -> {
            stateListY.print();
        });*/
        
        
        //Task taskT = qa.getAbstractTaskQueue().remove();
        //TimedAutomata taT = new TimedAutomata(taskT.getTaskAutomata().getStateState(),taskT.getTaskAutomata().getClocks(),taskT.getTaskAutomata().getAlphabetSet());
        
        
        
        //result = qa.queueAbstraction();
        //qa.print();
        
        
    }
    
}
