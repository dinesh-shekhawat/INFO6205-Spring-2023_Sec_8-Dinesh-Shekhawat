package edu.neu.coe.info6205.sort.par;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

import edu.neu.coe.info6205.sort.par.ParallelSortGraphPlotter.Graph;
import edu.neu.coe.info6205.sort.par.ParallelSortGraphPlotter.Group;
import edu.neu.coe.info6205.sort.par.ParallelSortGraphPlotter.Pair;
import edu.neu.coe.info6205.util.LazyLogger;

/**
 * This code has been fleshed out by Ziyao Qiao. Thanks very much.
 * CONSIDER tidy it up a bit.
 */
public class Main {

    private static final LazyLogger logger = new LazyLogger(Main.class);
	
    private static final String FRAME_TITLE = "Parallel Sort Comparison";
    private static final String X_AXIS_TITLE = "Cut Off";
    private static final String Y_AXIS_TITLE = "Time taken (milliseconds)";
    
    private static final String TEMPLATE_GRAPH_TITLE = "Array Size = %s";
    private static final String TEMPLATE_GROUP_NAME = "Thread size = %s";

    private static final int CUTOFF_START = 50000;
    private static final int CUTOFF_END = 100000;
    private static final int CUTOFF_INCREMENT = 1000;

    private static final int ITERATIONS = 10;
    
    private static final int RANDOM_MAX_RANGE = 10000000;
    
	private static final int MILLION = 1000000;
	
	public static void main(String[] args) {
		Random random = new Random();
		
		List<Integer> arraySizes = Arrays.asList(
				2 * MILLION,
				3 * MILLION,
				4 * MILLION,
				5 * MILLION
		);
		
		/*
		List<Integer> arraySizes = Arrays.asList(
				200, 
				300, 
				400,
				500); 
		*/
		
		List<Integer> threadSizes = Arrays.asList(
				(int) Math.pow(2, 0),
				(int) Math.pow(2, 1),
				(int) Math.pow(2, 2),
				(int) Math.pow(2, 3),
				(int) Math.pow(2, 4)
		);
		
		ParallelSortGraphPlotter plotter = new ParallelSortGraphPlotter(
				FRAME_TITLE,
				X_AXIS_TITLE,
				Y_AXIS_TITLE,
				1200,
				800);
		
		Map<Integer, ForkJoinPool> map = new HashMap<>();
		for (int threadSize : threadSizes) {
			ForkJoinPool pool = new ForkJoinPool(threadSize);
			map.put(threadSize, pool);
		}
		
		for (int arraySize : arraySizes) {
			logger.info("########### Computation start for arraySize: " + arraySize);
			
			List<Group> groups = new ArrayList<>(threadSizes.size());
			
			for (int threadSize : threadSizes) { 
				logger.info("---------- Start sorting for threadSize: " + threadSize);
				int[] array = new int[arraySize];
				
				List<Pair> pairs = new ArrayList<>();
				
				for (int cutoff = CUTOFF_START; cutoff <= CUTOFF_END; cutoff += CUTOFF_INCREMENT) {
//					logger.info("++++++++ Start sorting for cutoff: " + cutoff);
					
					ParSort.cutoff = cutoff;
					ParSort.forkJoinPool = map.get(threadSize);
					
					long startTimestamp = System.currentTimeMillis();
					
					for (int t = 0; t < ITERATIONS; ++t) {
						for (int i = 0; i < array.length; i++) array[i] = random.nextInt(RANDOM_MAX_RANGE);
		                ParSort.sort(array, 0, array.length);
					}
					
					long endTimestamp = System.currentTimeMillis();
					long timeTaken = endTimestamp - startTimestamp;
					long averageTimeTaken = timeTaken / ITERATIONS;
					
					logger.info(
							"[METRIC] thread-count: " + threadSize 
							+ ", cutoff: " + cutoff 
							+ ", averageTimeTaken: " + averageTimeTaken);
					
					Pair pair = new Pair(cutoff, averageTimeTaken);
					pairs.add(pair);
				}
				
				String groupName = String.format(TEMPLATE_GROUP_NAME, threadSize);
				Group group = new Group(groupName, pairs);
				groups.add(group);
			}

			String graphName = String.format(TEMPLATE_GRAPH_TITLE, arraySize);
			Graph graph = new Graph(graphName, groups);
			plotter.addGraph(graph);
		}
		
		plotter.plot();
	}
	
	/*
    public static void main(String[] args) {
        processArgs(args);
        System.out.println("Degree of parallelism: " + ForkJoinPool.getCommonPoolParallelism());
        Random random = new Random();
        int[] array = new int[2000000];
        ArrayList<Long> timeList = new ArrayList<>();
        for (int j = 50; j < 100; j++) {
            ParSort.cutoff = 10000 * (j + 1);
            // for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);
            long time;
            long startTime = System.currentTimeMillis();
            for (int t = 0; t < 10; t++) {
                for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);
                ParSort.sort(array, 0, array.length);
            }
            long endTime = System.currentTimeMillis();
            time = (endTime - startTime);
            timeList.add(time);


            System.out.println("cutoff：" + (ParSort.cutoff) + "\t\t10times Time:" + time + "ms");

        }
        try {
            FileOutputStream fis = new FileOutputStream("./src/result.csv");
            OutputStreamWriter isr = new OutputStreamWriter(fis);
            BufferedWriter bw = new BufferedWriter(isr);
            int j = 0;
            for (long i : timeList) {
                String content = (double) 10000 * (j + 1) / 2000000 + "," + (double) i / 10 + "\n";
                j++;
                bw.write(content);
                bw.flush();
            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    private static void processArgs(String[] args) {
        String[] xs = args;
        while (xs.length > 0)
            if (xs[0].startsWith("-")) xs = processArg(xs);
    }

    private static String[] processArg(String[] xs) {
        String[] result = new String[0];
        System.arraycopy(xs, 2, result, 0, xs.length - 2);
        processCommand(xs[0], xs[1]);
        return result;
    }

    private static void processCommand(String x, String y) {
        if (x.equalsIgnoreCase("N")) setConfig(x, Integer.parseInt(y));
        else
            // TODO sort this out
            if (x.equalsIgnoreCase("P")) //noinspection ResultOfMethodCallIgnored
                ForkJoinPool.getCommonPoolParallelism();
    }

    private static void setConfig(String x, int i) {
        configuration.put(x, i);
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final Map<String, Integer> configuration = new HashMap<>();
    */

}
