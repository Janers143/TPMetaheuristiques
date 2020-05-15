package jobshop.solvers;

import java.util.ArrayList;
import java.util.List;

import jobshop.Instance;
import jobshop.Result;
import jobshop.Schedule;
import jobshop.Solver;
import jobshop.Result.ExitCause;
import jobshop.encodings.ResourceOrder;
import jobshop.encodings.Task;


/**
 * This class allows you to use the taboo algorithm for solving a JobShop problem
 * @author antho
 *
 */
public class TabooSolver implements Solver {

	/**
	 * The same as for Descent Solver
	 * A block represents a subsequence of the critical path such that all tasks in it execute on the same machine.
     * This class identifies a block in a ResourceOrder representation.
     *
     * Consider the solution in ResourceOrder representation
     * machine 0 : (0,1) (1,2) (2,2)
     * machine 1 : (0,2) (2,1) (1,1)
     * machine 2 : ...
     *
     * The block with : machine = 1, firstTask= 0 and lastTask = 1
     * Represent the task sequence : [(0,2) (2,1)]
     *
     * */
	// TODO I should remove the public (used for tests)
    public static class Block {
        /** machine on which the block is identified */
        final int machine;
        /** index of the first task of the block */
        final int firstTask;
        /** index of the last task of the block */
        final int lastTask;

        Block(int machine, int firstTask, int lastTask) {
            this.machine = machine;
            this.firstTask = firstTask;
            this.lastTask = lastTask;
        }
        
        /**
         * Function that gets the number of tasks in a block
         * @return The number of tasks in a block
         */
        public int nbTasks() {
        	return this.lastTask - this.firstTask + 1;
        }
        
        @Override
        public String toString() {
			return "[Machine : " + this.machine + " | First Task : " + this.firstTask + " | Last Task : " + this.lastTask + "]";
        }
    }

	/**
	 * The same as for Descent Solver
	 * Represents a swap of two tasks on the same machine in a ResourceOrder encoding.
	 *
	 * Consider the solution in ResourceOrder representation
	 * machine 0 : (0,1) (1,2) (2,2)
	 * machine 1 : (0,2) (2,1) (1,1)
	 * machine 2 : ...
	 *
	 * The swap with : machine = 1, t1= 0 and t2 = 1
	 * Represent inversion of the two tasks : (0,2) and (2,1)
	 * Applying this swap on the above resource order should result in the following one :
	 * machine 0 : (0,1) (1,2) (2,2)
	 * machine 1 : (2,1) (0,2) (1,1)
	 * machine 2 : ...
	 */
    //TODO I should remove the public status (used for testing)
    public static class Swap {
        // machine on which to perform the swap
        final int machine;
        // index of one task to be swapped
        final int t1;
        // index of the other task to be swapped
        final int t2;

        Swap(int machine, int t1, int t2) {
            this.machine = machine;
            this.t1 = t1;
            this.t2 = t2;
        }

        /**
         * Apply this swap on the given resource order, transforming it into a new solution.
         * @param order A ResourceOrder on which we should apply the current swap.
         * <b>/!\ The ResourceOrder will be changed.</b>
         * To get back to the initial one apply the swap once again.
         */
        public void applyOn(ResourceOrder order) {
            Task t_aux = order.tasksByMachine[this.machine][this.t2];
            order.tasksByMachine[this.machine][this.t2] = order.tasksByMachine[this.machine][this.t1];
            order.tasksByMachine[this.machine][this.t1] = t_aux;            
        }
        
        @Override
        public String toString() {
			return "[Machine : " + this.machine + " | T1 : " + this.t1 + " | T2 : " + this.t2 + "]";
        }
    }
    
    /**
     * The maximum number of iterations for the TabooSolver
     */
    private final int maxIter;
    
    /**
     * The number of iterations a swap is forbidden
     */
    private final int forbiddenDuration;

