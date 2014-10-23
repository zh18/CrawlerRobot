package com.mm.util;

import java.io.BufferedWriter;
import java.io.IOException;

public class FileC {
	
	public synchronized void write(BufferedWriter bw,String line) throws IOException {
		bw.write(line);
	}
	
	
	
}
