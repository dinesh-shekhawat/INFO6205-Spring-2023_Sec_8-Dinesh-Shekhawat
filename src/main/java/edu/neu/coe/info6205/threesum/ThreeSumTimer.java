package edu.neu.coe.info6205.threesum;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import edu.neu.coe.info6205.util.Stopwatch;

public class ThreeSumTimer {
	
	private static final String SIZE_TEMPLATE = "------------- Starting Benchmark for N = %s -------------";
	private static final String ALGORITHM_TEMPLATE = "Algorithm: %s, N = %s, Time (milliseconds) = %s)";
	
	private static final String ALGORITHM_CUBIC = "Cubic (N^3)";
	private static final String ALGORITHM_QUADRATITHMIC = "Quadrithmic (N^2 logN)";
	private static final String ALGORITHM_QUADRATIC = "Quadratic (N^2)";
	private static final String ALGORITHM_QUADRATIC_CALLIPER = "Quadratic Calliper (N^2)";
	
	public static void main(String[] args) {
		
		Stopwatch stopwatch = null;
		
		try {
			List<Integer> powers = getPowers(7, 12, 2);
			
			for (int size : powers) {
				String message = String.format(SIZE_TEMPLATE, size);
				System.out.println(message);

				Supplier<int[]> intsSupplier = new Source(size, size + 100, 1L).intsSupplier(10);
		        
				int[] intsCubic = intsSupplier.get();
				int[] intsQuadrithmic = intsSupplier.get();
				int[] intsQuadratic = intsSupplier.get();
				int[] intsQuadraticCallipers = intsSupplier.get();
		        
				/*
				System.out.println(printArray(intsCubic));
				System.out.println(printArray(intsQuadrithmic));
				System.out.println(printArray(intsQuadratic));
				System.out.println(printArray(intsQuadraticCallipers));
				*/
				
				ThreeSum threeSumCubic = new ThreeSumCubic(intsCubic);
				ThreeSum threeSumQuadrithmic = new ThreeSumQuadrithmic(intsQuadrithmic);
				ThreeSum threeSumQuadratic = new ThreeSumQuadratic(intsQuadratic);
				ThreeSum threeSumQuadraticCallipers = new ThreeSumQuadraticWithCalipers(intsQuadraticCallipers);

				stopwatch = new Stopwatch();

				threeSumCubic.getTriples();
				long nanoseconds = stopwatch.lap();
				logAlgorithmTime(ALGORITHM_CUBIC, size, nanoseconds);
				
				threeSumQuadrithmic.getTriples();
				nanoseconds = stopwatch.lap();
				logAlgorithmTime(ALGORITHM_QUADRATITHMIC, size, nanoseconds);
				
				threeSumQuadratic.getTriples();
				nanoseconds = stopwatch.lap();
				logAlgorithmTime(ALGORITHM_QUADRATIC, size, nanoseconds);
				
				threeSumQuadraticCallipers.getTriples();
				nanoseconds = stopwatch.lap();
				logAlgorithmTime(ALGORITHM_QUADRATIC_CALLIPER, size, nanoseconds);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stopwatch != null) {
				stopwatch.close();
			}
		}
	}
	
	private static void logAlgorithmTime(
			String algorithm,
			int n,
			long nanoseconds) {
		String message = String.format(ALGORITHM_TEMPLATE, algorithm, n, nanoseconds);
		System.out.println(message);
	}
	
	/*
	private static String printArray(int[] array) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[");
		
		for (int item : array) {
			stringBuilder.append(item);
			stringBuilder.append(",");
		}
		
		stringBuilder.append("]");
		String result = stringBuilder.toString();
		return result;
	}
	*/
	
	private static List<Integer> getPowers(int low, int high, int base) {
		List<Integer> result = new ArrayList<>(high - low + 1);
		
		for (int i = low; i <= high; ++i) {
			result.add((int) Math.pow(base, i));
		}
		
		return result;
	}
	
}
