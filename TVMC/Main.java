/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TVMC;

//import be.uantwerpen.idlab.cobra.tasksetgenerator.executabletaskgenerator.CreateTaskCombination;
//import be.uantwerpen.idlab.cobra.tasksetgenerator.synthetictaskgenerator.SetsGenerator;
//import be.uantwerpen.idlab.cobra.tasksetgenerator.taskcreator.TaskCreator;
//import be.uantwerpen.idlab.cobra.tasksetgenerator.synthetictaskgenerator.SetsGenerator;
//import be.uantwerpen.idlab.cobra.tasksetgenerator.taskcreator.TaskCreator;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

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

        for(int j=5; j<12;j=j+5)   {  //j=5,j<6
            for(int i=0;i<1;i++)   {
            	for(int m = 1; m<4;m++)	{
            		int k = 3;
            		int l = j;
            
            
            		String label = new String(j+"-"+i+"-"+m);
            
            		TaskGenerator taskGen = new TaskGenerator(label, l, 0.8, i*5); //TaskGenerator(int setSize, double utilize, int _seed)
            		taskGen.generateTaskSet(l*5,l,l-4); //generateTaskSet(double periodmax, double periodmin, double periodStep)
            		taskGen.print(); 
//            for (int m = 0; m < 4; m++)	{
//            	label = new String(label+"-"+m);
                taskGen.taskSetSort(m);
                if(m==0)  		            		
                	System.out.println("First Come First Serve Queue: ");           	
                if(m==1)  		            		
                    System.out.println("Shortest Deadline First Queue: ");           	
                if(m==2)  	            		
                    System.out.println("Longest Remaining Time First Queue: ");           	
                if(m==3)  	            		
                    System.out.println("Highest Response Ration Next Queue: ");     
                
                try (FileWriter myWriter = new FileWriter("filename"+label+".txt")) {
                    System.out.println("File Successfully Created: Main class.");
                } catch (IOException e) {
                    System.out.println("File Creation Error Occurred: Main class.");
                }
            	
                QueueAbstractor qa = new QueueAbstractor(k,true,taskGen); 
//            	QueueAbstractor qa = new QueueAbstractor(k,true,taskGen); //FIFO - True, PriorityQ- False
            	qa.generateProcessorSet(1);
            	long startTime = Instant.now().toEpochMilli();
            	boolean result = qa.queueAbstraction();
            	long endTime = Instant.now().toEpochMilli();
            	long timeElapsed = endTime - startTime;
            //long minutes = (timeElapsed / 1000) / 60;
            //long seconds = (timeElapsed / 1000);// % 60;
            //qa.writeOnPath(" "+minutes+"m"+seconds+"s\n", "filename.txt");
            	QueueAbstractor.writeOnPath(" "+timeElapsed+"s\n", "filename"+label+".txt");
            	}
            }
        }      
    }
    
   
    
}
