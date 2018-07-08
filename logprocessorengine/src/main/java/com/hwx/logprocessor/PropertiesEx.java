package com.hwx.logprocessor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesEx  extends Properties{
	public PropertiesEx(String fileName) {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
		if (inputStream == null) {
			System.out.println("Sorry, unable to find " + fileName);
			return;
		} else {
			try {
				this.load(inputStream);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}

}
