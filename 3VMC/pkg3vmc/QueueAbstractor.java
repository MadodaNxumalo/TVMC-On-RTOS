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
import TimedAutomata.Zone;
import java.util.*;
import java.io.*;
import java.util.Scanner;

public final class QueueAbstractor {
    private final TMVC tvModelChecker;  //init as null
    private final Queue<Task> abstractTaskQueue; //init
    private final Queue<Task> concreteTaskQueue; //init
    private final ArrayList<TimedAutomata> automataArray; //init
    private static Task abstractTask;       //Init
    private final ArrayList<Processor> processorSet; //Init
    private final int interval;
    //private TimedAutomata NTA;
    
    public QueueAbstractor(int k, int n)    {
        tvModelChecker = new TMVC();
        processorSet = generateRandomProcessorSet(1);
        concreteTaskQueue = generateRandomConcreteQueue(n);
        automataArray = new ArrayList<>();
        abstractTaskQueue = new LinkedList<>();
        abstractTask = new Task("100",100,100,100);
        interval = setInterval(k);
        //NTA = new TimedAutomata();
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
            String l = new String(Integer.toString(i));
            Task task = new Task(l,3.1,5.2,5.2);
            task.setTaskAutomata();
            tempTaskList.add(task);  
        }
        return tempTaskList;
    }
    
    
    public Queue<Task> generateFileConcreteQueue(String file) {
        Queue<Task> tempTaskList = new LinkedList<>();
        try {
            File myObj = new File(file);
            Scanner myReader = new Scanner(myObj);
            //while (myReader.hasNextLine()) {
            
            while (myReader.hasNext()) {
                String label = myReader.next();
                double inWCET = Double.parseDouble(myReader.next());
                double inDeadline = Double.parseDouble(myReader.next());
                double inPeriod = Double.parseDouble(myReader.next());
                Task task = new Task(label, inWCET,inDeadline,inPeriod);
                task.setTaskAutomata();
                tempTaskList.add(task); 
                System.out.println(label+" "+inWCET+ " " + inDeadline +" " +inPeriod);
        }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
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
            Task q = new Task(concreteTaskQueue);
            abstractTaskQueue.add(q);
            automataArray.add(q.getTaskAutomata());
        }
        
        processorSet.forEach((processorSet1) -> {
            TimedAutomata temp = new TimedAutomata(processorSet1.getAutomata());
            automataArray.add(temp);
        });
        
   
    }
    
    public void generateNTA(TimedAutomata NTA)  {
        
        if(!automataArray.isEmpty())
            NTA = automataArray.get(0);
                
        for(int i = 1; i<automataArray.size();i++) {
            NTA = NTA.addTimedAutomata(automataArray.get(i));
        }
   
    }
    
    public boolean queueAbstraction() {
        ArrayList<Zone> passed = new ArrayList<>();
        int threeValue = 1;
        while(!concreteTaskQueue.isEmpty()) {
            generateAbstractQueue();
            TimedAutomata NTA;
            NTA = new TimedAutomata(automataArray.get(0));
            //System.out.println("NTA BEFORE ");
            for(int i=1;i<3;++i) {
            //for(int i=1;i<2;++i) {
                //System.out.println("NTA Size: " + NTA.getStateSet().size());
                NTA = NTA.addTimedAutomata(automataArray.get(i));
                //NTA.print();
                //System.out.println();
            }
             System.out.println("NTA AFTER ");          
            NTA.print();
            //threeValue = tvModelChecker.exploreStateSpace(NTA, abstractTaskQueue);
            //threeValue = tvModelChecker.threeValFwdReachability(NTA, abstractTaskQueue);
            
            //threeValue = tvModelChecker.threeV_Checker(NTA, abstractTaskQueue);
            threeValue = tvModelChecker.threeVReachability(NTA, abstractTaskQueue, passed);
            
            if (threeValue==1)   {
                System.out.println("Verification Success!" );
                return true;
            }
            else if(threeValue==0)  {
                System.out.println("Some Task Missed a Deadline!: --->" + concreteTaskQueue.size() );
                //tvModelChecker.PrintRun();
                //continue;
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
