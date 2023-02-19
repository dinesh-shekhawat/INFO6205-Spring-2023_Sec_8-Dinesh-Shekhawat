package edu.neu.coe.info6205.sort.par;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ParallelSortGraphPlotter {
	
	private String frameTitle;
	private String xAxis;
	private String yAxis;
	private int width;
	private int height;
	
	private List<Graph> graphs;
	
	public static class Graph {
		private String name;
		private List<Group> groups;
		
		public Graph(String name, List<Group> groups) {
			this.name = name;
			this.groups = groups;
		}
		
		public String getName() {
			return name;
		}
		public List<Group> getGroups() {
			return groups;
		}
	}
	
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
	
	public ParallelSortGraphPlotter(
			String frameTitle,
			String xAxis,
			String yAxis,
			int widht,
			int height) {
		this.frameTitle = frameTitle;
		this.xAxis= xAxis;
		this.yAxis = yAxis;
		this.width = widht;
		this.height = height;
		
		this.graphs = new ArrayList<>();
	}
	
	public void addGraph(Graph graph) {
		graphs.add(graph);
	}
	
	public void plot() {
		JTabbedPane tabbedPane = new JTabbedPane();
		
		for (Graph graph : graphs) {
			XYSeriesCollection collection = new XYSeriesCollection();
			
			List<Group> groups = graph.getGroups();
			
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
			
			String graphName = graph.getName();
			
			JFreeChart chart = ChartFactory.createXYLineChart(
					graphName, 
					xAxis, 
					yAxis, 
					collection);
			
			ChartPanel chartPanel = new ChartPanel(chart);
			
			chartPanel.setVisible(true);
			chartPanel.setSize(width, height);
			
			tabbedPane.addTab(graphName, chartPanel);
		}

		JFrame frame = new JFrame(frameTitle);
		frame.add(tabbedPane);
		frame.setResizable(false);
		frame.setSize(width, height);
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
}