package com.baggagepathfinder.graph;

import java.util.List;

/**
 * This class represents the path between 2 nodes in terms of intermittent nodes and required 
 * travel time. This is an immutable class. NodePath object once created cannot be modified. 
 
 * @author abankey
 *
 */
public class NodePath {

	private final List<Node> path;
	private final double travelTime;
	
	public NodePath(List<Node> path, double travelTime) {
		this.path = path;
		this.travelTime = travelTime;
	}
	
	public List<Node> getPath() {
		return path;
	}

	public double getTravelTime() {
		return travelTime;
	}

}
