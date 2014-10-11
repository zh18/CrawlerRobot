package com.mm.util;

public class Times {
	public static final long SECOND=1000,
			MIN=60*SECOND,
			HOUR=60*MIN,
			DAY=24*HOUR;
	
	public static String getTimes(long old,long young){
		long min = young-old;
		
		long day = min/DAY;
		long temp = min%DAY;
		long hour = temp/HOUR;
		temp%=HOUR;
		long minuts = temp/MIN;
		temp%=MIN;
		long second = temp/SECOND;
		return (day==0?"":day+" days ")+
				(hour==0?"":hour+" hours ")+
				(minuts==0?"":minuts+" mins ")+
				(second==0?"":second+" seconds");
				
	}
}
