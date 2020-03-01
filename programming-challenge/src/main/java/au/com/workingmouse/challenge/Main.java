package au.com.workingmouse.challenge;

import au.com.workingmouse.challenge.config.Configuration;
import au.com.workingmouse.challenge.models.VelocityAndDirectionData;
import au.com.workingmouse.challenge.services.FileService;
import au.com.workingmouse.challenge.services.VelocityAndDirectionService;
import org.apache.log4j.Logger;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Main {

	private static final Logger LOGGER = Logger.getLogger(Main.class);
	protected static final String OUTPUT_FILENAME = "index.html";

	public static void main(String[] args) throws IOException {

		try {
			Configuration.load();

			var lines = loadAndParseFile();
			String html = VelocityAndDirectionService.summarise(lines);

			writeFile(html);

			//throw new SecurityException("Segmentation fault");

		} catch (Exception e) {
			LOGGER.error("Failed to run application", e);
		} 
	}

	protected static List<VelocityAndDirectionData> loadAndParseFile() throws Exception  {
		List<String[]> lines = FileService.readLines(Configuration.getImportFile());

		List<VelocityAndDirectionData> parsedLines = VelocityAndDirectionService.parseLines(lines);

		return parsedLines;
	}


	protected static void writeFile(String html) throws IOException {
		/*try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(OUTPUT_FILENAME))) {
			bufferedWriter.write(html);
		}*/
		File file = new File(OUTPUT_FILENAME);
      
       
	        if(Desktop.isDesktopSupported()){
	        	Files.write(file.toPath(), html.getBytes());
	            Desktop.getDesktop().browse(file.toURI());
	        	
	        }
        
	}
}