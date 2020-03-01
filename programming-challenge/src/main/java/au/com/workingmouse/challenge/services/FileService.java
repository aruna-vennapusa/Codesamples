package au.com.workingmouse.challenge.services;

import org.apache.log4j.Logger;

import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
public class FileService {

    private static final Logger LOGGER = Logger.getLogger(FileService.class);

    public static List<String[]> readLines(File file) throws IOException, ParseException {
        LOGGER.trace("Reading lines for file " +  file.getName());

        List<String[]> lines = new ArrayList<>();
      
      
        try(CSVReader reader = new CSVReader(new FileReader(file))){
        	 String[] line;
             while((line = reader.readNext()) != null) {
            	  lines.add(line);
               }
             
             reader.close();
        }

           return lines;
    }
}
