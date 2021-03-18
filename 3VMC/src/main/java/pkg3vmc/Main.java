/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3vmc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.ProcessBuilder.Redirect.Type.APPEND;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.CREATE;
import java.time.Instant;

/**
 *
 * @author Madoda
 */


public class Main {

    /**
     * @param args the command line arguments
     */
    
    
    public static void main(String[] args) throws IOException {
        //for(int i=2;i<;i=i+2)   {
        /*
        try {
            File myObj = new File("filename.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }*/
        
        try (FileWriter myWriter = new FileWriter("filename.txt")) {
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        
        for(int j=2; j<5;j++)   {
            for(int i=0;i<5;i++)   {
            int k = j;
            int l = 4*k;
            QueueAbstractor qa = new QueueAbstractor(k,l);
            //qa.generateFileConcreteQueue("fifoTasks.txt");
            qa.generateRandomProcessorSet(1);
            long startTime = Instant.now().toEpochMilli();
            boolean result = qa.queueAbstraction();
            long endTime = Instant.now().toEpochMilli();
            long timeElapsed = endTime - startTime;
            qa.writeOnPath(" "+timeElapsed+"\n", "filename.txt");
            }
        }
            //myWriter.write(" "+timeElapsed);
            /*
            try (FileWriter myWriter = new FileWriter("filename.txt")) {
                myWriter.write(" "+timeElapsed);"
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }*/
            
            
            /*
            QueueAbstractor qaFull = new QueueAbstractor(2,6);
            //qa.generateFileConcreteQueue("fifoTasks.txt");
            qaFull.generateRandomProcessorSet(1);
            startTime = Instant.now().toEpochMilli();
            result = qaFull.queueAbstraction();
            endTime = Instant.now().toEpochMilli();
            timeElapsed = endTime - startTime;
            System.out.println(" "+ timeElapsed);*/
        
    }
    
   
    
}
