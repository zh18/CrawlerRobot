package com.mm.core;

import java.util.ArrayList;
import java.util.List;

import com.mm.data.struct.Selector;
import com.mm.db.DataBase;
import com.mm.db.dao.ProtectionDao;
import com.mm.logger.Log;
import com.mm.stop.BreakPoint;
import com.mm.util.ReadSelector;
import com.mm.util.SYS;
import com.mm.util.Times;

/**
 *  自动检查每一个类目的更新时间，如果更新时间到了，则把目标插入Core的tasklist中，并开启该任务
 *  <pre>
 *  运行更新程序，并检测已经有的程序文件，把新的类目，append 到以前的旧文件中
 *  first 与 product 检测 旧文件，而download则直接写入 文件夹{@link Idata.hfname} 中
 *  </pre>
 *  
 * @author zh
 * @version 0.1
 * @since Oct 21,2014
 */
public class Refresh implements Runnable {

	private String path = null;
	
	public Refresh(){
		
	}
	
	/**
	 * 检查时间，开启程序的时候，检测每个类目的 <b>更新频率时间 - refresh time  RFT </b> 与
	 *                                    <b>最后更新时间last refresh time LFT</b> 
	 * 
	 * 睡眠时间为                          LFT + RFT - now time 
	 * 以天为单位，如果类目已过期 (now - LFT+RFT > 0) 则把类目加载到Task list中运行
	 * 
	 * 其中 没有在数据库保存断点，最后更新数据的统称为 <i>新数据</i> 新数据不列入更新范围
	 */
	public void run() {
		try {
			while(true){
				synchronized (this) {
					wait(getSleepTime());
					//检测过期时间，如果过期，则
					for(String s:needRefresh()){
						refresh(s);
					}
				}
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
	
	
	public boolean isOld(String name){
		return ReadSelector.isIn(name) && DataBase.getProtection(name) != null;
	}
	
	/**
	 * @param name Amazon_book.download or Amazon_book.all
	 */
	public void refresh(String name){
		String iname = null;
		String part = null;
		if(name.indexOf(".") != -1){
			iname = name.substring(0,name.indexOf("."));
			part = name.substring(name.indexOf(".")+1).trim();
		}
		else {
			iname = name.trim();
		}
		if(!isOld(name)) {
			Log.logger.info(name+" is not an old task , please use task first run it");
			return;
		}
		Core.addRefresh(iname, part);
	}
	
	private List<String> needRefresh(){
		// if now - LFT + RFT > 0   return add to list
		return null;
	}
	
	private long getSleepTime() {
		List<BreakPoint> temp = ProtectionDao.seeAll(); //获取未运行的，与task中的数据做差集
		
		//####################################
		//       未完工
		//####################################
		List<Selector> selectors = new ArrayList<Selector>();
		for(BreakPoint bp:temp){
			selectors.add(ReadSelector.getSelector(bp.getWname()));
		}
		long [] times = new long[temp.size()];
		for(int i=0;i<temp.size();i++){
			times[i] = Times.dateToLong(temp.get(i).getTime())+Times.englishToLong(selectors.get(i).getRft());
		}
		long min = times[0];
		for(int i=1;i<times.length;i++){
			if(min>times[i])
				min = times[i];
		}
		// 如果min是整数天，则直接返回，如果不是整数天，则补数到整数天  以避免无限休眠的状态
		if(min%Times.DAY == 0)
			return min;
		else
			return min + Times.DAY - min%Times.DAY;
	}
	
	
}
