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
import jobshop.solvers.TabooSolver;

import java.io.FileWriter;
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
        	/*
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
            
            // Test for criticalPath
            List<Task> criticalPath = (List<Task>) sched.criticalPath();
            Iterator<Task> it1 = criticalPath.iterator();
            int counter = 1;
            System.out.println("CRITICAL PATH:\n");
            while(it1.hasNext()) {
            	Task current = it1.next();
            	System.out.println("Task number " + counter + ": " + current);
            	counter++;
            }
            
            // Test for blocksOfCriticalPath
            ArrayList<Block> blocks = (ArrayList<Block>) ds.blocksOfCriticalPath(ro);
            Iterator<Block> it2 = blocks.iterator();
            counter = 1;
            System.out.println("\nBLOCKS OF CRITICAL PATH:\n");
            while(it2.hasNext()) {
            	Block current = it2.next();
            	System.out.println("Block number " + counter + ": " + current);
            	counter++;
            }
            
            // Test for neighbors
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
            
            // Test for applyOn
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
            
            // Test for solve
            System.out.println("\nDESCENT SOLVER:\n");
            Result rs = ds.solve(instance, System.currentTimeMillis() + 1000);
            System.out.println("SCHEDULE:\n" + rs.schedule);
            System.out.println("VALID: " + rs.schedule.isValid() + "\n");
            System.out.println("MAKESPAN: " + rs.schedule.makespan() + "\n");
            */
            /* Tests for the taboo solver */
        	/*
            // Test for solve
            TabooSolver taboo = new TabooSolver(5, 3);
            System.out.println("\nTABOO SOLVER:\n");
            Result rs_taboo = taboo.solve(instance, System.currentTimeMillis() + 1000);
            System.out.println("SCHEDULE:\n" + rs_taboo.schedule);
            System.out.println("VALID: " + rs_taboo.schedule.isValid() + "\n");
            System.out.println("MAKESPAN: " + rs_taboo.schedule.makespan() + "\n");
            */
        	
        	/* Writing in a CSV File */
        	//Instances
        	ArrayList<Instance> instances = new ArrayList<>();
        	Instance ft06 = Instance.fromFile(Paths.get("instances/", "ft06")); instances.add(ft06);
        	Instance ft10 = Instance.fromFile(Paths.get("instances/", "ft10")); instances.add(ft10);
        	Instance ft20 = Instance.fromFile(Paths.get("instances/", "ft20")); instances.add(ft20);
        	Instance la01 = Instance.fromFile(Paths.get("instances/", "la01")); instances.add(la01);
        	Instance la02 = Instance.fromFile(Paths.get("instances/", "la02")); instances.add(la02);
        	Instance la03 = Instance.fromFile(Paths.get("instances/", "la03")); instances.add(la03);
        	Instance la04 = Instance.fromFile(Paths.get("instances/", "la04")); instances.add(la04);
        	Instance la05 = Instance.fromFile(Paths.get("instances/", "la05")); instances.add(la05);
        	Instance la06 = Instance.fromFile(Paths.get("instances/", "la06")); instances.add(la06);
        	Instance la07 = Instance.fromFile(Paths.get("instances/", "la07")); instances.add(la07);
        	Instance la08 = Instance.fromFile(Paths.get("instances/", "la08")); instances.add(la08);
        	Instance la09 = Instance.fromFile(Paths.get("instances/", "la09")); instances.add(la09);
        	Instance la10 = Instance.fromFile(Paths.get("instances/", "la10")); instances.add(la10);
        	Instance la11 = Instance.fromFile(Paths.get("instances/", "la11")); instances.add(la11);
        	Instance la12 = Instance.fromFile(Paths.get("instances/", "la12")); instances.add(la12);
        	Instance la13 = Instance.fromFile(Paths.get("instances/", "la13")); instances.add(la13);
        	Instance la14 = Instance.fromFile(Paths.get("instances/", "la14")); instances.add(la14);
        	Instance la15 = Instance.fromFile(Paths.get("instances/", "la15")); instances.add(la15);
        	Instance la16 = Instance.fromFile(Paths.get("instances/", "la16")); instances.add(la16);
        	Instance la17 = Instance.fromFile(Paths.get("instances/", "la17")); instances.add(la17);
        	Instance la18 = Instance.fromFile(Paths.get("instances/", "la18")); instances.add(la18);
        	Instance la19 = Instance.fromFile(Paths.get("instances/", "la19")); instances.add(la19);
        	Instance la20 = Instance.fromFile(Paths.get("instances/", "la20")); instances.add(la20);
        	Instance la21 = Instance.fromFile(Paths.get("instances/", "la21")); instances.add(la21);
        	Instance la22 = Instance.fromFile(Paths.get("instances/", "la22")); instances.add(la22);
        	Instance la23 = Instance.fromFile(Paths.get("instances/", "la23")); instances.add(la23);
        	Instance la24 = Instance.fromFile(Paths.get("instances/", "la24")); instances.add(la24);
        	Instance la25 = Instance.fromFile(Paths.get("instances/", "la25")); instances.add(la25);
        	Instance la26 = Instance.fromFile(Paths.get("instances/", "la26")); instances.add(la26);
        	Instance la27 = Instance.fromFile(Paths.get("instances/", "la27")); instances.add(la27);
        	Instance la28 = Instance.fromFile(Paths.get("instances/", "la28")); instances.add(la28);
        	Instance la29 = Instance.fromFile(Paths.get("instances/", "la29")); instances.add(la29);
        	Instance la30 = Instance.fromFile(Paths.get("instances/", "la30")); instances.add(la30);
        	Instance la31 = Instance.fromFile(Paths.get("instances/", "la31")); instances.add(la31);
        	Instance la32 = Instance.fromFile(Paths.get("instances/", "la32")); instances.add(la32);
        	Instance la33 = Instance.fromFile(Paths.get("instances/", "la33")); instances.add(la33);
        	Instance la34 = Instance.fromFile(Paths.get("instances/", "la34")); instances.add(la34);
        	Instance la35 = Instance.fromFile(Paths.get("instances/", "la35")); instances.add(la35);
        	Instance la36 = Instance.fromFile(Paths.get("instances/", "la36")); instances.add(la36);
        	Instance la37 = Instance.fromFile(Paths.get("instances/", "la37")); instances.add(la37);
        	Instance la38 = Instance.fromFile(Paths.get("instances/", "la38")); instances.add(la38);
        	Instance la39 = Instance.fromFile(Paths.get("instances/", "la39")); instances.add(la39);
        	Instance la40 = Instance.fromFile(Paths.get("instances/", "la40")); instances.add(la40);
        	
        	//Solvers
        	GreedySolver EST_LRPT_sol = new GreedySolver(EST_PriorityRule.EST_LRPT);
        	GreedySolver LRPT_sol = new GreedySolver(PriorityRule.LRPT);
        	
        	DescentSolver descent_sol = new DescentSolver();
        	TabooSolver taboo_sol_1_1 = new TabooSolver(1,1);
        	TabooSolver taboo_sol_10_3 = new TabooSolver(10,3);
        	TabooSolver taboo_sol_100_5 = new TabooSolver(100,5);
        	TabooSolver taboo_sol_1000_10 = new TabooSolver(1000,10);
        	TabooSolver taboo_sol_5000_10 = new TabooSolver(5000,10);
        	
        	//FileWritter
        	FileWriter csvWriter = new FileWriter("Different_taboos_results.csv");
        	csvWriter.append("Instance,DescentSolver,TabooSolver(1_1),TabooSolver(10_3),TabooSolver(100_5),TabooSolver(1000_10),TabooSolver(5000_10)\n");
        	for (int i = 0; i < instances.size(); i++) {
        		Instance current = instances.get(i);
	        	String instanceName = current.path.toString().substring(10, 14);
	        	csvWriter.append(instanceName + ",");
	        	
	        	int bestKnown =  BestKnownResult.of(instanceName);
	        	
	        	int makespan = descent_sol.solve(current, System.currentTimeMillis() + 10000).schedule.makespan();
	        	float dist = 100f * (makespan - bestKnown) / (float) bestKnown;
	        	csvWriter.append(Float.toString(dist) + ",");
	        	
	        	makespan = taboo_sol_1_1.solve(current, System.currentTimeMillis() + 10000).schedule.makespan();
	        	dist = 100f * (makespan - bestKnown) / (float) bestKnown;
	        	csvWriter.append(Float.toString(dist) + ",");
	        	
	        	makespan = taboo_sol_10_3.solve(current, System.currentTimeMillis() + 10000).schedule.makespan();
	        	dist = 100f * (makespan - bestKnown) / (float) bestKnown;
	        	csvWriter.append(Float.toString(dist) + ",");
	        	
	        	makespan = taboo_sol_100_5.solve(current, System.currentTimeMillis() + 10000).schedule.makespan();
	        	dist = 100f * (makespan - bestKnown) / (float) bestKnown;
	        	csvWriter.append(Float.toString(dist) + ",");
	        	
	        	makespan = taboo_sol_1000_10.solve(current, System.currentTimeMillis() + 10000).schedule.makespan();
	        	dist = 100f * (makespan - bestKnown) / (float) bestKnown;
	        	csvWriter.append(Float.toString(dist) + ",");
	        	
	        	makespan = taboo_sol_5000_10.solve(current, System.currentTimeMillis() + 10000).schedule.makespan();
	        	dist = 100f * (makespan - bestKnown) / (float) bestKnown;
	        	csvWriter.append(Float.toString(dist) + "\n");
	        	
	        	System.out.println("Finished instance " + i);
        	}
        	
        	csvWriter.flush();
        	csvWriter.close();
            
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }
}
