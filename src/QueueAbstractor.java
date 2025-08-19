/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Madoda
 */
import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public final class QueueAbstractor {
    private final TMVC tvModelChecker;  //init as null
    //PriorityQueue<Integer> pQueue = new PriorityQueue<Integer>();
    private final Queue<Task> abstractTaskQueue; //init
    private final Queue<Task> concreteTaskQueue; //init
    private final ArrayList<Task> terminatedTaskArray; //init
    private final ArrayList<TimedAutomata> automataArray; //init
    private final ArrayList<Processor> processorSet; //Init
    private ArrayList<StateZone> counterPath;
    private final int interval;
    private String label;
    //private TimedAutomata NTA;
    
    public QueueAbstractor(int k, boolean t, TaskGenerator tg, int procSz)    {
    	
        tvModelChecker = new TMVC();
        processorSet = generateProcessorSet(procSz);
        //concreteTaskQueue = generateRandomConcreteQueue(tg.getTaskSet().size());
        concreteTaskQueue = new LinkedList<>();
        
       
        
        
        for(Task task:tg.getTaskSet()) {
        	Task tk = new Task(task);
        	concreteTaskQueue.add(tk);
        }
        
        terminatedTaskArray = new ArrayList<>();
        counterPath = new ArrayList<>();
        automataArray = new ArrayList<>();
        
        if(t==true)
        	abstractTaskQueue = new LinkedList<>();
        else 
            abstractTaskQueue =  new PriorityQueue<>();
/*        else if (t==2)
            abstractTaskQueue =  new PriorityQueue<>();
        else if (t==3)
            abstractTaskQueue =  new PriorityQueue<>();
        else
        	abstractTaskQueue = new LinkedList<>();
*/        
        new Task("100",100,100,100,100);
        interval = setInterval(k);
        
        label = tg.getLabel();
    }
    
    public QueueAbstractor()    {
        tvModelChecker = new TMVC();
        processorSet = new ArrayList<>();
        concreteTaskQueue = new LinkedList<>();
        terminatedTaskArray = new ArrayList<>();
        automataArray = new ArrayList<>();
        counterPath = new ArrayList<>();
        abstractTaskQueue = new LinkedList<>();
        new Task("",0,0,0,0);
        interval = 0;
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
                double inOccurance = Double.parseDouble(myReader.next());
                Task task = new Task(label, inWCET,inDeadline,inPeriod,inOccurance);
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
    
    public ArrayList<Processor> generateProcessorSet(int m)    {
        ArrayList<Processor> tempProcessorList = new ArrayList<>(); 
        for(int i=0; i<m; i++){
            Processor processor = new Processor(Integer.toString(i),0);
            processor.setProcessorAutomata();
            tempProcessorList.add(processor);  
        }
        return tempProcessorList;
    }
    
    
    public void generateAbstractQueue(double abstractClock)    {
    	Task prevIteTask = new Task();
    	if(!abstractTaskQueue.isEmpty())	{
    		//prevIteTask = new Task(terminateTaskArray);
    		prevIteTask = abstractTaskQueue.remove();
    	}
    	abstractTaskQueue.clear();
        for (int i=0; i<interval; i++)  { //|| !concreteTaskQueue.isEmpty()
            if(concreteTaskQueue.isEmpty())
                break;
            Task p = concreteTaskQueue.remove();
            System.out.println("Task in Spotlight: "+ p.toString()); 
            abstractTaskQueue.add(p);
            TimedAutomata temp = new TimedAutomata(p.getTaskAutomata());
            //System.out.println("TEMP TA: "+temp.getClocks());
            //temp.getClocks().forEach(t -> {
            //	System.out.println("TEMP TA: "+temp.getClocks().toString());
            //});
            automataArray.add(temp);
        }
        
        if(!concreteTaskQueue.isEmpty()){
            Task shade = new Task(concreteTaskQueue);
            System.out.println("Abstract Task in Spotlight: "+ shade.toString());
            abstractTaskQueue.add(shade);
            automataArray.add(shade.getTaskAutomata()); 
        }
        
        for(Task shade:concreteTaskQueue)
        	System.out.println("Task in Shade: "+ shade.toString());
        
        processorSet.forEach((processorSet1) -> {
            TimedAutomata temp = new TimedAutomata(processorSet1.getAutomata());
            automataArray.add(temp);
        });
        
    }
    
    public void abstractionCounter()	{
    	
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
//        	System.out.println("Abstract Queue CALLED WITH SIZE: "+ abstractTaskQueue.size());
            generateAbstractQueue(abstractZn);
            TimedAutomata NTA;
            NTA = new TimedAutomata(automataArray.get(0));
//            System.out.println("Abstract Queue IS NOW SIZE: "+ abstractTaskQueue.size());
//            System.out.println("AUTOMATA GET  ");          
//            automataArray.get(0).print();
            
            for(int i=1;i<automataArray.size();++i) {
                NTA = NTA.addTimedAutomata(automataArray.get(i));
            }
            
            //System.out.println("NTA AFTER ");          
            //NTA.print();
            
            threeValue = tvModelChecker.threeVReachability(NTA,abstractTaskQueue, counterPath); 
            //Add terminatedTaskArray if task has reached terminate state? 
            
            //terminatedTaskArray = new ArrayList<>();
            
            abstractZn = tvModelChecker.timeline;
            
            iteration++;
            //System.out.print(label+" FILE NAME");
            writeOnPath(NTA.getClocks().size()+" "+NTA.getStateSet().size()+" "+NTA.getTransitions().size()+"; ", "Output"+label+".txt"); 
            //System.out.print(iteration+" - "+NTA.getTransitions().size()+" | ");
            if(threeValue==0)  {
            	printCounterExample();
            	writeOnPath(iteration+" ; "+" Not Sched","Output"+label+".txt");
                return false;
            }
            //System.out.println("Highest Clock Value: "+ abstractZn);
            //updateConcreteQueue(concreteTaskQueue, abstractTaskQueue);
            
        }
        writeOnPath(iteration+" ; "+" Sched", "Output"+label+".txt");
        //System.out.println("Highest Clock Value : "+ abstractZn);
        
        //System.out.println();
        return true; 
    }
    
    public void printCounterExample()	{
    	System.out.println("Counter Example: "); 
    	for(StateZone cp: counterPath)	{
    		System.out.println(toString()+" --> "); 
    	}
    	System.out.println();
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

	public ArrayList<Task> getTerminatedTaskArray() {
		return terminatedTaskArray;
	}
    
}
