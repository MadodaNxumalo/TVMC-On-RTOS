# TVMC-On-RTOS-Extension
The RT-3MVC tool performs the functionalities for schedulability verification for real-time task models. 

# Functionalities of the tool
*  Real Time Task Schedulability Checking
*  Schedulability property verification over the concrete taskset.
*  Schedulability property verification over the concrete taskset by applying the spotlight abstraction technique.

##### The following scheduling policies are supported: 
* First Come First Serve (FIFO)
* Earliest Deadline First (EDF)
* Longest Remaining Time First (LRTF)
* Highest Response Ration Next (HRRN)

# User Guide

## Run

i)	Install the lastest [java runtime environment](http://www.oracle.com/technetwork/java/javase/downloads/index.html).
ii)	Load the program in an IDE.
iii)	Execute the program by running the Main class.

## Input

The program accepts input from a text file that represents a taskset.

Each line of the text file contains four fields that represent a model of a task. The four fields are: task id number, WECT, Deadline, and Period. 

An example of the acceptable input file is provided with a caption; 'exampleinput.txt'.

## Output
SAVING THE INPUT TASKS: The input tasks are saved on a file captioned: ExpNoLABEL.txt", where label is the experiment number properties

STATE SPACE OUTPUT: The output is stored in an output file called "OutputLABEL.txt". 
This file stores the size (clock size, states size, transitions) of the timed automata in each iteration, number of iterations, Sched or Not Sched result, and the exection time of the program. 

STATE EXPLORATION TRACE: In the cases when the output is Not Sched, this file stores the counter example trace.

## Disclaimer
###### This project is an extension of [Madoda Nxumalo's](https://github.com/MadodaNxumalo) original project, called TVMC-On-RTOS. This repository was created as part of a research effort to extend the tool's capabilities by including preemption and recurring tasks. This readme was also changed - to view the original repository, click [here](https://github.com/MadodaNxumalo/TVMC-On-RTOS).
