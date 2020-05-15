package jobshop.solvers;

import java.util.ArrayList;

import jobshop.Instance;
import jobshop.encodings.Task;

/**
 * Enum class for the est priority rules
 * @author antho
 */
public enum EST_PriorityRule {
	/**  EST Prority for the shortest task */
	EST_SPT {
		@Override
		public Task getTaskByRule(ArrayList<Task> feasable, Instance instance, int[] nextStartTimeJobs, int[] nextStartTimeMachines) {
			// We get all the tasks with the minimum starting time
			ArrayList<Task> sameStartTime = this.getMinStartTasks(feasable, instance, nextStartTimeJobs, nextStartTimeMachines);
			
			// Now that we have all the tasks with the minimum start time,
			// we apply the priority rule
			int minTime = Integer.MAX_VALUE;
			Task minTask = null;
			
			for (Task t: sameStartTime) {
				// Get the duration of the current task
				int taskDuration = instance.duration(t.job, t.task);
				// Check if it's lower than the current minimum duration
				if (taskDuration < minTime) {
					minTime = taskDuration;
					minTask = t;
				}
			}
			return minTask;
			
		}
	},
	
	/** EST Prority for the longest task */
	EST_LPT{
		@Override
		public Task getTaskByRule(ArrayList<Task> feasable, Instance instance, int[] nextStartTimeJobs, int[] nextStartTimeMachines) {
			// We get all the tasks with the minimum starting time
			ArrayList<Task> sameStartTime = this.getMinStartTasks(feasable, instance, nextStartTimeJobs, nextStartTimeMachines);
			
			// Now that we have all the tasks with the minimum start time,
			// we apply the priority rule
			int maxTime = Integer.MIN_VALUE;
			Task maxTask = null;
			
			for (Task t: sameStartTime) {
				// Get the duration of the current task
				int taskDuration = instance.duration(t.job, t.task);
				// Check if it's higher than the current maximum duration
				if (taskDuration >= maxTime) {
					maxTime = taskDuration;
					maxTask = t;
				}
			}
			
			return maxTask;
		}
	},
	
	/** EST Priority for the longest remaining time for the full job */
	EST_SRPT{
		@Override
		public Task getTaskByRule(ArrayList<Task> feasable, Instance instance, int[] nextStartTimeJobs, int[] nextStartTimeMachines) {
			// We get all the tasks with the minimum starting time
			ArrayList<Task> sameStartTime = this.getMinStartTasks(feasable, instance, nextStartTimeJobs, nextStartTimeMachines);
			
			// Now that we have all the tasks with the minimum start time,
			// we apply the priority rule
			int minTime = Integer.MAX_VALUE;
			Task minTask = null;
			
			for (Task t: sameStartTime) {
				// Calculating the duration of the remaining time for the current job
				int jobDuration = 0;
				for (int task = t.task; task < instance.numTasks; task++) {
					jobDuration += instance.duration(t.job, task);
				}
				
				// Check if it's lower than the current minimum duration
				if (jobDuration < minTime) {
					minTime = jobDuration;
					minTask = t;
				}
			}
			
			return minTask;
		}
	},
	
	/** EST Priority for the longest remaining time for the full job */
	EST_LRPT {
		@Override
		public Task getTaskByRule(ArrayList<Task> feasable, Instance instance, int[] nextStartTimeJobs, int[] nextStartTimeMachines) {
			// We get all the tasks with the minimum starting time
			ArrayList<Task> sameStartTime = this.getMinStartTasks(feasable, instance, nextStartTimeJobs, nextStartTimeMachines);
			
			// Now that we have all the tasks with the minimum start time,
			// we apply the priority rule
			int maxTime = Integer.MIN_VALUE;
			Task maxTask = null;
			
			for (Task t: sameStartTime) {
				// Calculating the duration of the remaining time for the current job
				int jobDuration = 0;
				for (int task = t.task; task < instance.numTasks; task++) {
					jobDuration += instance.duration(t.job, task);
				}
				
				// Check if it's higher than the current maximum duration
				if (jobDuration > maxTime) {
					maxTime = jobDuration;
					maxTask = t;
				}
			}
			
			return maxTask;
		}
	};
	
	/**
	 * A method that gets the Task we should do right now depending on the
	 * priority rule that's being used 
	 * @param feasable A list with all the tasks currently feasable
	 * @param instance An instance with the durations for each task
	 * @param nextStartTimeJobs An array with the next possible start time for each job
	 * @param nextStartTimeMachines An array with the next possible start time for each machine
	 * @return The task we should do according to the priority rule used
	 */
	public abstract Task getTaskByRule(ArrayList<Task> feasable, Instance instance, int[] nextStartTimeJobs, int[] nextStartTimeMachines);
	
	/**
	 * Function that gets all the tasks with the minimum possible starting time
	 * @param feasable A list with all the tasks currently feasable
	 * @param instance An instance with the durations and the machines for each task
	 * @param nextStartTimeJobs An array with the next possible start time for each job
	 * @param nextStartTimeMachines An array with the next possible start time for each machine
	 * @return An arraylist with all the tasks with the minimum possible starting time
	 */
	protected ArrayList<Task> getMinStartTasks(ArrayList<Task> feasable, Instance instance, int[] nextStartTimeJobs, int[] nextStartTimeMachines){
		// We search the min starting time for the tasks in feasable list
		int minStartTime = Integer.MAX_VALUE;
		
		// ArrayList with all the Tasks with the same min start time
		ArrayList<Task> sameStartTime = new ArrayList<>();
		
		for (Task t: feasable) {
			int tJob = t.job;
			int tMachine = instance.machine(t.job, t.task);
			int currentTaskStartTime = Integer.max(nextStartTimeJobs[tJob], nextStartTimeMachines[tMachine]);
			
			if (currentTaskStartTime < minStartTime) {
				// There is a new minimum, we clear the whole old task list
				// and we put the current task
				sameStartTime.clear();
				sameStartTime.add(t);
				// We update the minimum start time
				minStartTime = currentTaskStartTime;
			} else if (currentTaskStartTime == minStartTime) {
				// We found a new task with the same start time,
				// we add the task to the ones we should apply the
				// priority rule
				sameStartTime.add(t);
			}
		}
		
		return sameStartTime;
	}

}
