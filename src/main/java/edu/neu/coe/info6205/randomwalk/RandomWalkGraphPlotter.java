package edu.neu.coe.info6205.randomwalk;

import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class RandomWalkGraphPlotter {
	
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
	
	public RandomWalkGraphPlotter(
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
}
