package com.mm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mm.logger.Log;

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
	
	public static long dateToLong(String date){
		long result = -1L;
		try {
			result = new SimpleDateFormat().parse(date).getTime();
		} catch (ParseException e) {
			Log.logger.warn("date format error", e);
		}
		return result;
	}
	
	// year month week day hour min second
	public static long englishToLong(String name){
		String times = null;
		if(name.matches("[\\w]") && name.length() > 1) 
			times = name.substring(0, name.length()-1);
		else 
			return -1L;
		try {
			if(name.endsWith("d")){
				return Integer.parseInt(times)*DAY;
			}
			else if(name.endsWith("h")){
				return Integer.parseInt(times)*HOUR;
			}
			else if(name.endsWith("m")){
				return Integer.parseInt(times)*MIN;
			}
			else if(name.endsWith("s")) {
				return Integer.parseInt(times)*SECOND;
			}
		}catch(Exception e){
		}
		return -1;
	}
	
	public static void main(String[] args) {
		long min = System.currentTimeMillis();
		if(min%Times.DAY == 0)
			System.out.println(min);
		else
			System.out.println(getTimes(min, min + Times.DAY - min%Times.DAY));
	}
}
