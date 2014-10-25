package com.mm.core;

import com.mm.logger.Log;
import com.mm.util.SystemUtil;

/**
 * 实现对增加线程以及减少线程的自动化管理
 * [守护进程]
 * <pre>
 *  轮流检查现有任务，并根据计算规则对任务进行分析，每隔一段hint对pots进行增加或减少或不变 , 
 *  
 *  1：<b>增加</b> , 下载网页数(downnumbers)/下载时间(hint) > 上一次的上式值%50
 *  2：<b>减少</b> ，                                     <
 *  3: <b>不变</b> , 50% > 值 > -50% 时
 *  
 * </pre>
 * @author public
 *
 */
public class AutoPotManagent implements Runnable {
	// 默认从setting文件中获取值
	private long hint = 0L;
	private int crate; // change rate;
	
	public AutoPotManagent() {
		try {
			setHint(SystemUtil.readProperties("setting", "hint"));
			
		}catch(Exception e){
			Log.logger.error("read hint error", e);
		}
	}
	
	public void run(){
		while(true){
			try {
				Thread.sleep(hint);
			}catch(Exception e){
				e.printStackTrace();
			}
			for(Task t:Core.getAllRunningTask()){
				autoPots(t);
			}
		}
		
	}
	
	public void autoPots(Task t){
		
	}

	
	
	/**
	 * 默认毫秒
	 * 1s = 1000 毫秒
	 * 1m = 1分钟*60s
	 * 1h = 1小时*60m
	 * 1d = 1天*24h
	 * @param s
	 */
	public void setHint(String s){
		
	}
	
	/**
	 * 只接受10～100内的数字
	 * @param num
	 */
	public void setCrate(String num){
		
	}
}
