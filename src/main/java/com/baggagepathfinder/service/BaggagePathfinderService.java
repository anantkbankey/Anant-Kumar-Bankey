package com.baggagepathfinder.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baggagepathfinder.exception.SystemException;
import com.baggagepathfinder.graph.Edge;
import com.baggagepathfinder.graph.Node;
import com.baggagepathfinder.graph.NodePath;
import com.baggagepathfinder.graph.ShortestPathfinder;
import com.baggagepathfinder.model.Bag;
import com.baggagepathfinder.model.BaggageClaim;
import com.baggagepathfinder.model.Departure;
import com.baggagepathfinder.model.NodeFactory;
import com.baggagepathfinder.model.Terminal;
import com.baggagepathfinder.utils.FileUtils;

/**
 * The class represents BaggageConveyor system. It is responsible for processing input data, 
 * creates domain objects, identify shortest travel path and time for each baggage.
 
 * @author abankey
 *
 */
public class BaggagePathfinderService {

	public static final String HEADER_CONVEYOR 	= "# Section: Conveyor System";
	public static final String HEADER_DEPARTURE = "# Section: Departures";
	public static final String HEADER_BAG 		= "# Section: Bags";
	public static final String ARRIVAL 			= "ARRIVAL";
	public static final String BAGGAGE_CLAIM 	= "BaggageClaim";
	public static final String TIME_FORMAT 		= "hh:mm";
	public static final String NEW_LINE 		= System.getProperty("line.separator");
	public static final String SPACE 			= " ";

	/**
	 *Departure object repository 
	 */
	private Map<String, Departure> departureMap;

	/**
	 * Bag object repository
	 */
	private List<Bag> bags;

	/**
	 * Node object repository
	 */
	private Map<String, Node> nodeMap;

	public BaggagePathfinderService() {
		init();
	}
	
	/**
	 * Initialize object repositories.
	 */
	private void init(){
		this.departureMap = new HashMap<String, Departure>();
		this.bags = new ArrayList<Bag>();
		this.nodeMap = new HashMap<String, Node>();
	}
	
	/**
	 * It handles input data, initializes the system. For each bag, identifies shortest travel path and travel time 
	 * required. 
	 * 
	 * @param input Input data
	 * 
	 * @return travel path and time for each bag.
	 * 
	 * Example output: 
	 * 
	 * BagId Path                 TravelTime
	 * -------------------------------------
	 * 001   A1 A4 A5 A6          11.0
	 * 003   A5 A6 A7              6.0
	 * 
	 * 
	 * @throws SystemException
	 */
	public String findShortestPath(String input) throws SystemException{
		//Initialize system
		handelData(input);
		
		//Find path 
		StringBuilder output = new StringBuilder();
		
		//Iterate over each bag and find shortest route for each bag.
		for(Bag bag : this.bags){
			StringBuilder lineOutput = new StringBuilder();
			ShortestPathfinder pathFinder = new ShortestPathfinder();
			
			//Reset all nodes. 
			pathFinder.resetAllNodes(nodeMap);
			
			//Find shortest path between source and destination nodes.
			NodePath nodePath = pathFinder.getShortestPath(bag.getSourceNode(), bag.getDestinationNode());
			
			//Prepare output data
			lineOutput.append(bag.getBagId()).append(SPACE);
			for(Node node : nodePath.getPath()){
				lineOutput.append(node.getName()).append(SPACE);
			}
			lineOutput.append(":").append(SPACE).append(nodePath.getTravelTime());
			
			output.append(lineOutput).append(NEW_LINE);
		}
		
		return output.toString();
	}
	
	/**
	 * Extracts different sections from input data and calls respective apis to create domain objects.
	 * 
	 * @param data for conveyor system graph topology, departure schedule, baggage information.
	 * 
	 * Expected data format:
	 * 
	 * # Section: Conveyor System
	 * <conveyor data>
	 * # Section: Departures
	 * <departure data>
	 * # Section: Bags
	 * <baggage data>
	 * 
	 * @throws SystemException
	 */
	private void handelData(String data) throws SystemException{
		
		//Initialize object repositories.
		init();
		
		if(data == null || data.isEmpty() || !data.contains(HEADER_CONVEYOR)
				|| !data.contains(HEADER_DEPARTURE) || !data.contains(HEADER_BAG))
		//If any section is missing, throw exception.
		{

			throw new SystemException("Invalid input data");
		}

		//Extract conveyor data
		String conveyorData = data.substring(data.indexOf(HEADER_CONVEYOR) + HEADER_CONVEYOR.length() + NEW_LINE.length(),
				data.indexOf(HEADER_DEPARTURE) - 1);

		//Extract departure data
		String departureData = data.substring(data.indexOf(HEADER_DEPARTURE) + HEADER_DEPARTURE.length() + NEW_LINE.length(),
				data.indexOf(HEADER_BAG) - 1);

		//Extract baggage data
		String baggageData = data.substring(data.indexOf(HEADER_BAG) + HEADER_BAG.length() + NEW_LINE.length());

		//Parse, validate and create domain objects.
		handleConveyorData(conveyorData);
		handleDepartureData(departureData);
		handleBaggagedata(baggageData);
	}

