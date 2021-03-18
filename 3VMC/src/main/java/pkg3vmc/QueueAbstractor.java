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
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public final class QueueAbstractor {
    private final TMVC tvModelChecker;  //init as null
    //PriorityQueue<Integer> pQueue = new PriorityQueue<Integer>();
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
            String l = Integer.toString(i);
            //double w=i,d=i*2,p=i*2;
            Task task = new Task(l,2,18,18);
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
    
    
    public void generateAbstractQueue(double abstractClock)    {
        //System.out.println("Generate ABSTRACT QUEUE Clock Value: "+ abstractClock);
        for (int i=0; i<interval; i++)  { //|| !concreteTaskQueue.isEmpty()
            if(concreteTaskQueue.isEmpty())
                break;
            Task p = concreteTaskQueue.remove();
            //p.setAbstracTaskAutomata(abstractClock);
            abstractTaskQueue.add(p);
            TimedAutomata temp = new TimedAutomata(p.getTaskAutomata());
            temp.getClocks().forEach(t -> {
                t.update(abstractClock);
            });
            //for(State s: temp.getStateSet())   {
            //    for(ClockConstraint cc: s.getInvariant())   
            //    cc.getClock().update(abstractClock);
            //}
            //temp.print();
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
    
    public boolean queueAbstraction() throws IOException {
        int threeValue = 1;
        int iteration = 0;
        double abstractZn = 0.0; //new ClockZone();
        
        while(!concreteTaskQueue.isEmpty()) {
            //System.out.println("Highest Clock Value 11: "+ abstractZn);
            automataArray.clear();
            generateAbstractQueue(abstractZn);
            TimedAutomata NTA;
            NTA = new TimedAutomata(automataArray.get(0));
            
            //System.out.println("Abstract Queue SIZE: "+ automataArray.size());
            
            for(int i=1;i<automataArray.size();++i) {
                NTA = NTA.addTimedAutomata(automataArray.get(i));
            }
            
            //System.out.println("NTA AFTER ");          
            //NTA.print();
            
            
            threeValue = tvModelChecker.threeVReachability(NTA);
            abstractZn = tvModelChecker.highClock;
            
            iteration++;
            writeOnPath(NTA.getTransitions().size()+" ; ", "filename.txt"); //System.out.print(iteration+" - "+NTA.getTransitions().size()+" | ");
            if(threeValue==0)  {
                //System.out.println("Some Task Missed a Deadline!: --->" + concreteTaskQueue.size() );
                return false;
            }
            //System.out.println("Highest Clock Value: "+ abstractZn);
            //updateConcreteQueue(concreteTaskQueue, abstractTaskQueue);
            
        }
        writeOnPath(iteration+" ; ", "filename.txt");
        //System.out.println("Highest Clock Value : "+ abstractZn);
        
        //System.out.println();
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
    
    public static void writeOnPath(String fileContent, String pathString) throws IOException {
        Files.write(Paths.get(pathString), fileContent.getBytes(), StandardOpenOption.APPEND);
    }
    
}
