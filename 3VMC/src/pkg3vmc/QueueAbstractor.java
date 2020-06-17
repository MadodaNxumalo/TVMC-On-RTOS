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
import TimedAutomata.TimedAutomata;
import java.util.*;

public final class QueueAbstractor {
    private final TMVC tvModelChecker;  //init as null
    private final Queue<Task> abstractTaskQueue; //init
    private final Queue<Task> concreteTaskQueue; //init
    private final ArrayList<TimedAutomata> automataArray; //init
    private static Task abstractTask;       //Init
    private final ArrayList<Processor> processorSet; //Init
    private final int interval;
    
     public QueueAbstractor(int k, int n)    {
        tvModelChecker = null;
        processorSet = generateRandomProcessorSet(1);
        concreteTaskQueue = generateRandomConcreteQueue(n);
        automataArray = new ArrayList<>();
        abstractTaskQueue = new LinkedList<>();
        abstractTask = new Task(100,100,100);
        //generateAbstractQueue();
        interval = setInterval(k);
    }
    
    public QueueAbstractor()    {
        tvModelChecker = null;
        processorSet = new ArrayList<>();
        concreteTaskQueue = new LinkedList<>();
        automataArray = new ArrayList<>();
        abstractTaskQueue = new LinkedList<>();
        abstractTask = new Task(0,0,0);
        interval = 0;
    }
    
    public Queue<Task> getConcreteTaskQueue() {
        return concreteTaskQueue;
    }
    
    public ArrayList<Processor> getProsessorSet()    {
        return processorSet;
    }
    
    public ArrayList<TimedAutomata> getAutomataArray()    {
        return automataArray;
    }
    
    public Queue<Task> getAbstractTaskQueue() {
        return abstractTaskQueue;
    }
   
    
        
    private int setInterval(int k){
         //Interval should be less than n: queue size
        return (k >0) ? k : 0;
    }
    
    public Queue<Task> generateRandomConcreteQueue(int n)    {
        Queue<Task> tempTaskList = new LinkedList<>();
        for(int i=0; i<n; i++){
            Task task = new Task(3.1,5.2,5.2);
            tempTaskList.add(task);  
        }
        return tempTaskList;
    }
    
    
    public ArrayList<Processor> generateRandomProcessorSet(int m)    {
        ArrayList<Processor> tempProcessorList = new ArrayList<>(); 
        for(int i=0; i<m; i++){
            Processor processor = new Processor(0);
            tempProcessorList.add(processor);  
        }
        return tempProcessorList;
    }
    
    
    public void generateAbstractQueue()    {
        for (int i=0; i<interval; i++)  { //|| !concreteTaskQueue.isEmpty()
            if(concreteTaskQueue.isEmpty())
                break;
            Task p = concreteTaskQueue.remove();
            abstractTaskQueue.add(p);
            automataArray.add(p.taskAutomata);
        }
        if(!concreteTaskQueue.isEmpty()){
            abstractTaskQueue.add(abstractTask);
            automataArray.add(abstractTask.taskAutomata);
        }
        
        processorSet.forEach((processorSet1) -> {
            automataArray.add(processorSet1.processorAutomata);
        }); 
    }
    
    public boolean queueAbstraction() {
        TemporalLogic propertyTCTL = new TemporalLogic();
        boolean threeValue = true;
        //while(!concreteTaskQueue.isEmpty()) {
            generateAbstractQueue();
            TimedAutomata NTA = new TimedAutomata(automataArray.get(0));
            System.out.println("COMPONENT TASK TIMED AUTOMATA PRINT");
            for(int x=1; x<automataArray.size();x++)
                NTA.addTimedAutomata(automataArray.get(x));
                System.out.println("NRTWORK TIMED AUTOMATA PRINT "+ NTA.getStateState().size());
            
                NTA.getStateState().forEach((i) -> {
                    i.print();
                });
            
                System.out.println("NTA PRINT DONE");
            //tvModelChecker.addLogic(propertyTCTL);
            //threeValue = tvModelChecker.exploreStateSpace(NTA, abstractTaskQueue);
            
            if (threeValue==true)   {
                System.out.println("Verification Success!" );
                return true;
            }
            else if(threeValue==false)  {
                System.out.println("Some Task Missed a Deadline!" );
                //tvModelChecker.PrintRun();
                return false;
            }
        //}
        return true; 
    }
    
    public void print() {
        //System.out.println("Concrete Task Front " );
        //System.out.print(concreteTaskQueue.size());
        //System.out.println("Next is abstract Task array");
        
        //for(Task x : abstractTaskArray)
        //    x.print();
    }
}
