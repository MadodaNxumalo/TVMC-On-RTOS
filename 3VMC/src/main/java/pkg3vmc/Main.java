/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3vmc;

import java.time.Instant;

/**
 *
 * @author Madoda
 */


public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //for(int i=2;i<;i=i+2)   {
            QueueAbstractor qa = new QueueAbstractor(2,8);
            //qa.generateFileConcreteQueue("fifoTasks.txt");
            qa.generateRandomProcessorSet(1);
            long startTime = Instant.now().toEpochMilli();
            boolean result = qa.queueAbstraction();
            long endTime = Instant.now().toEpochMilli();
            long timeElapsed = endTime - startTime;
            System.out.println("Execution time in milliseconds: " + timeElapsed);
            
            QueueAbstractor qaFull = new QueueAbstractor(8,8);
            //qa.generateFileConcreteQueue("fifoTasks.txt");
            qaFull.generateRandomProcessorSet(1);
            startTime = Instant.now().toEpochMilli();
            result = qaFull.queueAbstraction();
            endTime = Instant.now().toEpochMilli();
            timeElapsed = endTime - startTime;
            System.out.println("Execution time in milliseconds: " + timeElapsed);
        
    }
    
}
