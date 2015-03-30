package com.baggagepathfinder.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * The class ShortestPathfinder implements Dijkstra's shortest path
 * algorithm to find shortest path between two nodes in a bidirectional weighted graph.
 * 
 * @author abankey
 */
public class ShortestPathfinder {

	/**
	 * Fetches the shortest path between two nodes.
	 * 
	 * @param source Source Node
	 * 
	 * @param destination Destination Node
	 * 
	 * @return NodePath which contains path and travel time information.
	 */
	public NodePath getShortestPath(Node source, Node destination)
	{
		computeQuickestPath(source);

		List<Node> path = new ArrayList<Node>();
		for (Node node = destination; node != null; node = node.getPrevious())
			path.add(node);
		Collections.reverse(path);
		return new NodePath(path, destination.getMinTravelTime());
	}

	/**
	 * Computes the shortest path to every node in graph from source node.
	 * 
	 * @param sourceNode Source node
	 */
	private void computeQuickestPath(Node sourceNode){
		sourceNode.setMinTravelTime(0d);
		Queue<Node> nodeQueue = new PriorityQueue<Node>();
		nodeQueue.add(sourceNode);

		while(!nodeQueue.isEmpty()){
			Node currentNode = nodeQueue.poll();

			for(Edge edge : currentNode.getEdges()){
				Node adjacentNode = edge.getTargetNode();
				double totalTravelTime = currentNode.getMinTravelTime() + edge.getTravelTime();

				if(totalTravelTime < adjacentNode.getMinTravelTime()){
					nodeQueue.remove(adjacentNode);
					adjacentNode.setMinTravelTime(totalTravelTime);
					adjacentNode.setPrevious(currentNode);
					nodeQueue.add(adjacentNode);
				}
			}
		}
	}

	/**
	 * Resets all input nodes. Executing computeQuickestPath api sets two of the Node's
	 * attributes minTravelTime, previous in the context of Source Node. These attributes 
	 * should be set to their default values before executing computeQuickestPath api again.
	 * 
	 * @param nodeMap NodeMap
	 */
	public void resetAllNodes(Map<String, Node> nodeMap){
		for(Map.Entry<String, Node> entry : nodeMap.entrySet()){
			entry.getValue().reset();
		}
	}

}
