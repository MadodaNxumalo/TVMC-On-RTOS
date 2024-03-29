# TVMC-On-RTOS
#Functionalities of the tool

##The RT-3MVC tool performs the functionalities for schedulability verification for real-time task models. 

i)	The following scheduling policies are supported: 

	a)	First Come First Serve FIFO, 
	
	b)	Earliest Deadline First EDF, 
	
	c)	Longest Remaining Time First, and 
	
	d)	Highest Response Ration Next are supported

ii)	Schedulability property verification over the concrete taskset.

iii)	Schedulability property verification over the concrete taskset by applying the spotlight abstraction technique.



#User Guide

## Run

i)	Install the lasted java runtime environment.

ii)	Load the program in an IDE such as Eclipse.

iii)	Execute the program by running the Main class in TVMC package.

##Input

The program accepts input from a text file that represents a taskset.

Each line of the text file contains four fields that represent a model of a task. The four fields are: task id number, WECT, Deadline, and Period. 

An example of the acceptable input file is provided with a caption; 'exampleinput.txt'.

##Output

SAVING THE INPUT TASKS: The input tasks are saved on a file captioned: ExpNoLABEL.txt", where label is the experiment number properties

STATE SPACE OUTPUT: The output is stored in an output file called "OutputLABEL.txt". 
This file stores the size (clock size, states size, transitions) of the timed automata in each iteration, number of iterations, Sched or Not Sched result, and the exection time of the program. 

STATE EXPLORATION TRACE: In the cases when the output is Not Sched, this file stores the counter example trace.
