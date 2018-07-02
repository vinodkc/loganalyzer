package com.hwx.logprocessor.plugin;

import java.io.File;

import com.hwx.logprocessor.engine.KafkaLogProcessor;

public class PluginFactory {
	
	public static ILogProcessor getPlugin(String pluginName) {
		if ("kafka".equalsIgnoreCase(pluginName)) {
		return new KafkaLogProcessor(System.getProperty("catalina.home") + File.separator + "loganalysis/recommendationdb/kafka/kafkasolutions.csv");
	} else {
		return null ; // new KafkaLogProcessor("path");
	}
	}

}