	/**
	 * Adds source node and destination node in system node graph. Also adjusts the links
	 * between source and destination nodes.
	 * 
	 * @param sourceNode Source Node
	 * 
	 * @param destinationNode Destination Node
	 * 
	 * @param travelTime Travel time between source and destination nodes
	 */
	public void addNode(Node sourceNode, Node destinationNode, double travelTime){
		if(nodeMap.containsKey(sourceNode.getName())){
			sourceNode = nodeMap.get(sourceNode.getName());
		}
		else if(nodeMap.containsKey(destinationNode.getName())){
			destinationNode = nodeMap.get(destinationNode.getName());
		}

		//Set the connector between source and destination nodes.
		Edge sourceEdge = new Edge(destinationNode, travelTime);
		sourceNode.addEdge(sourceEdge);

		Edge destinationEdge = new Edge(sourceNode, travelTime);
		destinationNode.addEdge(destinationEdge);

		//Put the nodes in node repository
		nodeMap.put(sourceNode.getName(), sourceNode);
		nodeMap.put(destinationNode.getName(), destinationNode);

	}
	
	/**
	 * Creates conveyor system nodes. 
	 * 
	 * Expected data format:
	 * 
	 * Terminal1 Terminal2 TravelTime
	 * -------------------------------
	 * A1        A2        4
	 * A2        A5        3
	 * 
	 * @param 	conveyorData conveyor data string
	 * 
	 * @throws 	SystemException 
	 * 		   	Either if any of the line in input data contains less then 3 parameters 
	 * 			or if 3rd parameter of any of the line is not numeric.  
	 */
	public void handleConveyorData(String conveyorData) throws SystemException{
		if(conveyorData == null || conveyorData.isEmpty()){
			return;
		}

		String[] lines = conveyorData.split(NEW_LINE);
		for(String line : lines){
			String[] words = line.split(SPACE);
			if(words.length < 3)
			//Each line is expected to have three parameters.
			{
				throw new SystemException("Invalid input data");
			}
			
			//Create source and destination nodes.
			Node sourceNode = NodeFactory.createNode(words[0]);
			Node destinationNode = NodeFactory.createNode(words[1]);
			try{
				double travelTime = Double.parseDouble(words[2]);
				
				//Add nodes in node repository.
				addNode(sourceNode, destinationNode, travelTime);
			}catch(NumberFormatException n){
				throw new SystemException("Invalid input data");
			}
		}
	}

