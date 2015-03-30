package com.baggagepathfinder.model;

import java.util.Date;

/**
 * The Departure class represents Departure schedule data
 * 
 * @author abankey
 *
 */
public class Departure {
	
	private String flightId;
	private Terminal terminal;
	private String destination;
	private Date departureTime;
	
	public Departure(String flightId, Terminal terminal, String destination,
			Date departureTime) {
		
		this.flightId = flightId;
		this.terminal = terminal;
		this.destination = destination;
		this.departureTime = departureTime;
	}

	public String getFlightId() {
		return flightId;
	}

	public void setFlightId(String flightId) {
		this.flightId = flightId;
	}

	public Terminal getTerminal() {
		return terminal;
	}

	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Date getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(Date departureTime) {
		this.departureTime = departureTime;
	}

}
