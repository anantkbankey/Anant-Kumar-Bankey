package com.baggagepathfinder.exception;

/**
 * The class SystemException is a form of Throwable that indicates 
 * conditions that a reasonable application might want to catch. 
 * 
 * @author abankey
 *
 */
public class SystemException extends Exception{
	
	public SystemException(String message){
		super(message);
	}
}
