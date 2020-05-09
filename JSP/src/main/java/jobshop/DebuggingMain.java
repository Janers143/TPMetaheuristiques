package jobshop;

import jobshop.encodings.JobNumbers;
import jobshop.encodings.ResourceOrder;
import jobshop.encodings.Task;
import jobshop.solvers.DescentSolver;
import jobshop.solvers.DescentSolver.Block;
import jobshop.solvers.DescentSolver.Swap;
import jobshop.solvers.EST_PriorityRule;
import jobshop.solvers.GreedySolver;
import jobshop.solvers.PriorityRule;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unused")
public class DebuggingMain {

    public static void main(String[] args) {
        try {
        	/* Tests for the Greedy Solver */
        	/*
            // load the aaa1 instance
            Instance instance = Instance.fromFile(Paths.get("instances/aaa1"));

            // construit une solution dans la représentation par
            // numéro de jobs : [0 1 1 0 0 1]
            // Note : cette solution a aussi été vue dans les exercices (section 3.3)
            //        mais on commençait à compter à 1 ce qui donnait [1 2 2 1 1 2]
            JobNumbers enc = new JobNumbers(instance);
            enc.jobs[enc.nextToSet++] = 0;
            enc.jobs[enc.nextToSet++] = 0;
            enc.jobs[enc.nextToSet++] = 1;
            enc.jobs[enc.nextToSet++] = 1;
            enc.jobs[enc.nextToSet++] = 0;
            enc.jobs[enc.nextToSet++] = 1;

            System.out.println("\nJOB NUMBER ENCODING: " + enc + "\n");

            Schedule sched = enc.toSchedule();
            
            System.out.println("SCHEDULE:\n" + sched);
            System.out.println("VALID: " + sched.isValid() + "\n");
            System.out.println("MAKESPAN: " + sched.makespan() + "\n");
            
            System.out.println("---------------------------------------------\n");
            
            ResourceOrder ro = new ResourceOrder(instance);
            ro.tasksByMachine[0][0] = new Task(0,0);
            ro.tasksByMachine[0][1] = new Task(1,1);
            ro.tasksByMachine[1][0] = new Task(1,0);
            ro.tasksByMachine[1][1] = new Task(0,1);
            ro.tasksByMachine[2][0] = new Task(0,2);
            ro.tasksByMachine[2][1] = new Task(1,2);
            
            System.out.println("RESOURCE ORDER ENCODING:\n" + ro + "\n");
            
            sched = ro.toSchedule();
            
            System.out.println("SCHEDULE:\n" + sched);
            System.out.println("VALID: " + sched.isValid() + "\n");
            System.out.println("MAKESPAN: " + sched.makespan() + "\n");
            
            System.out.println("---------------------------------------------\n");
            
            JobNumbers jo = JobNumbers.fromSchedule(sched);
            System.out.println("JOB NUMBER ENCODING (FROM_SCHEDULE): " + jo + "\n");
            
            System.out.println("---------------------------------------------\n");
            
            GreedySolver sptGS = new GreedySolver(PriorityRule.SPT);
            Result sptRs = sptGS.solve(instance, 10);
            System.out.println("SPT GREEDY SOLVER SCHEDULE:\n");
            System.out.println("SCHEDULE:\n" + sptRs.schedule);
            System.out.println("VALID: " + sptRs.schedule.isValid() + "\n");
            System.out.println("MAKESPAN: " + sptRs.schedule.makespan() + "\n");
            
            System.out.println("---------------------------------------------\n");
            
            GreedySolver lptGS = new GreedySolver(PriorityRule.LPT);
            Result lptRs = lptGS.solve(instance, 10);
            System.out.println("LPT GREEDY SOLVER SCHEDULE:\n");
            System.out.println("SCHEDULE:\n" + lptRs.schedule);
            System.out.println("VALID: " + lptRs.schedule.isValid() + "\n");
            System.out.println("MAKESPAN: " + lptRs.schedule.makespan() + "\n");
            
            System.out.println("---------------------------------------------\n");
            
            GreedySolver srptGS = new GreedySolver(PriorityRule.SRPT);
            Result srptRs = srptGS.solve(instance, 10);
            System.out.println("SRPT GREEDY SOLVER SCHEDULE:\n");
            System.out.println("SCHEDULE:\n" + srptRs.schedule);
            System.out.println("VALID: " + srptRs.schedule.isValid() + "\n");
            System.out.println("MAKESPAN: " + srptRs.schedule.makespan() + "\n");
            
            System.out.println("---------------------------------------------\n");
            
            GreedySolver lrptGS = new GreedySolver(PriorityRule.LRPT);
            Result lrptRs = lrptGS.solve(instance, 10);
            System.out.println("LRPT GREEDY SOLVER SCHEDULE:\n");
            System.out.println("SCHEDULE:\n" + lrptRs.schedule);
            System.out.println("VALID: " + lrptRs.schedule.isValid() + "\n");
            System.out.println("MAKESPAN: " + lrptRs.schedule.makespan() + "\n");
            
            System.out.println("---------------------------------------------\n");
            
            GreedySolver estSptGS = new GreedySolver(EST_PriorityRule.EST_SPT);
            Result estSptRs = estSptGS.solve(instance, 10);
            System.out.println("EST SPT GREEDY SOLVER SCHEDULE:\n");
            System.out.println("SCHEDULE:\n" + estSptRs.schedule);
            System.out.println("VALID: " + estSptRs.schedule.isValid() + "\n");
            System.out.println("MAKESPAN: " + estSptRs.schedule.makespan() + "\n");
            
            System.out.println("---------------------------------------------\n");
            
            GreedySolver estLptGS = new GreedySolver(EST_PriorityRule.EST_SPT);
            Result estLptRs = estLptGS.solve(instance, 10);
            System.out.println("EST LPT GREEDY SOLVER SCHEDULE:\n");
            System.out.println("SCHEDULE:\n" + estLptRs.schedule);
            System.out.println("VALID: " + estLptRs.schedule.isValid() + "\n");
            System.out.println("MAKESPAN: " + estLptRs.schedule.makespan() + "\n");
            
            System.out.println("---------------------------------------------\n");
            
            GreedySolver estSrptGS = new GreedySolver(EST_PriorityRule.EST_SPT);
            Result estSrptRs = estSrptGS.solve(instance, 10);
            System.out.println("EST SRPT GREEDY SOLVER SCHEDULE:\n");
            System.out.println("SCHEDULE:\n" + estSrptRs.schedule);
            System.out.println("VALID: " + estSrptRs.schedule.isValid() + "\n");
            System.out.println("MAKESPAN: " + estSrptRs.schedule.makespan() + "\n");
            
            System.out.println("---------------------------------------------\n");
            
            GreedySolver estLrptGS = new GreedySolver(EST_PriorityRule.EST_SPT);
            Result estLrptRs = estLrptGS.solve(instance, 10);
            System.out.println("EST LRPT GREEDY SOLVER SCHEDULE:\n");
            System.out.println("SCHEDULE:\n" + estLrptRs.schedule);
            System.out.println("VALID: " + estLrptRs.schedule.isValid() + "\n");
            System.out.println("MAKESPAN: " + estLrptRs.schedule.makespan() + "\n");
            */
        	
        	/* Tests for the descent solver */
        	// load the aaa1 instance
            Instance instance = Instance.fromFile(Paths.get("instances/myinstance"));
        	ResourceOrder ro = new ResourceOrder(instance);
        	
        	// Filling it with the solution given in the exercise
        	ro.tasksByMachine[0][0] = new Task(2,0);
            ro.tasksByMachine[0][1] = new Task(1,1);
            ro.tasksByMachine[0][2] = new Task(0,1);
            
            ro.tasksByMachine[1][0] = new Task(1,0);
            ro.tasksByMachine[1][1] = new Task(2,1);
            ro.tasksByMachine[1][2] = new Task(0,2);
            
            ro.tasksByMachine[2][0] = new Task(2,2);
            ro.tasksByMachine[2][1] = new Task(0,0);
            ro.tasksByMachine[2][2] = new Task(1,2);
            
            // Printing this solution
            System.out.println("RESOURCE ORDER ENCODING:\n" + ro + "\n");
            
            Schedule sched = ro.toSchedule();
            
            System.out.println("SCHEDULE:\n" + sched);
            System.out.println("VALID: " + sched.isValid() + "\n");
            System.out.println("MAKESPAN: " + sched.makespan() + "\n");
            
            DescentSolver ds = new DescentSolver();
            
            List<Task> criticalPath = (List<Task>) sched.criticalPath();
            Iterator<Task> it1 = criticalPath.iterator();
            int counter = 1;
            System.out.println("CRITICAL PATH:\n");
            while(it1.hasNext()) {
            	Task current = it1.next();
            	System.out.println("Task number " + counter + ": " + current);
            	counter++;
            }
            
            ArrayList<Block> blocks = (ArrayList<Block>) ds.blocksOfCriticalPath(ro);
            Iterator<Block> it2 = blocks.iterator();
            counter = 1;
            System.out.println("\nBLOCKS OF CRITICAL PATH:\n");
            while(it2.hasNext()) {
            	Block current = it2.next();
            	System.out.println("Block number " + counter + ": " + current);
            	counter++;
            }
            
            counter = 1;
            System.out.println("\nNEIGFHBORS:\n");
            ArrayList<Swap> allNbrs = new ArrayList<>();
            for (Block b : blocks) {
            	ArrayList<Swap> nbrs = (ArrayList<Swap>) ds.neighbors(b);
            	allNbrs.addAll(nbrs);
            	Iterator<Swap> it3 = nbrs.iterator();
                while(it3.hasNext()) {
                	Swap current = it3.next();
                	System.out.println("Swap number " + counter + ": " + current);
                	counter++;
                }
            }
            
            System.out.println("\nAPPLY ON:\n");
            Iterator<Swap> it4 = allNbrs.iterator();
            counter = 1;
            while(it4.hasNext()) {
            	Swap current = it4.next();
            	System.out.println("Normal ResourceOrder " + counter + ":\n" + ro);
            	current.applyOn(ro);
            	System.out.println("Swap number " + counter + ": " + current + "\n");
            	System.out.println("ResourceOrder number " + counter + ":\n" + ro);
            	current.applyOn(ro);
            	counter++;
            }
            
            System.out.println("\nDESCENT SOLVER:\n");
            Result rs = ds.solve(instance, System.currentTimeMillis() + 1000);
            System.out.println("SCHEDULE:\n" + rs.schedule);
            System.out.println("VALID: " + rs.schedule.isValid() + "\n");
            System.out.println("MAKESPAN: " + rs.schedule.makespan() + "\n");
            
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }
}
