package jobshop;

import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


import jobshop.solvers.BasicSolver;
import jobshop.solvers.DescentSolver;
import jobshop.solvers.EST_PriorityRule;
import jobshop.solvers.GreedySolver;
import jobshop.solvers.PriorityRule;
//import jobshop.solvers.PriorityRule;
import jobshop.solvers.RandomSolver;
import jobshop.solvers.TabooSolver;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;


public class Main {

    /** All solvers available in this program */
    private static HashMap<String, Solver> solvers;
    static {
        solvers = new HashMap<>();
        solvers.put("basic", new BasicSolver());
        solvers.put("random", new RandomSolver());
        // add new solvers here
        solvers.put("greedy_spt", new GreedySolver(PriorityRule.SPT));
        solvers.put("greedy_lpt", new GreedySolver(PriorityRule.LPT));
        solvers.put("greedy_srpt", new GreedySolver(PriorityRule.SRPT));
        solvers.put("greedy_lrpt", new GreedySolver(PriorityRule.LRPT));
        
        solvers.put("greedy_est_spt", new GreedySolver(EST_PriorityRule.EST_SPT));
        solvers.put("greedy_est_lpt", new GreedySolver(EST_PriorityRule.EST_LPT));
        solvers.put("greedy_est_srpt", new GreedySolver(EST_PriorityRule.EST_SRPT));
        solvers.put("greedy_est_lrpt", new GreedySolver(EST_PriorityRule.EST_LRPT));
        
        solvers.put("descent_solver", new DescentSolver());
        solvers.put("taboo_solver1", new TabooSolver(1, 1));
        solvers.put("taboo_solver2", new TabooSolver(10, 3));
        solvers.put("taboo_solver3", new TabooSolver(100, 5));
        solvers.put("taboo_solver4", new TabooSolver(1000, 10));
        solvers.put("taboo_solver5", new TabooSolver(5000, 10));
        solvers.put("taboo_solver6", new TabooSolver(50000, 15));
        /*la01 la02 la03 la04 la05 la06 la07 la08 la09 la10 la11 la12 la13
          la14 la15 la16 la17 la18 la19 la20 la21 la22 la23 la24 la25 la26 
          la27 la28 la29 la30 la31 la32 la33 la34 la35 la36 la37 la38 la39 la40*/
    }


    @SuppressWarnings("unused")
	public static void main(String[] args) {
        ArgumentParser parser = ArgumentParsers.newFor("jsp-solver").build()
                .defaultHelp(true)
                .description("Solves jobshop problems.");

        parser.addArgument("-t", "--timeout")
                .setDefault(1L)
                .type(Long.class)
                .help("Solver timeout in seconds for each instance");
        parser.addArgument("--solver")
                .nargs("+")
                .required(true)
                .help("Solver(s) to use (space separated if more than one)");

        parser.addArgument("--instance")
                .nargs("+")
                .required(true)
                .help("Instance(s) to solve (space separated if more than one)");

        Namespace ns = null;
        try {
            ns = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }

        PrintStream output = System.out;

        long solveTimeMs = ns.getLong("timeout") * 1000;

        List<String> solversToTest = ns.getList("solver");
        for(String solverName : solversToTest) {
            if(!solvers.containsKey(solverName)) {
                System.err.println("ERROR: Solver \"" + solverName + "\" is not avalaible.");
                System.err.println("       Available solvers: " + solvers.keySet().toString());
                System.err.println("       You can provide your own solvers by adding them to the `Main.solvers` HashMap.");
                System.exit(1);
            }
        }
        List<String> instances = ns.<String>getList("instance");
        for(String instanceName : instances) {
            if(!BestKnownResult.isKnown(instanceName)) {
                System.err.println("ERROR: instance \"" + instanceName + "\" is not avalaible.");
                System.err.println("       available instances: " + Arrays.toString(BestKnownResult.instances));
                System.exit(1);
            }
        }

        float[] runtimes = new float[solversToTest.size()];
        float[] distances = new float[solversToTest.size()];

        try {
            output.print(  "                         ");;
            for(String s : solversToTest)
                output.printf("%-30s", s);
            output.println();
            output.print("instance size  best      ");
            for(String s : solversToTest) {
                output.print("runtime makespan ecart        ");
            }
            output.println();


        for(String instanceName : instances) {
            int bestKnown = BestKnownResult.of(instanceName);


            Path path = Paths.get("instances/", instanceName);
            Instance instance = Instance.fromFile(path);

            output.printf("%-8s %-5s %4d      ",instanceName, instance.numJobs +"x"+instance.numTasks, bestKnown);

            for(int solverId = 0 ; solverId < solversToTest.size() ; solverId++) {
                String solverName = solversToTest.get(solverId);
                Solver solver = solvers.get(solverName);
                long start = System.currentTimeMillis();
                long deadline = System.currentTimeMillis() + solveTimeMs;
                Result result = solver.solve(instance, deadline);
                long runtime = System.currentTimeMillis() - start;

                if(!result.schedule.isValid()) {
                    System.err.println("ERROR: solver returned an invalid schedule");
                    System.exit(1);
                }

                assert result.schedule.isValid();
                int makespan = result.schedule.makespan();
                float dist = 100f * (makespan - bestKnown) / (float) bestKnown;
                runtimes[solverId] += (float) runtime / (float) instances.size();
                distances[solverId] += dist / (float) instances.size();

                output.printf("%7d %8s %5.1f        ", runtime, makespan, dist);
                output.flush();
            }
            output.println();

        }


        output.printf("%-8s %-5s %4s      ", "AVG", "-", "-");
        for(int solverId = 0 ; solverId < solversToTest.size() ; solverId++) {
            output.printf("%7.1f %8s %5.1f        ", runtimes[solverId], "-", distances[solverId]);
        }



        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
