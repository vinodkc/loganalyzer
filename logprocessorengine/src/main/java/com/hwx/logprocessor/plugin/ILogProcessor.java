package com.hwx.logprocessor.plugin;

import java.util.List;
import java.util.Map;

import com.hwx.logprocessor.vo.Recommendation;

public interface ILogProcessor {

	void loadRecommendations();

	List<Recommendation> generateRecommendations(String parsedOutput);

}
