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

        for(int j=5; j<6;j=j+5)   {
            for(int i=0;i<10;i++)   {
            int k = 3;
            int l = j;
            String label = new String(j+"-"+i);
            
            try (FileWriter myWriter = new FileWriter("filename"+label+".txt")) {
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
            }
            
            TaskGenerator taskGen = new TaskGenerator(label, l, 0.8, i*5); //TaskGenerator(int setSize, double utilize, int _seed)
            taskGen.generateTaskSet(l*5,l,l-4); //generateTaskSet(double periodmax, double periodmin, double periodStep)
            taskGen.print(); 
            QueueAbstractor qa = new QueueAbstractor(k,true,taskGen); //FIFO - True, PriorityQ- False
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
