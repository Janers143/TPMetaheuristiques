/**
 * 
 */
package jobshop.solvers;

import jobshop.encodings.Task;

/**
 * A structure in which we can store the taboo permutations
 * @author antho
 *
 */
public class TabooStructure {

	/**
	 * The maximum duration for a permutation to be Taboo
	 */
	private final int tabooDuration;
	
	/**
	 * A matrix with all the forbidden permutations : it stores the
	 * number of the iteration when the permutation will be allowed.
	 * The lines are the first task in the permutation, the columns
	 * are the second task.
	 * <br>
	 * <br>
	 * E.G. : Consider the following ResourceOrder<br>
	 * machine 0 : (0,1) (1,2) (2,2)<br>
	 * machine 1 : (0,2) (2,1) (1,1)<br>
	 * ...<br>
	 * <br>
	 * If we make the Swap: machine = 0; t1 = 0; t2 = 1
	 * then the permutation: machine = 0; t1 = 0; t2 = 1 will be
	 * forbidden for the next <b>tabooDuration</b> iterations.<br>
	 * To represent it, the value <b>currentIteration + tabooDuration</b>
	 * will be stored in the matrix on line <b>Task(1,2).getTaskID(numTasks)</b>,
	 * column <b>Task(0,1).getTaskID(numTasks)</b>.
	 * <br>
	 * <br>
	 * Task.getTaskID(int numTasks) is a function I have added to
	 * the task class to get an ID for each task that goes from 0 to
	 * the total number of tasks. numTasks is the number of tasks for
	 * each job.
	 */
	private int [][] tabooPerms;
	
	/**
	 * Constructor
	 * @param forbiddenDuration The duration for a permutation to be Taboo
	 * @param nbTasks The total number of tasks. If there are 2 jobs with
	 * 3 tasks each then nbTasks = 6.
	 */
	public TabooStructure(int forbiddenDuration, int nbTasks) {
		this.tabooDuration = forbiddenDuration;
		
		this.tabooPerms = new int[nbTasks][nbTasks];
	}
	
	/**
	 * Function that adds a taboo permutation (t1<->t2) to the matrix
	 * @param t1 The first task in the permutation
	 * @param t2 The second task in the permutation
	 * @param currentIteration The current iteration number 
	 * @param numTasks The number of tasks there is in a job
	 */
	public void addTaboo(Task t1, Task t2, int currentIteration, int numTasks) {
		int t1ID = t1.getTaskID(numTasks);
		int t2ID = t2.getTaskID(numTasks);
		
		this.tabooPerms[t1ID][t2ID] = currentIteration + this.tabooDuration;
	}
	
	/**
	 * Function that tells you if the permutation (t1<->t2) is allowed
	 * @param t1 The first task in the permutation
	 * @param t2 The second task in the permutation
	 * @param currentIteration The current iteration number 
	 * @param numTasks The number of tasks there is in a job
	 * @return A boolean telling whether the permutation is allowed or not
	 */
	public boolean isAllowed(Task t1, Task t2, int currentIteration, int numTasks) {
		int t1ID = t1.getTaskID(numTasks);
		int t2ID = t2.getTaskID(numTasks);
		
		return (this.tabooPerms[t1ID][t2ID] <= currentIteration);
	}

}
