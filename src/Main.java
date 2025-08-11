/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


//import be.uantwerpen.idlab.cobra.tasksetgenerator.executabletaskgenerator.CreateTaskCombination;
//import be.uantwerpen.idlab.cobra.tasksetgenerator.synthetictaskgenerator.SetsGenerator;
//import be.uantwerpen.idlab.cobra.tasksetgenerator.taskcreator.TaskCreator;
//import be.uantwerpen.idlab.cobra.tasksetgenerator.synthetictaskgenerator.SetsGenerator;
//import be.uantwerpen.idlab.cobra.tasksetgenerator.taskcreator.TaskCreator;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.io.*;

import Components.TaskGenerator;



/**
 *
 * @author Madoda
 */


public class Main {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    
    
    public static void main(String[] args) throws IOException {
        //for(int j=1; j<1;j=j+1)   {  //j is number of tasks.
        //    for(int i=0;i<1;i++)   { //i is number of attempts
        //    	for(int m = 1; m<2;m++)	{ //m was the number assigned to sched policy
         		int i=1, j=2; //m=1;   		
        		int l = j;
        		
        		Scanner scan = new Scanner(System.in);
        		System.out.println("TVMC: RT Schedulability Checker");
        		System.out.println();
        		System.out.println("Enter the mode of taskset input: "); 
        		System.out.println("  Enter 1 - Randomly generated taskset ");
        		System.out.println("  Enter 2 - Taskset input from a file ");
        		int mode = scan.nextInt();
        		System.out.println();
        		
        		System.out.println("Enter the scheduling policy: ");
        		System.out.println("  Enter 1 - First Come First Serve Queue: ");
        		System.out.println("  Enter 2 - Earliest Deadline First Queue: ");
        		System.out.println("  Enter 3 - Longest Remaining Time First Queue: ");
        		System.out.println("  Enter 4 - Highest Response Ration Next Queue: ");
        		System.out.println("  Enter 5 - Round Robin (Preemptive): ");
        		System.out.println("  Enter 6 - Fixed Priority (Preemptive): ");
				
        		int policyId = scan.nextInt();
        		System.out.println();
            	
                switch (policyId) {
					case 1:
						System.out.println("First Come First Serve Queue: ");
						break;
					case 2:
                    	System.out.println("Earliest Deadline First Queue: ");           	
						break;
					case 3:
                    	System.out.println("Longest Remaining Time First Queue: ");
						break;
					case 4:
                    	System.out.println("Highest Response Ration Next Queue: ");     
						break;
					case 5:
	                	System.out.println("Round Robin (Preemptive) Queue: ");
						break;
					case 6:
	                	System.out.println("Rate Monotonic (Preemptive) Queue: ");
						break;
					default:
	                	System.out.println("Incorrect policy ID used: ");
						break;
                }
        		
        		//TaskGenerator taskGen =  new TaskGenerator("AA", l, 0.8, i*5); //TaskGenerator(int setSize, double utilize, int _seed)
        		//taskGen.setLabel(label+"-"+policyId);
                TaskGenerator taskGen; 
                String label; 
                if(mode==1)	{
        			System.out.println("Enter the taskset size: ");  
            		int tsSize = scan.nextInt();
            		label = new String(j+"-"+tsSize+"-"+policyId);
            		taskGen = new TaskGenerator(label, tsSize, 0.8, tsSize*5); //TaskGenerator(int setSize, double utilize, int _seed)
            		taskGen.generateTaskSet(j*5,j,j-4); //l=j //generateTaskSet(double periodmax, double periodmin, double periodStep)
            	//String label = new String(j+"-"+i+"-"+policyId);
            	//taskGen.taskSetSort(policyId);
                //System.out.println();
        		} else if(mode==2)	{
        			System.out.println();
        			System.out.println("Choose the file containing the taskset: "); 
					File inputDir = new File("../tasksetInput");

					String[] inputFiles = inputDir.list();
					for (int f = 0; f < inputFiles.length; f++) {
						System.out.println(String.format("%d) %s", f, inputFiles[f]));
					}
					int fileChosen = scan.nextInt();
                  	String filename = inputFiles[fileChosen];
        			
					System.out.println();
        			label = filename;//new String(filename);
        			taskGen = new TaskGenerator(filename, 0.8, i*5);
        			//taskGen.readTaskSet(filename);
        		}
        		else 
        			return;
        		
        		taskGen.taskSetSort(policyId);
        		taskGen.print(); 
        		
        //		System.out.println();
        //		System.out.println("Enter the number of processors: ");  
        //		int procSize = scan.nextInt();
        		int procSize = 1;
        		
        		//int k = 2;			//predefined iteration interval
        		System.out.println();
        		System.out.println("Specify the number of tasks per iteration: Enter Integer Range := 0 - 4");  
        		int iterationTasks = scan.nextInt();	//predefined iteration interval
//            for (int m = 0; m < 4; m++)	{
//            	label = new String(label+"-"+m);
                
                
                try (FileWriter myWriter = new FileWriter("Output"+label+".txt")) {
                	System.out.println();
                    System.out.println("Output File Successfully Created: Main class.");
                } catch (IOException e) {
                	System.out.println();
                    System.out.println("Output File Creation Error Occurred: Main class.");
                }
            	
                
                
                QueueAbstractor qa = new QueueAbstractor(iterationTasks,true,taskGen, procSize); 
                //QueueAbstractor qa = new QueueAbstractor(k,m,taskGen); 
//            	QueueAbstractor qa = new QueueAbstractor(k,true,taskGen); //FIFO - True, PriorityQ- False
//            	qa.generateProcessorSet(procSize);
            	long startTime = Instant.now().toEpochMilli();
            	boolean result = qa.queueAbstraction();
            	long endTime = Instant.now().toEpochMilli();
            	long timeElapsed = endTime - startTime;
            //long minutes = (timeElapsed / 1000) / 60;
            //long seconds = (timeElapsed / 1000);// % 60;
            //qa.writeOnPath(" "+minutes+"m"+seconds+"s\n", "filename.txt");
            	QueueAbstractor.writeOnPath(" "+timeElapsed+"s\n", "Output"+label+".txt");
            	System.out.println();
                System.out.println("Program Terminate");
                
                scan.close();
            	
            	
       //     	}
       //     }
       // }      
    }
    
   
    
}

// The following resources were used in making this file listing
// https://www.geeksforgeeks.org/java/file-list-method-in-java-with-examples/