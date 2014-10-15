/**
 * @author public
 *
 */
package com.mm.exception;

public class MulGoOutException extends RuntimeException {
	
	public MulGoOutException() {
		
	}
	
	public MulGoOutException(String name){
		System.out.println(name+" had go out");
	}
}