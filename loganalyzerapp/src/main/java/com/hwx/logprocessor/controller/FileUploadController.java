package com.hwx.logprocessor.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.hwx.logprocessor.engine.KafkaLogProcessor;
import com.hwx.logprocessor.plugin.AbsLogProcessor;
import com.hwx.logprocessor.plugin.ILogProcessor;
import com.hwx.logprocessor.plugin.PluginFactory;
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
	public @ResponseBody String uploadFileHandler(@RequestParam("name") String compName,
			@RequestParam("caseid") String caseId, @RequestParam("file") MultipartFile file) {
		String name = file.getOriginalFilename();
		if (!file.isEmpty()) {
			ILogProcessor ilp = PluginFactory.getPlugin(compName);

			if (ilp == null) {
				return "Unsupported component or Plugin missing " + compName ;
			} else {
				return processRequest(compName, caseId, file, name, ilp);
			}

		} else {
			return "You failed to upload " + name + " because the file was empty.";
		}
	}

	private static String processRequest(String compName, String caseId, MultipartFile file, String name, ILogProcessor ilp) {
		try {
			byte[] bytes = file.getBytes();

			// Creating the directory to store file
			String randomStr = UUID.randomUUID().toString();
			String rootPath = System.getProperty("catalina.home") + File.separator + "loganalysis";
			String baseFilePath = rootPath + File.separator + "userfiles" + File.separator + compName
					+ File.separator + caseId + File.separator + randomStr;

			File inputDir = new File(baseFilePath + File.separator + "input");
			File outputDir = new File(baseFilePath + File.separator + "output");
			if (!inputDir.exists())
				inputDir.mkdirs();

			// Create the file on server
			File inputFile = new File(inputDir.getAbsolutePath() + File.separator + name);
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(inputFile));
			stream.write(bytes);
			stream.close();

			String inputFilePath = inputFile.getAbsolutePath();

			logger.info("Server File Location=" + inputFilePath);

			if (!outputDir.exists())
				outputDir.mkdirs();
			
			String outputDirPath = outputDir.getAbsolutePath();

			String parserPath = rootPath + File.separator + "parser" + File.separator + "parserscript.sh";

			logger.info("Started " + parserPath);

			// Call logstash script to generate parsed file

			ProcessBuilder pb = new ProcessBuilder(parserPath, inputFilePath, outputDirPath);
			Process process = pb.start();
			int errCode = process.waitFor();
			logger.debug("command executed, any errors? " + (errCode == 0 ? "No" : "Yes"));
			logger.debug("Output:\n" + output(process.getInputStream()));
			logger.info("Finished Processing " + parserPath);

			String parsedFilePath = outputDirPath + File.separator + "parsedfile.csv";
			// call processor for the Component's Plugin

			ilp.loadRecommendations();
			List<Recommendation> recommendations = ilp.generateRecommendations(parsedFilePath);
			

			  String s =  ""+"Recommendations for caseid  " + caseId  + " - uploaded file : " + name +  ""
		               +" <table border ='1'>"
		               +"<tr>"                           
		               +"<td>Key</td>"
		               +"<td>Level</td>"       
		               +"<td>Resolution</td> "                              
		               +"</tr> ";
			  
		    for(Recommendation rec : recommendations) {
		        s = s.concat( "<tr>"
		                +"<td>"+rec.getKey()+"</td>"
		                +"<td>"+rec.getLevel()+"</td> "
		               +"<td>"+rec.getResolution()+"</td> "
		               +"</tr> ");
		    }
		    s=s.concat( "</table>");
		    return s;

		} catch (Exception e) {
			return "You failed to upload " + name + " => " + e.getMessage();
		}
	}

	private static String output(InputStream inputStream) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(inputStream));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line + System.getProperty("line.separator"));
			}
		} finally {
			br.close();
		}
		return sb.toString();
	}
}