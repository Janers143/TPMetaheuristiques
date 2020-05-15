package jobshop.solvers;

import java.util.ArrayList;

import jobshop.Instance;
import jobshop.Result;
import jobshop.Solver;
import jobshop.encodings.ResourceOrder;
import jobshop.encodings.Task;

public class GreedySolver implements Solver {
	
	/** The priority rule we want to apply */
	public final PriorityRule pr;
	/** The EST priority rule we want to apply */
	public final EST_PriorityRule estPr;
	
	/**
	 * Constructor for Greedy solver with normal priority rule.
	 * Priority rule will have a value, EST priority rule will be null
	 * @param pr The priority rule we want to apply
	 */
	public GreedySolver(PriorityRule pr) {
		this.pr = pr;
		this.estPr = null;
	}
	
	/**
	 * Constructor for Greedy solver with EST priority rule.
	 * Priority rule will be null, EST priority rule will have a value
	 * @param estPr The EST priority rule we want to apply
	 */
	public GreedySolver(EST_PriorityRule estPr) {
		this.estPr = estPr;
		this.pr = null;
	}

	@Override
	public Result solve(Instance instance, long deadline) {
		
		int numJobs = instance.numJobs;
		int numTasks = instance.numTasks;
		int numMachines = instance.numMachines;
		
		// At the beginning, the starting times are 0
		int[] nextStartTimeJobs = new int[numJobs];
		int[] nextStartTimeMachines = new int[numMachines];
		
		// An empty ressource order used to put all the tasks in a schedule
		ResourceOrder sol = new ResourceOrder(instance);
		
		// An arraylist that has all the feasable tasks right now
		ArrayList<Task> feasable = new ArrayList<>();
		// Initializing the arraylist : at the beginning the first task of each job is feasable
		for (int i = 0; i < numJobs; i++) {
			feasable.add(new Task(i, 0));
		}
		
		// While the feasable tasks list is not empty
		while(!feasable.isEmpty()) {
			Task current = null;
			// We take the task we should do now in function of the priority rule used
			if (this.estPr == null) {
				current = this.pr.getTaskByRule(feasable, instance);
			} else if (this.pr == null) {
				current = this.estPr.getTaskByRule(feasable, instance, nextStartTimeJobs, nextStartTimeMachines);
			} else {
				System.out.println("ERROR : YOU SHOULD USE A PR OR A EST_PR");
			}
			
			// We update the next free slots for next start times
			int currentJob = current.job;
			int currentTask = current.task;
			int currentMachine = instance.machine(currentJob, currentTask);
			int currentDuration = instance.duration(currentJob, currentTask);
			
			int currentStartTime = Integer.max(nextStartTimeJobs[currentJob], nextStartTimeMachines[currentMachine]);
			
			nextStartTimeJobs[currentJob] += currentStartTime + currentDuration;
			nextStartTimeMachines[currentMachine] += currentStartTime + currentDuration;
			
			// We remove the current task from the feasable list
			feasable.remove(current);
			
			// If it's not the last task of the job, we add the next one
			if (current.task < (numTasks - 1)) {
				feasable.add(new Task(current.job, current.task + 1));
			}
			
			// We add the current task to the solution
			int nextFreeSlot = sol.nextFreeSlot[currentMachine]++;
			sol.tasksByMachine[currentMachine][nextFreeSlot] = current;
		}
		
		return new Result(instance, sol.toSchedule(), Result.ExitCause.Blocked);
	}

}
