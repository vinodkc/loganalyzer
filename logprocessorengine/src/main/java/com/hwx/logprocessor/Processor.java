package com.hwx.logprocessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class Processor {
	private static final Logger logger = Logger.getLogger(Processor.class);

	public static Map<Integer, String> execCommand(String cwd, String... str) {
		
		
		
		logger.info("Command " + str.toString());

		Map<Integer, String> map = new HashMap<Integer, String>();
		ProcessBuilder pb = new ProcessBuilder(str);
		if(cwd.length() > 0) {
			pb.directory(new File(cwd));
		}
		pb.redirectErrorStream(true);
		Process process = null;
		try {
			process = pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

		BufferedReader reader = null;
		if (process != null) {
			reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		}

		String line;
		StringBuilder stringBuilder = new StringBuilder();
		try {
			if (reader != null) {
				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line).append("\n");
					logger.debug(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			if (process != null) {
				process.waitFor();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (process != null) {
			map.put(0, String.valueOf(process.exitValue()));
		}

		try {
			map.put(1, stringBuilder.toString());
		} catch (StringIndexOutOfBoundsException e) {
			if (stringBuilder.toString().length() == 0) {
				return map;
			}
		}
		return map;
	}

}
