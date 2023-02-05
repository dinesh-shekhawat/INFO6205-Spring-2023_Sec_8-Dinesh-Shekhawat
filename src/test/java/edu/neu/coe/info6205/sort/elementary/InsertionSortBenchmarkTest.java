package edu.neu.coe.info6205.sort.elementary;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.neu.coe.info6205.sort.GenericSort;
import edu.neu.coe.info6205.util.Benchmark_Timer;
import edu.neu.coe.info6205.util.LazyLogger;

public class InsertionSortBenchmarkTest {

    private static final LazyLogger logger = new LazyLogger(InsertionSortBenchmarkTest.class);

    private static final String DESCRIPTION_TEMPLATE = "Insertion sort: %s: N = %s";
    
    private static final String ARRAY_TYPE_ORDERED = "Ordered Array";
    private static final String ARRAY_TYPE_RANDOM_ORDERED = "Random Ordered Array";
    private static final String ARRAY_TYPE_PARTIALLY_ORDERED = "Partially Ordered Array";
    private static final String ARRAY_TYPE_REVERSE_ORDERED = "Reverse Ordered Array";

    private static final int START = 1000;
    
    private static final int ITERATIONS = 5;
    
    private static final int WARM_UPS = 10;
    
    private static final int RUNS = 100;
    
    @BeforeClass
    public static void beforeClass() {
    	logger.info(">>>>>> All benchmarking tests will start now");
    }
    
    @AfterClass
    public static void afterClass() {
    	logger.info("All benchmarking tests done >>>>>>>>>>");
    }
    
    @Test
    public void randomOrderArrayTest() {
    	logger.info("Starting benchmarking of insertion sort tests with random ordered array");
    	
    	Function<Integer, Supplier<Integer[]>> randomSupplierBuilder = (length) -> {
    		Supplier<Integer[]> orderedSupplier = () -> {
    			Random random = new Random();
    			Integer[] array = new Integer[length];
        		for (int j = 0; j < length; j++) {
        			array[j] = random.nextInt();
        		}
        		return array;
        	};
        	
        	return orderedSupplier;
    	};
    	
    	benchmarkTest(randomSupplierBuilder, ARRAY_TYPE_RANDOM_ORDERED);
    }
    
    @Test
    public void orderedArrayTest() {
    	logger.info("Starting benchmarking of insertion sort tests with ordered array");
    	
    	Function<Integer, Supplier<Integer[]>> orderedSupplierBuilder = (length) -> {
    		Supplier<Integer[]> orderedSupplier = () -> {
    			Integer[] array = new Integer[length];
        		for (int j = 0; j < length; j++) {
        			array[j] = j;
        		}
        		return array;
        	};
        	
        	return orderedSupplier;
    	};
    	
    	benchmarkTest(orderedSupplierBuilder, ARRAY_TYPE_ORDERED);
    }
    
    @Test
    public void partiallyOrderedArrayTest() {
    	logger.info("Starting benchmarking of insertion sort tests with partially ordered array");
    	
    	Function<Integer, Supplier<Integer[]>> reverseOrderedSupplierBuilder = (length) -> {
    		Supplier<Integer[]> orderedSupplier = () -> {
    			Random random = new Random();
    			Integer[] array = new Integer[length];
    			
    			for (int i = 0; i < length / 2; i++) {
    				array[i] = i;
    			}

    			for (int i = length/2; i < length; i++) {
    				array[i] = random.nextInt();
    			}
    				
        		return array;
        	};
        	
        	return orderedSupplier;
    	};
    	
    	benchmarkTest(reverseOrderedSupplierBuilder, ARRAY_TYPE_PARTIALLY_ORDERED);
    }
    
    @Test
    public void reverseOrderedArrayTest() {
    	logger.info("Starting benchmarking of insertion sort tests with reverse ordered array");
    	
    	Function<Integer, Supplier<Integer[]>> reverseOrderedSupplierBuilder = (length) -> {
    		Supplier<Integer[]> orderedSupplier = () -> {
    			Integer[] array = new Integer[length];
        		for (int j = 0; j < length; j++) {
        			array[j] = (length - j);
        		}
        		return array;
        	};
        	
        	return orderedSupplier;
    	};
    	
    	benchmarkTest(reverseOrderedSupplierBuilder, ARRAY_TYPE_REVERSE_ORDERED);
    }
    
    private void benchmarkTest(
    		Function<Integer, Supplier<Integer[]>> supplierBuilder,
    		String arrayOrderType) {
    	for (int n = START, i = 0; i < ITERATIONS; n *= 2, i++) {
    		Supplier<Integer[]> orderedSupplier = supplierBuilder.apply(n);
        	
        	String description = getDescriptionName(arrayOrderType, n);
        	
        	GenericSort<Integer> insertionSort = new InsertionSort<>(); 
        	
        	Consumer<Integer[]> function = array -> insertionSort.sort(array);
        	
        	Benchmark_Timer<Integer[]> benchmarkTimer = new Benchmark_Timer<>(description, function);
        	
        	/* 
        	 * edu.neu.coe.info6205.util.Benchmark_Timer.runFromSupplier(Supplier<T>, int)
        	 * 
        	 * Code inside this already takes care of warm up phase
        	 * 
        	 * but still getting high time for first iteration so doing
        	 * runs explicitly for warm up
        	 * */
        	for (int counter = 0; counter < WARM_UPS; counter++) {
        		function.accept(orderedSupplier.get());
        	}
        	
        	double time = benchmarkTimer.runFromSupplier(orderedSupplier, RUNS);
        	logger.info(description + ", Benchmarking Result: " + time);
    	}
    }
    
    private String getDescriptionName(String orderingType, int runs) {
    	String result = String.format(DESCRIPTION_TEMPLATE, orderingType, runs);
    	return result;
    }
    
}