package com.baggagepathfinder.model;

import com.baggagepathfinder.graph.Node;

/**
 * The Bag class represents Baggage data.
 * 
 * @author abankey
 *
 */
public class Bag {
	private String bagId;
	private Node sourceNode;
	private Terminal destinationNode;
	private String flightId;
	
	public Bag(String bagId, Node sourceNode, Terminal destinationNode,
			String flightId) {
	
		this.bagId = bagId;
		this.sourceNode = sourceNode;
		this.destinationNode = destinationNode;
		this.flightId = flightId;
	}

	public String getBagId() {
		return bagId;
	}

	public void setBagId(String bagId) {
		this.bagId = bagId;
	}

	public Node getSourceNode() {
		return sourceNode;
	}

	public void setSourceNode(Node sourceNode) {
		this.sourceNode = sourceNode;
	}

	public Terminal getDestinationNode() {
		return destinationNode;
	}

	public void setDestinationNode(Terminal destinationNode) {
		this.destinationNode = destinationNode;
	}

	public String getFlightId() {
		return flightId;
	}

	public void setFlightId(String flightId) {
		this.flightId = flightId;
	}

}