    /**
     * Constructor
     * @param maxIter The maximum number of iterations for the TabooSolver
     * @param tabooDuration The number of iterations a swap is forbidden
     */
	public TabooSolver(int maxIter, int tabooDuration) {
		this.maxIter = maxIter;
		this.forbiddenDuration = tabooDuration;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public Result solve(Instance instance, long deadline) {
		// We get an initial solution with the EST_LRPT greedy solver
        GreedySolver gs = new GreedySolver(EST_PriorityRule.EST_LRPT);
    	Schedule best = gs.solve(instance, deadline).schedule;
    	Schedule current = best;
    	
    	// We initialize the other components needed
    	ResourceOrder currentOrder = null;
    	ExitCause ec = null;
    	
    	// We create a new matrix that will store all the forbidden permutations
    	TabooStructure forbiddenSwaps = new TabooStructure(this.forbiddenDuration, instance.numJobs * instance.numTasks);
    	// We initialize the iteration counter k
    	int k = 0;
    	
    	// Main loop (while we haven't reached the timeout or the max number of iterations)
    	while(k < this.maxIter && deadline - System.currentTimeMillis() > 1) {
    		// We update the resource order
    		currentOrder = new ResourceOrder(current);
    		// We get all the blocks from the critical path
    		ArrayList<Block> blocks = (ArrayList<Block>) this.blocksOfCriticalPath(currentOrder);
    		
    		// Best neighbor schedule in the loop
    		Schedule bestNeighbor = null;
    		Swap bestSwap = null;
    		
    		for (Block b : blocks) {
    			// For each block we get all its neighbors
    			ArrayList<Swap> nbrs = (ArrayList<Swap>) this.neighbors(b);
    			
    			for (Swap s : nbrs) {
    				// We check if the current swap is allowed
    				Task t1 = currentOrder.tasksByMachine[s.machine][s.t1];
    				Task t2 = currentOrder.tasksByMachine[s.machine][s.t2];
    				boolean allowed = forbiddenSwaps.isAllowed(t1, t2, k, instance.numTasks);
    				
    				if (allowed) {
    					// If the swap is allowed we see if it's the best neighbor
    					// For each neighbor, we apply the swap and we check if it is better than the current best solution
        				s.applyOn(currentOrder);
        				Schedule neighbor = currentOrder.toSchedule();
        				if ((neighbor != null) && (bestNeighbor == null)) {
        					// We found the first valid neighbor
        					bestNeighbor = neighbor;
        					bestSwap = s;
        				} else if ((neighbor != null) && (bestNeighbor != null) && (neighbor.makespan() < bestNeighbor.makespan())) {
        					// We found a better neighbor than the one we had before
        					bestNeighbor = neighbor;
        					bestSwap = s;
        				}
        				s.applyOn(currentOrder);
    				}
    			}	
    		}
    		
    		// Now we have the best valid neighbor from all neighbors of the current schedule
    		// We check if it is better than the global best
    		if ((bestNeighbor != null) && (bestNeighbor.makespan() < best.makespan())) {
    			// We found a better solution than the one we had before
    			best = bestNeighbor;
    			// We forbid the opposite swap of the one we have made
    			// We have made t1<->t2 so we forbid t2<->t1
    			Task t1 = currentOrder.tasksByMachine[bestSwap.machine][bestSwap.t1];
				Task t2 = currentOrder.tasksByMachine[bestSwap.machine][bestSwap.t2];
    			forbiddenSwaps.addTaboo(t2, t1, k, instance.numTasks);
    		}
    		
    		// We update the current schedule with the one of the best neighbor
    		if (bestNeighbor != null) {
    			current = bestNeighbor;
    		}
    		// Increasing the iteration counter
    		k++;
    	}
    	
    	if (k == this.maxIter) {
    		ec = Result.ExitCause.Blocked;
    	} else {
    		ec = Result.ExitCause.Timeout;
    	}
    	
		return new Result(instance, best, ec);
	}
	
	/** 
	 * The same as for Descent Solver
     * Returns a list of all blocks of the critical path. 
     * @param order A Resource Order of which we have to extract the blocks of the critical path 
     * @return A list with the blocks of the critical path 
     */
    public List<Block> blocksOfCriticalPath(ResourceOrder order) {
        // Recovering the critical path of this solution
    	List<Task> criticalPath = order.toSchedule().criticalPath();
    	// Recovering the number of machines for this problem
        int nbMachines = order.instance.numMachines;
        // Recovering the number of jobs for this problem
        int nbJobs = order.instance.numJobs;
        // An array containing the first and last task executed in each block
        // It has nbMachines lines and 2 columns (one for the first and one 
        // for the last task of the block). Initialized at -1.
        int [][] blocksArray = new int [nbMachines][2];
        for (int i = 0; i < nbMachines; i++) {
        	blocksArray[i][0] = -1;
        	blocksArray[i][1] = -1;
        }
        
        for (Task t : criticalPath) {
        	// Get the machine used for the current task
        	int currentMachine = order.instance.machine(t.job, t.task);
        	// Get the number of the execution for this task in the machine
        	// (is it the first task to execute on this machine? the second?)
        	int taskForMachine = -1;
        	for (int i = 0; i < nbJobs; i++) {
        		if (t.equals(order.tasksByMachine[currentMachine][i])) {
        			taskForMachine = i;
        		}
        	}
        	// Updating blocksArray if necessary
        	if (blocksArray[currentMachine][0] == -1 && blocksArray[currentMachine][1] == -1) {
        		// First task to appear for this block so its the fisrt and the last for the moment
        		blocksArray[currentMachine][0] = taskForMachine;
        		blocksArray[currentMachine][1] = taskForMachine;
        	} else {
        		// For the moment, it is the last task for this block
        		blocksArray[currentMachine][1] = taskForMachine;
        	}
        }
    	
        // Now that we have the data needed, we create the blocks and return them
        ArrayList<Block> blocks = new ArrayList<>();
        for (int i = 0; i < nbMachines; i++) {
        	if (blocksArray[i][0] != -1 && blocksArray[i][1] != -1 && blocksArray[i][0] != blocksArray[i][1]) {
        		// This block is used in the critical path
        		Block current = new Block(i, blocksArray[i][0], blocksArray[i][1]);
        		blocks.add(current);
        	}
        }
        
        return blocks;
    }

    /** 
     * The same as for Descent Solver
     * For a given block, return the possible swaps for the Nowicki and Smutnicki neighborhood
     * @param A block of which we extract the neighbors
     * @return A list of Swaps that represent the neighbors of the current solution
     */
    public List<Swap> neighbors(Block block) {
        List<Swap> nbrs = new ArrayList<>();
        
        if (block.nbTasks() == 2) {
        	// There's only one possible swap
        	Swap s1 = new Swap(block.machine, block.firstTask, block.lastTask);
        	nbrs.add(s1);
        } else if (block.nbTasks() > 2) {
        	// There are 2 possible swap
        	Swap s1 = new Swap(block.machine, block.firstTask, block.firstTask + 1);
        	Swap s2 = new Swap(block.machine, block.lastTask - 1, block.lastTask);
        	nbrs.add(s1);
        	nbrs.add(s2);
        } else {
        	System.out.println("Less than 2 tasks in the block : ERROR");
        }
        
        return nbrs;
    }

}
