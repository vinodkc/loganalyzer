package com.hwx.logprocessor.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.hwx.logprocessor.plugin.AbsLogProcessor;
import com.hwx.logprocessor.vo.LogData;
import com.hwx.logprocessor.vo.LogTuple;
import com.hwx.logprocessor.vo.Recommendation;

public class KafkaLogProcessor extends  AbsLogProcessor {

	private String recommendationFilePath;

	public KafkaLogProcessor(String recommendationFilePath) {
     this.recommendationFilePath = recommendationFilePath;
	}

	private static List<Recommendation> generateRecommendationsHelper(
			String fileName,
			Map<String, List<Recommendation>> recommendationMap) {
		File f2 = new File(fileName);
		List<Recommendation> recommendationsList = new ArrayList<Recommendation>();
        try {
            FileInputStream logstashoutput = new FileInputStream(f2);
            Scanner sc = new Scanner(logstashoutput);

            Set<LogTuple> recSet = new HashSet<LogTuple>();

            while (sc.hasNextLine()) {

                String logstashlines = sc.nextLine();
                //          System.out.println(logstashlines);

                String[] splitlog = logstashlines.split("\\|");
                //           System.out.println(splitlog.length);
                if (splitlog.length < 8) {

                    System.out.println(" ERROR, less than 8 fields in : " +logstashlines);
                    continue;
                }

                String pl = splitlog[0];
                String ll = splitlog[1];
                String etl = splitlog[2];
                String ecl = splitlog[3];
                String edl = splitlog[4];
                String ccl = splitlog[5];
                String cdl = splitlog[6];
                String erdl = splitlog[7];


                LogData ld = new LogData(pl, etl, ll, ecl, edl, ccl, cdl, erdl);

                String key = ld.getKey();

                if (recommendationMap.containsKey(key)) {
                    recSet.add(new LogTuple(recommendationMap.get(key), ld));

                }else {
                    System.out.println(ld + " Missing in DB ");
                  //  System.out.println(ld.getKey());

                }


            }
            if (recSet.size() == 0) {
                System.out.println("There are no Recommendations");

            } else {
                for (LogTuple tpl : recSet) {
                    List<Recommendation> lst = tpl.getRecList();
                    LogData ld = tpl.getLd();
  //                  System.out.println(lst.size());
                    int index = 1;
                    for (Recommendation rec : lst) {

                    	recommendationsList.add(rec);
                        System.out.println("Recommendation " + index++ + " -> " + rec);
                    }

                }
            }

        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
		return recommendationsList;
	}

	public void loadRecommendations() {
		this.loadRecommendations(recommendationFilePath);
	}
	
	public List<Recommendation> generateRecommendations(String parsedOutput) {
		return generateRecommendationsHelper(parsedOutput, recommendations);
	}

}
