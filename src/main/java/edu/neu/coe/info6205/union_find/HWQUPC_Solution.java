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
		int min = 2, max = 2000, step = 1;
		
		List<Integer> inputs = IntStream.iterate(min, i -> i <= max, i -> i + step)
	            .boxed()
	            .collect(Collectors.toList());
		
		List<Pair> diffPairs = new ArrayList<>(inputs.size());
		
		for (int input : inputs) {
			int m = count(input);
			int diff = input - m;
			
			diffPairs.add(new Pair(m, diff));

			System.out.println(input + " -> " + count(input) + " -> " + diff);
		}
		
		HWQUPC_Solution plotter = new HWQUPC_Solution(
				"Union Find Relationship",
				"Generate value from n = " + min + " to " + max + ", increment = " + step,
				"n",
				"m",
				1200,
				800);
		
		Group linerGroup = new Group("n - m", diffPairs);
		
		plotter.addGroup(linerGroup);

		plotter.plot();
	}
	
	public static int count(int n) {
        UF_HWQUPC uf = new UF_HWQUPC(n, true);
        Random random = new Random();

        int count = 0;
        while(uf.components() > 1) {
            int a = random.nextInt(n), b = random.nextInt(n);
            
            if(uf.connected(a, b)) {
            	continue;
            }
            
            uf.union(a, b);
            count++;
        }
        
        return count;
    }
	
	
	
}
