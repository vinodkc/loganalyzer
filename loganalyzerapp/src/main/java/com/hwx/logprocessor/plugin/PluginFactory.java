package com.hwx.logprocessor.plugin;

import java.io.File;

import com.hwx.logprocessor.PropertyFactory;
import com.hwx.logprocessor.engine.KafkaLogProcessor;

public class PluginFactory {
	
	public static ILogProcessor getPlugin(String pluginName) {
		if ("kafka".equalsIgnoreCase(pluginName)) {
			String recommendationfile = PropertyFactory.getComponentProperties(pluginName).getProperty("recommendationfile");
		return new KafkaLogProcessor(recommendationfile);
	} else {
		return null ; // new KafkaLogProcessor("path");
	}
	}

}
