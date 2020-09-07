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
import Components.Task;
import Components.Processor;
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
        tvModelChecker = new TMVC();
        processorSet = generateRandomProcessorSet(1);
        concreteTaskQueue = generateRandomConcreteQueue(n);
        automataArray = new ArrayList<>();
        abstractTaskQueue = new LinkedList<>();
        abstractTask = new Task("100",100,100,100);
        interval = setInterval(k);
    }
    
    public QueueAbstractor()    {
        tvModelChecker = new TMVC();
        processorSet = new ArrayList<>();
        concreteTaskQueue = new LinkedList<>();
        automataArray = new ArrayList<>();
        abstractTaskQueue = new LinkedList<>();
        abstractTask = new Task("",0,0,0);
        interval = 0;
    }
    
   
    
    public Queue<Task> generateRandomConcreteQueue(int n)    {
        Queue<Task> tempTaskList = new LinkedList<>();
        for(int i=0; i<n; i++){
            String l = new String();
            l = Integer.toString(i);
            Task task = new Task(l,3.1,5.2,5.2);
            task.setTaskAutomata();
            tempTaskList.add(task);  
        }
        return tempTaskList;
    }
    
    
    public ArrayList<Processor> generateRandomProcessorSet(int m)    {
        ArrayList<Processor> tempProcessorList = new ArrayList<>(); 
        for(int i=0; i<m; i++){
            Processor processor = new Processor(0);
            processor.setProcessorAutomata();
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
            TimedAutomata temp = new TimedAutomata(p.getTaskAutomata());
            automataArray.add(temp);
        }
        if(!concreteTaskQueue.isEmpty()){
            abstractTaskQueue.add(abstractTask);
            automataArray.add(abstractTask.getTaskAutomata());
        }
        
        processorSet.forEach((processorSet1) -> {
            TimedAutomata temp = new TimedAutomata(processorSet1.getAutomata());
            automataArray.add(temp);
        }); 
    }
    
    public boolean queueAbstraction() {
        TemporalLogic propertyTCTL = new TemporalLogic();
        boolean threeValue = true;
        while(!concreteTaskQueue.isEmpty()) {
            //generateAbstractQueue();
            
            TimedAutomata NTA = new TimedAutomata(automataArray.get(0));
            NTA = NTA.addTimedAutomata(automataArray.get(1));
            //NTA = NTA.addTimedAutomata(automataArray.get(2));
            //NTA = NTA.addTimedAutomata(automataArray.get(3));
            //NTA = NTA.addTimedAutomata(automataArray.get(4));
            NTA.print();
            
            //tvModelChecker.addLogic(propertyTCTL);
            //threeValue = tvModelChecker.exploreStateSpace(NTA, abstractTaskQueue);
            threeValue =tvModelChecker.threeValFwdReachability(NTA, abstractTaskQueue);
            if (threeValue==true)   {
                System.out.println("Verification Success!" );
                return true;
            }
            else if(threeValue==false)  {
                System.out.println("Some Task Missed a Deadline!" );
                //tvModelChecker.PrintRun();
                return false;
            }
        }
       
        return true; 
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
    
    public void print() {
        //System.out.println("Concrete Task Front " );
        //System.out.print(concreteTaskQueue.size());
        //System.out.println("Next is abstract Task array");
        
        //for(Task x : abstractTaskArray)
        //    x.print();
    }
}