	/**
	 * Creates departure objects. It does various validations on input data. It should be called 
	 * after see com.baggagepathfinder.service#handleConveyorData
	 * 
	 * Expected data format:
	 * 
	 * FlightId Terminal Destination Time
	 * ----------------------------------
	 * UA10     A1       JFK         8:00
	 * UA12     A4       LAX         9:00
	 * 
	 * @param 	departureData Departure data string
	 * 
	 * @throws 	SystemException
	 * 			Either if any of the line in input data contains less than 4 parameters 
	 * 			or if the 2nd parameter line does not represents valid node.
	 */
	public void handleDepartureData(String departureData) throws SystemException{
		if(departureData == null || departureData.isEmpty()){
			return;
		}

		String[] lines = departureData.split(NEW_LINE);
		for(String line : lines){
			String[] words = line.split(SPACE);
			if(words.length < 4)
			//Each line is expected to have four parameters
			{
				throw new SystemException("Invalid input data");
			}

			String flightId = words[0];
			String nodeName = words[1];
			Terminal terminal;
			if(!nodeMap.containsKey(nodeName))
			//Terminal node doesn't exist. Invalid data.
			{
				throw new SystemException("Terminal " + nodeName + " doesn't exist.");
			}
			else{
				Node node = nodeMap.get(nodeName);
				if(!(node instanceof Terminal))
				//Node exists but not an instance of Terminal. Invalid data.
				{
					throw new SystemException("Terminal " + nodeName + " doesn't exist.");
				}

				terminal = (Terminal)node;
			}

			String destination = words[2];
			String time = words[3];
			Date departureTime;
			SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
			try{
				departureTime = timeFormat.parse(time);
			}catch(ParseException p){
				throw new SystemException("Invalid departure time " + time);
			}
			
			//Extract the hour and minute count from date supplied.
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(departureTime);
			int hour = calendar.get(Calendar.HOUR);
			int minute = calendar.get(Calendar.MINUTE);
			
			//Set the current date from hour and minut extracted above.
			calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.HOUR, hour);
			calendar.set(Calendar.MINUTE, minute);
			calendar.set(Calendar.SECOND, 0);
			
			//Create departure object and put it in object repository.
			Departure departure = new Departure(flightId, terminal, destination, calendar.getTime());
			this.departureMap.put(flightId, departure);
		}
	}

	/**
	 * Creates bag objects and put them in bag repository. It performs validation
	 * on input data. If flight id is ARRIVAL, it looks for BaggageClaim terminal in the node
	 * repository and set BaggageClaim as destination terminal.
	 * 
	 * Expected data format:
	 * 
	 * BagId Terminal FlightId
	 * --------------------------------
	 * 0001  A1       UA10
	 * 0002  A2       UA12
	 * 
	 * @param 	baggageData BaggageData string.
	 * 
	 * @throws 	SystemException if
	 * 			1. Any of the input line contains less than 3 parameters.
	 * 			2. If source Departure Terminal/BaggageClaim/Checkin counter doesn't exist.
	 * 			3. If input contains ARRIVAL and no BaggageClain terminal exist in system.
	 * 			4. Invalid flight id.
	 */
	public void handleBaggagedata(String baggageData) throws SystemException{
		if(baggageData == null || baggageData.isEmpty()){
			return;
		}

		String[] lines = baggageData.split(NEW_LINE);
		for(String line : lines){
			String[] words = line.split(SPACE);
			if(words.length < 3)
			//Each line is expected to have three parameters.
			{
				throw new SystemException("Invalid input data");
			}

			String bagId = words[0];
			String nodeName = words[1];
			Node sourceNode;
			Terminal destinationTerminal;
			if(!nodeMap.containsKey(nodeName))
			//Node with nodeName doesn't exist in the conveyor system. Invalid data.
			{
				throw new SystemException("Terminal/Checkin/BaggageClaim of name " + nodeName + " doesn't exist.");
			}
			else{
				sourceNode = nodeMap.get(nodeName);
			}

			String flightId = words[2];

			if(flightId.equals(ARRIVAL))
			//If flight id is ARRIVAL, then destination terminal should be baggage claim.
			{
				if(nodeMap.containsKey(BAGGAGE_CLAIM)){
					Node node = nodeMap.get(BAGGAGE_CLAIM);
					if(!(node instanceof BaggageClaim))
					//Node with name BaggageClaim should be of type BaggageClaim.
					{
						throw new SystemException("Invalid Data. Inputs contains a bag for ARRIVAL and no " +
								"BaggageClaim terminal exists in system.");
					}
					else{
						destinationTerminal = (BaggageClaim)node;
					}
				}
				else
				//BaggageClaim node not exist in the system and 
				//we have a bag with flight id ARRIVAL. Invalid data.
				{
					throw new SystemException("Invalid Data. Inputs contains a bag for ARRIVAL and no " +
							"BaggageClaim terminal exists in system.");
				}
			}
			else if(this.departureMap.containsKey(flightId))
			//Find the departure with flight id and fetch destination terminal
			{
				Departure departure = departureMap.get(flightId);
				destinationTerminal = departure.getTerminal();
			}
			else	
			//Flight id is not ARRIVAL and it doesn't belong to any of the departure exist in 
		    //system. Invalid data.
			{
				throw new SystemException("Departure with flight name " + flightId + " doesn't exist.");
			}

			//Create bag object and put it in object repository
			Bag bag = new Bag(bagId, sourceNode, destinationTerminal, flightId);
			this.bags.add(bag);
		}
	}

	public static void main(String[] args) throws IOException, SystemException{
		String data = FileUtils.readFileData("/TestInputData_1.txt");
		
		BaggagePathfinderService service = new BaggagePathfinderService();
		String output = service.findShortestPath(data);
		
		System.out.println(output);
	}
}
