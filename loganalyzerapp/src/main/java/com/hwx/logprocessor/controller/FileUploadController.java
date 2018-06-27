package com.hwx.logprocessor.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.hwx.logprocessor.engine.KafkaLogProcessor;
import com.hwx.logprocessor.plugin.AbsLogProcessor;
import com.hwx.logprocessor.plugin.ILogProcessor;
import com.hwx.logprocessor.vo.Recommendation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Handles requests for the application file upload requests
 */
@Controller
public class FileUploadController {

	private static final Logger logger = Logger.getLogger(FileUploadController.class);

	/**
	 * Upload single file using Spring Controller
	 */
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public @ResponseBody
	String uploadFileHandler(@RequestParam("name") String compName,
			@RequestParam("caseid") String caseId,
			@RequestParam("file") MultipartFile file) {
        String name = file.getOriginalFilename();
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();

				// Creating the directory to store file
				String rootPath = System.getProperty("catalina.home");
				File dir = new File(rootPath +  File.separator +
						"logfiles" + File.separator +
						compName + File.separator + 
						caseId + File.separator +
						UUID.randomUUID());
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath()
						+ File.separator + name);
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();

				logger.info("Server File Location="
						+ serverFile.getAbsolutePath());
				
				//Call logstash script to generate recommendations
				
				//eg: https://stackoverflow.com/questions/525212/how-to-run-unix-shell-script-from-java-code
//				ProcessBuilder pb = new ProcessBuilder("myshellScript.sh", "myArg1", "myArg2");
//				 Map<String, String> env = pb.environment();
//				 env.put("VAR1", "myValue");
//				 env.remove("OTHERVAR");
//				 env.put("VAR2", env.get("VAR1") + "suffix");
//				 pb.directory(new File("myDir"));
//				 Process p = pb.start();
       // read that path and construct recommendations
				
				String parsedOutPut = rootPath +  File.separator +
				"parsedoutput" + File.separator +
				compName + File.separator + 
				caseId + File.separator ;
				
				// + UUID.randomUUID();
				ILogProcessor ilp = new KafkaLogProcessor();
				ilp.loadRecommendations();
				List<Recommendation> recommendations = ilp.generateRecommendations(parsedOutPut);
				return "Recommendations for uploaded file : " + name + " are bla bla bla";
			} catch (Exception e) {
				return "You failed to upload " + name + " => " + e.getMessage();
			}
		} else {
			return "You failed to upload " + name
					+ " because the file was empty.";
		}
	}
}