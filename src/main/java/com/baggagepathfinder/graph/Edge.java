package com.baggagepathfinder.graph;

/**
 * The Edge class represents a Connector which has a associated travel 
 * time and can connect two end points. 
 * 
 * @author abankey
 *
 */
public class Edge {
	
	private final Node targetNode;
	private final double travelTime;
	
	public Edge(Node targetNode, double travelTime) {
		super();
		this.targetNode = targetNode;
		this.travelTime = travelTime;
	}

	public Node getTargetNode() {
		return targetNode;
	}

	public double getTravelTime() {
		return travelTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((targetNode == null) ? 0 : targetNode.hashCode());
		long temp;
		temp = Double.doubleToLongBits(travelTime);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		if (targetNode == null) {
			if (other.targetNode != null)
				return false;
		} else if (!targetNode.equals(other.targetNode))
			return false;
		if (Double.doubleToLongBits(travelTime) != Double
				.doubleToLongBits(other.travelTime))
			return false;
		return true;
	}

}
