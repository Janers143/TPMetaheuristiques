package jobshop.solvers;

import java.util.ArrayList;

import jobshop.Instance;
import jobshop.encodings.Task;

/**
 * Enum class for the normal priority rules
 * @author antho
 */
public enum PriorityRule {
	/** Prority for the shortest task */
	SPT {
		@Override
		public Task getTaskByRule(ArrayList<Task> feasable, Instance instance) {
			// We search the min duration for the tasks in feasable list
			int minTime = Integer.MAX_VALUE;
			Task minTask = null;
			
			for (Task t: feasable) {
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
	
	/** Prority for the longest task */
	LPT{
		@Override
		public Task getTaskByRule(ArrayList<Task> feasable, Instance instance) {
			// We search the max duration for the tasks in feasable list
			int maxTime = Integer.MIN_VALUE;
			Task maxTask = null;
			
			for (Task t: feasable) {
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
	
	/** Priority for the longest remaining time for the full job */
	SRPT{
		@Override
		public Task getTaskByRule(ArrayList<Task> feasable, Instance instance) {
			// We search the min duration of the remaining time for each job
			int minTime = Integer.MAX_VALUE;
			Task minTask = null;
			
			for (Task t: feasable) {
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
	
	/** Priority for the longest remaining time for the full job */
	LRPT {
		@Override
		public Task getTaskByRule(ArrayList<Task> feasable, Instance instance) {
			// We search the max duration of the remaining time for each job
			int maxTime = Integer.MIN_VALUE;
			Task maxTask = null;
			
			for (Task t: feasable) {
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
	 * @return The task we should do according to the priority rule used
	 */
	public abstract Task getTaskByRule(ArrayList<Task> feasable, Instance instance);
}
