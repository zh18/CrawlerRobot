package com.mm.server.bins;

import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;
import com.mm.core.Core;
import com.mm.server.Bin;
import com.mm.util.ReadSelector;
import com.mm.util.SYS;
import com.mm.util.SystemUtil;

/**
 * 任务类命令 tk 查看所有任务进行情况 tk -n new task 可以输入连续的任务，用逗号隔开，如果名字错误，则提示输入错误 tk -s id
 * 开始任务号为id的任务，通过tk 查看id tk -sl 开始所有任务 tk -p id 查看id为p的配置文件 tk -a 查看所有准备好被加入的任务
 * 
 * @author zh
 * @version 0.1
 * @since Oct 13,2014
 */
public class Task implements Bin {
	public String getName() {
		return "tk : task";
	}

	/**
	 * 显然这个函数有点长。。。不过逻辑还算清晰，先不改了。留个记号
	 */
	public void run(InputStream is, PrintStream os, String cmd) {
		String name = null;
		Scanner scan = new Scanner(is);
		if (cmd.indexOf("-") != -1) {
			cmd = cmd.trim();
			name = cmd.substring(cmd.lastIndexOf(" "), cmd.length()).trim();
			if (cmd.indexOf("-n") != -1) {
				if (cmd.indexOf(",") != -1) {
					String tasks[] = cmd.split(",");
					// 获得所有已经含有的名字
					List<String> allNames = ReadSelector.getAllNames();
					// 获得想要插入task的名字
					for (String s : tasks) {
						if (s.trim().equals(""))
							continue;
						boolean flag = false;
						for (String temps : allNames) {
							if (temps.equals(s.trim())) {
								flag = true;
								break;
							}
						}
						// 没找到，提示错误信息，并继续下一个名字
						if (!flag) {
							os.println("This name is not right" + s.trim());
							continue;
						}
						// 找到了，则添加
						os.print("Go on the breakpoint ? y or n : ");
						Core.add(s.trim(), scan.next().indexOf("y") != -1);
					}
				} else {
					os.print("Go on the breakpoint ? y or n : ");
					Core.add(name, scan.next().indexOf("y") != -1);
				}
			} else if (cmd.indexOf("-s") != -1) {
				Core.start(name);
			} else if (cmd.indexOf("-a") != -1) {
				Core.showReady(os);
			} else if (cmd.indexOf("-p") != -1) {
				if (name.indexOf("_") == -1) {
					for (String s : SystemUtil.getAllKeys(SYS.PATH
							+ SYS.SYS_DG_SCHEME_FLODER + name.trim()
							+ ".properties")) {
						os.println(s);
					}
				} else
					os.println(ReadSelector.getSelector(name.trim()).toString());
			} else if (cmd.indexOf("-sl") != -1) {
				Core.startAll();
			}
		} else {
			Core.showList(os);
		}
	}
}