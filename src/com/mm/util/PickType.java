/**
 * @author public
 *
 */
package com.mm.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class PickType {
	
	static List<String> all = new ArrayList<String>();
		
	private final static int MAX_F = 30;
	
	public static void pickfile(File file,Exe e) throws Exception {
		List<String> temp = null;
		for(File f:file.listFiles()){
			if (f.isDirectory()){
				pickfile(f,e);
			}
			else if (f.getName().indexOf(e.getName()) != -1){
				e.exe(f);

			}
		}
	}
}