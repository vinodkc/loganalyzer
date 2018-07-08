package com.hwx.logprocessor;

import java.util.Properties;

public class PropertyFactory {
static Properties globalProperties = new PropertiesEx("global.properties");

public static Properties getGlobalProperties() {
	return globalProperties;
}

public static Properties getCompoentProperties(String component) {
	return null;
}
}
