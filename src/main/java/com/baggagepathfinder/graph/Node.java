package com.baggagepathfinder.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Parent class for all the classes who can be part conveyor system graph.
 * 
 * @author abankey
 */
public abstract class Node implements Comparable<Node>{
	
	
	private double minTravelTime = Double.POSITIVE_INFINITY;
	private Set<Edge> edges;
	private Node previous;
	private final String name;
	
	public Node(String name){
		this.edges = new HashSet<Edge>();
		this.name = name;
		this.previous = null;
	}
	
	/**
	 * Compares this object with specified Node object for order.
	 */
	public int compareTo(Node node){
		return Double.compare(this.getMinTravelTime(), node.getMinTravelTime());
	}

	public Node getPrevious() {
		return previous;
	}

	public void setPrevious(Node previous) {
		this.previous = previous;
	}
	
	public double getMinTravelTime() {
		return minTravelTime;
	}

	public void setMinTravelTime(double minTravelTime) {
		this.minTravelTime = minTravelTime;
	}

	public Set<Edge>  getEdges() {
		return edges;
	}

	public void setEdges(Set<Edge>  edges) {
		this.edges = edges;
	}
	
	public void addEdge(Edge edge){
		this.edges.add(edge);
	}
	
	public String getName() {
		return name;
	}

	/**
	 * Resets minTravelTime and previous attributes. 
	 */
	public void reset(){
		minTravelTime = Double.POSITIVE_INFINITY;
		previous = null;
	}
}
