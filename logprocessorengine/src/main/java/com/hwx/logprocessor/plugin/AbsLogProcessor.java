package com.hwx.logprocessor.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.hwx.logprocessor.vo.Recommendation;

public abstract class AbsLogProcessor implements ILogProcessor {
  
	protected Map<String, List<Recommendation>>  recommendations;

	protected void loadRecommendations(String dbFile) {
		recommendations =  readRecommendationFromDB(new File(dbFile));
	}
	
	
	 private static Map<String, List<Recommendation>> readRecommendationFromDB(File f1) {
	        Map<String, List<Recommendation>> rmap = new HashMap<String, List<Recommendation>>();

	        try {
	            FileInputStream fin = new FileInputStream(f1);
	            Scanner sc = new Scanner(fin);

	            while (sc.hasNextLine()) {

	                String lines = sc.nextLine();

	                String[] split = lines.split("\\|");
//	                    System.out.println(split.length);
	                if (split.length < 8) {
	                    continue;
	                }
	                String k = split[0];
//	                    System.out.println(k);
	                String l = split[1];
//	                    System.out.println(l);
	                String ec = split[2];
//	                    System.out.println(ec);
	                String ed = split[3];
//	                    System.out.println(ed);
	                String cc = split[4];
//	                    System.out.println(cc);
	                String cd = split[5];
//	                    System.out.println(cd);
	                String erd = split[6];
	                //                   System.out.println(erd);
	                String rr = split[7];
//	                    System.out.println(rr);

	   /*              String line = r.getLine();
	                 String[] fields = line.split(",");*/

	                Recommendation rec = new Recommendation(k, l, ec, ed, cc, cd, rr);
	                List<Recommendation> listRec = rmap.get(rec.getKey());
	                if (listRec == null) {

	                    listRec = new ArrayList<Recommendation>();
	                    rmap.put(rec.getKey(), listRec);

	                }

	                listRec.add(rec);

	            }
	        } catch (FileNotFoundException e) {
	            System.out.println(e);
	        }

	        return rmap;

	    }
	 
}
