package Components;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;


public class TaskGenerator {
		String label; 
        ArrayList<Task> taskSet; //new ArrayList<>();
        int numberOfTasks;
        double utilization; 
        int seed;

	public TaskGenerator(String l, int setSize, double utilize, int _seed) {
		label = l;
        taskSet = new ArrayList<>();
        numberOfTasks = setSize;
        utilization = utilize;
        seed = _seed;        
	}
	
	public String getLabel()    {
        return label;
    }
        
    public ArrayList<Task> getTaskSet()    {
    	return taskSet;
    } 
        
         
        
        //Bini Enrico - Biasing Effects in Schedulability Measures
        private List<Double> uFitting()    {
            List<Double> vectorU = new ArrayList<>();
            vectorU.add(0.0);
            double upLimit = utilization;
            //System.out.println("Task UTE: "+ upLimit);
            Random random = new Random(seed);
            for(int i=1; i<numberOfTasks-1;i++)    {
                double randUte = random.nextDouble()*upLimit;
                vectorU.add(randUte);
                upLimit = upLimit-vectorU.get(i);
            }
            vectorU.add(upLimit);
            return vectorU;
        }
        
        /*private int[][] gnerateMatrix(int  rowSize) {
            int colSize = Math.Rand(Math.random(1,row));
            int matrix[rowSize][colSize]; 
            for (int i=0;i<colSize;i++)
                matrix[i][0] = 

        }*/
        
        private double detPeriod()    {
              /*for(int i=0; i<size; i++)
                for(int j=0; j<size; j++)
                    Mij[i][j] = 0;
             
            double period = 1.0;
            Random rand = new Random();
            for(int i=0; Mij.size();i++)    {
                for(int j=0; j<Mij.size();j++)  {
                    p = Math.round(Math.floor(Math.random()*Mij[i,j]+1));
                    period = period * Mij[i,p];
                }
            }*/
            return 0;
        }
	//generateTaskset(double minPeriod, double maxPeriod, double stepPeriod, double minLoad, double maxLoad, double stepLoad, int numberTasks, int seed)
        public void generateTaskSet(double periodmax, double periodmin, double periodStep) throws FileNotFoundException, UnsupportedEncodingException    {
        	
        	PrintWriter writer = new PrintWriter("ExpNo"+label+".txt", "UTF-8");
        	
        	
            int currentLoad = 0;
            int i=0;
            List<Double> taskUtils;
            taskUtils = uFitting(); //(int numTask, double utilization, int seed)
            //for(Double t: taskUtils)
            //	System.out.println("Task UTE: "+ t.toString());
            while(currentLoad < utilization && i < numberOfTasks)    {
                double period = (int)(Math.round((Math.random()*(periodmax/periodStep-periodmin/periodStep)+periodmin/periodStep))*periodStep);
                double wcet = Math.round(taskUtils.get(i)*period)+1;
                //double occurrence = Math.Round(Math.random(o1,o2)*period);
                double occurance = Math.round(Math.random()*period);
                //double occurance = 0;
                double deadline = Math.round(Math.random()*(period-wcet) + wcet);//(period-wcet)*Math.random()*range)+wcet;
                //deadline = period = 100;
                
                
                if(currentLoad + (wcet/period) <= 1){
                    currentLoad = (int) (currentLoad + (wcet/period));
                    Task t = new Task("T"+i,wcet,period, deadline,occurance); //, occurance);   Task(String s, double w, double p, double d)
                    t.setTaskAutomata();
                    taskSet.add(t);
                    i=i+1;
                }
                
            }
            Collections.sort(taskSet, new Comparator<Object>() {
                @Override
				public int compare(Object o1, Object o2) {
                	Task oA = (Task) o1;
                	Task oB = (Task) o2;
                	return oA.getDeadline() < oB.getDeadline() ? -1 : 1;
					//return oA.getOccurance() < oB.getOccurance() ? -1 : 1;
				}
            });
            
            for(Task t : taskSet)	{
            	writer.println(t.toString());
            }
            writer.close();
            
        }   
      //double period = detPeriod();
        //(double minPeriod, double maxPeriod, double stepPeriod, double minLoad, double maxLoad, double stepLoad, int numberTasks, int seed)

        //List<Task> taskSet = new ArrayList<Task>(); 
      //double deadline = Math.Round((period-wcet)*Math.random(d1,d2))+wcet;
        //double range = Math.random()*(period-wcet) + wcet; 
        //double wcet = Math.max(1, Math.round(Rand(u1,u2)*period));
        //double wcet = Math.max(1, Math.round(taskUtils.get(i-1)*period));
        //double wcet = Math.max(1, Math.round(taskUtils.get(i-1)*period));
      //numberTasks = i;
        //Collections.sort(taskSet);
        
        public void print() {
        	for(Task t: taskSet)
        		System.out.println(t.toString());;
        }

	
	
}
