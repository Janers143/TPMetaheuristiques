package jobshop.encodings;

import java.util.Objects;

/** Represents a task (job,task) of an jobshop problem.
 *
 * Example : (2, 3) repesents the fourth task of the third job. (remeber that we tart counting at 0)
 * */
public final class Task {

    /** Identifier of the job */
    public final int job;

    /** Index of the task inside the job. */
    public final int task;


    public Task(int job, int task) {
        this.job = job;
        this.task = task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task1 = (Task) o;
        return job == task1.job &&
                task == task1.task;
    }

    @Override
    public int hashCode() {
        return Objects.hash(job, task);
    }

    @Override
    public String toString() {
        return "Job " + this.job + " Task " + this.task;
    }
    
    /**
     * Function I have added to make a prettier print function
     * @return A new Task that has 1 added to its job and its task nb
     */
    public Task add_one() {
    	return new Task(this.job + 1, this.task + 1);
    }

    /**
     * Function I have added to get an ID for each task that goes 
     * from 0 to the total number of tasks.
     * @param numTasks The number of tasks ther is for each job
     * @return An int ID for the task
     * @see jobshop.solvers.TabooStructure#tabooPerms
     */
	public int getTaskID(int numTasks) {
		return (this.job * numTasks + this.task);
	}
}
