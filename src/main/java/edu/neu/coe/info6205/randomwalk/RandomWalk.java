/*
 * Copyright (c) 2017. Phasmid Software
 */

package edu.neu.coe.info6205.randomwalk;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.neu.coe.info6205.randomwalk.RandomWalkGraphPlotter.Group;
import edu.neu.coe.info6205.randomwalk.RandomWalkGraphPlotter.Pair;

public class RandomWalk {

	private int x = 0;
	private int y = 0;

	private final Random random = new Random();

	/**
	 * Private method to move the current position, that's to say the drunkard moves
	 *
	 * @param dx the distance he moves in the x direction
	 * @param dy the distance he moves in the y direction
	 */
	private void move(int dx, int dy) {
		x += dx;
		y += dy;
	}

	/**
	 * Perform a random walk of m steps
	 *
	 * @param m the number of steps the drunkard takes
	 */
	private void randomWalk(int m) {
		for (int i = 0; i < m; ++i) {
			randomMove();
		}
	}

	/**
	 * Private method to generate a random move according to the rules of the
	 * situation. That's to say, moves can be (+-1, 0) or (0, +-1).
	 */
	private void randomMove() {
		boolean ns = random.nextBoolean();
		int step = random.nextBoolean() ? 1 : -1;
		move(ns ? step : 0, ns ? 0 : step);
	}

	/**
	 * Method to compute the distance from the origin (the lamp-post where the
	 * drunkard starts) to his current position.
	 *
	 * @return the (Euclidean) distance from the origin to the current position.
	 */
	public double distance() {
		double distance = Math.sqrt(x*x + y*y);
		return distance;
	}

	/**
	 * Perform multiple random walk experiments, returning the mean distance.
	 *
	 * @param m the number of steps for each experiment
	 * @param n the number of experiments to run
	 * @return the mean distance
	 */
	public static double randomWalkMulti(int m, int n) {
		double totalDistance = 0;
		for (int i = 0; i < n; i++) {
			RandomWalk walk = new RandomWalk();
			walk.randomWalk(m);
			totalDistance = totalDistance + walk.distance();
		}
		return totalDistance / n;
	}

	public static void main(String[] args) {
		if (args.length == 0)
			throw new RuntimeException("Syntax: RandomWalk steps [experiments]");
		
		final String COMMA = ",";
		String firstArgument = args[0];
		System.out.println("firstArgument: " + firstArgument);
		
		String secondArgument = args[1];
		System.out.println("secondArgument: " + secondArgument);
		
		boolean containComma = firstArgument.contains(COMMA);
		System.out.println("containComma: " + containComma);
		if (!containComma) {
			int n = 30;
			if (args.length > 1) {
				n = Integer.parseInt(args[1]);
			}
			
			int m = Integer.parseInt(args[0]);
			double meanDistance = randomWalkMulti(m, n);
			System.out.println(m + " steps: " + meanDistance + " over " + n + " experiments");	
		} else {
			System.out.println("Multiple random walk experiments in process...");
			/*
			 * Extra step added to perform the experiments
			 * for multiple values of m.
			 * 
			 * If argument is in form of (x,y)
			 * will perform the experiment starting from x to y
			 * in increment of unit steps.
			 * 
			 * NOTE: x is expected to be less than y
			 * */
			String[] splitArguments = firstArgument.split(COMMA);
			String firstSplitArgument = splitArguments[0];
			String secondSplitArgument = splitArguments[1];
			String thirdSplitArgument = splitArguments[2];
			
			int minimum = Integer.parseInt(firstSplitArgument);
			int maximum = Integer.parseInt(secondSplitArgument);
			int increment = Integer.parseInt(thirdSplitArgument);
			
			int minimumExperiments = -1, maximumExperiments = -1, experimentIncrement = 1;
			
			boolean secondArgumentContainComma = secondArgument.contains(COMMA);
			if (secondArgumentContainComma) {
				String[] experimentSplitArguments = secondArgument.split(COMMA);
				minimumExperiments = Integer.parseInt(experimentSplitArguments[0]);
				maximumExperiments = Integer.parseInt(experimentSplitArguments[1]);
				experimentIncrement = Integer.parseInt(experimentSplitArguments[2]);
			} else {
				int experiments = Integer.parseInt(secondArgument);
				minimumExperiments = experiments;
				maximumExperiments = experiments;
				experimentIncrement = experiments;
			}
			
			String thirdArgument = args[2];
			System.out.println("thirdArgument: " + thirdArgument);
			
			int numberOfSimulations = Integer.parseInt(thirdArgument);
			
			RandomWalkGraphPlotter plotter = new RandomWalkGraphPlotter(
					"Random Walk Experiment",
					"Generate value from m = " + minimum + " to " + maximum + ", increment = " + increment,
					"m",
					"d",
					1200,
					800);

			List<Pair> squareRootPairs = new ArrayList<>();

			for (int m = minimum; m <= maximum; m += increment) {
				double squareRoot = Math.sqrt(m);
				Pair squareRootPair = new Pair(m, squareRoot);
				squareRootPairs.add(squareRootPair);	
			}
			
			Group sqaureRootGroup = new Group("sqrt(m)", squareRootPairs);
			plotter.addGroup(sqaureRootGroup);
			
			for (int n = minimumExperiments; n <= maximumExperiments; n += experimentIncrement) {
				List<Pair> pairs = new ArrayList<>();
				
				for (int m = minimum; m <= maximum; m += increment) {
					
					double totalDistance = 0;
					for (int i = 0; i < numberOfSimulations; ++i) {
						double distance = randomWalkMulti(m, n);
						totalDistance += distance;
					}

					double meanDistance = totalDistance / numberOfSimulations;
					System.out.println("On Average: " + m + " steps: " + meanDistance + " over " + n + " experiments");
					Pair pair = new Pair(m, meanDistance);
					pairs.add(pair);
				}
				
				Group group = new Group("n = " + n, pairs);
				plotter.addGroup(group);
			}

			plotter.plot();
		}
		
	}

}
