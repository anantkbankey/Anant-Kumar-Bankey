package com.baggagepathfinder.model;

import com.baggagepathfinder.graph.Node;

/**
 * The NodeFactory class represents the factory which creates instances of 
 * Node based on name supplied. This logic can be changed later 
 * based on actual implementation.
 * 
 * @author abankey
 */
public class NodeFactory {
	
	/**
	 * Creates specific instance of Node based of name attribute.
	 * 
	 * @param name Node name
	 * @return
	 */
	public static Node createNode(String name){
		if(name.toLowerCase().contains("ticketing") || name.contains("checkin")){
			return new CheckinCounter(name);
		}
		else if(name.toLowerCase().contains("baggage")){
			return new BaggageClaim(name);
		}
		else{
			return new Terminal(name);
		}
	}

}
