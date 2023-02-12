package edu.neu.coe.info6205.union_find;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class HWQUPC_Solution {
	
	private String frameTitle;
	private String graphTitle;
	private String xAxis;
	private String yAxis;
	private int width;
	private int height;
	
	private List<Group> groups;
	
	public static class Group {
		private String name;
		private List<Pair> pairs;
		
		public Group(String name, List<Pair> pairs) {
			this.name = name;
			this.pairs = pairs;
		}

		public String getName() {
			return name;
		}
		public List<Pair> getPairs() {
			return pairs;
		}
		
	}
	
	public static class Pair {
		private double x;
		private double y;

		public Pair(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public double getX() {
			return x;
		}
		public double getY() {
			return y;
		}
	}
	
	public HWQUPC_Solution(
			String frameTitle,
			String graphTitle,
			String xAxis,
			String yAxis,
			int widht,
			int height) {
		this.frameTitle = frameTitle;
		this.graphTitle = graphTitle;
		this.xAxis= xAxis;
		this.yAxis = yAxis;
		this.width = widht;
		this.height = height;
		
		this.groups = new ArrayList<>();
	}
	
	public void addGroup(Group group) {
		groups.add(group);
	}
	
	public void plot() {
		XYSeriesCollection collection = new XYSeriesCollection();
		
		for (Group group : groups) {
			String name = group.getName();

			XYSeries series = new XYSeries(name);
			
			List<Pair> pairs = group.getPairs();
			for (Pair pair : pairs) {
				double x = pair.getX();
				double y = pair.getY();
				
				series.add(x, y);
			}
			
			collection.addSeries(series);
		}
		
		JFreeChart chart = ChartFactory.createXYLineChart(
				graphTitle, 
				xAxis, 
				yAxis, 
				collection);
		
		ChartFrame chartFrame = new ChartFrame(frameTitle, chart);
		chartFrame.setVisible(true);
		chartFrame.setResizable(false);
		chartFrame.setSize(width, height);
	}
	
	public static void main(String[] args) {
		int min = 2, max = 2000, step = 1, simulations = 10;
		
		List<Integer> inputs = IntStream.iterate(min, i -> i <= max, i -> i + step)
	            .boxed()
	            .collect(Collectors.toList());
		
		List<Pair> pairs = new ArrayList<>(inputs.size());
		List<Pair> pairsCompressed = new ArrayList<>(inputs.size());
		List<Pair> logPairs = new ArrayList<>(inputs.size());
		List<Pair> linearPairs = new ArrayList<>(inputs.size());

		for (int input = 100; input < 1000000; input *= 2) {
			double m = count(input, simulations, false);
			double mCompressed = count(input, simulations, true);
			
			double log2Value = log2(input);
			double log = ((double) input) * log2Value / 2;
			
			pairs.add(new Pair(input, m));
			pairsCompressed.add(new Pair(input, mCompressed));
			logPairs.add(new Pair(input, log));
			linearPairs.add(new Pair(input, input));
			
			System.out.println(input 
					+ " m -> " + m
					+ " mCompressed -> " + mCompressed
					+ " -> log = " + log + 
					" -> log2Value = " + log2Value);
		}
		
		HWQUPC_Solution plotter = new HWQUPC_Solution(
				"Union Find Relationship",
				"Generate value from n = " + min + " to " + max,
				"n",
				"m",
				1200,
				800);
		
//		Group linerGroup = new Group("n - m", diffPairs);
		Group group = new Group("m", pairs);
		Group compressedGroup = new Group("m (compressed)", pairsCompressed);
		Group logGroup = new Group("n * lg(n)", logPairs);
		Group linearGroup = new Group("n", linearPairs);
		
//		plotter.addGroup(linerGroup);
		plotter.addGroup(group);
		plotter.addGroup(compressedGroup);
		plotter.addGroup(logGroup);
		plotter.addGroup(linearGroup);

		plotter.plot();
	}
	
	private static double log2(int N) {
		double result = (Math.log10(N) / Math.log10(2));
        return result;
    }
	
	public static double count(
			int n, 
			int simulations,
			boolean pathCompression) {
        Random random = new Random();
        double total = 0;
        
        for (int i = 0; i < simulations; i++) {
            UF_HWQUPC uf = new UF_HWQUPC(n, pathCompression);

            double count = 0;
            while(uf.components() > 1) {
            	int a = random.nextInt(n), b = random.nextInt(n);
            	count++;

            	if(uf.connected(a, b)) {
            		continue;
            	}

            	uf.union(a, b);
            }

            total += count;
//            System.out.println("total increase to: " + total);
        }
       
        double average = total / (double) simulations;
//        System.out.println("average: " + average);
        return average;
    }
	
}
