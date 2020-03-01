package au.com.workingmouse.challenge.config;

import lombok.Getter;
import org.apache.log4j.Logger;



import eu.trentorise.opendata.jackan.CkanClient;
import eu.trentorise.opendata.jackan.exceptions.CkanException;
import eu.trentorise.opendata.jackan.model.CkanDataset;
import eu.trentorise.opendata.jackan.model.CkanResource;
import eu.trentorise.opendata.traceprov.internal.org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

public class Configuration {

    private static final Logger LOGGER = Logger.getLogger(Configuration.class);

    private static final String CONFIG_FILE = "config.properties";

    @Getter
    private static File importFile;

    public static void load() throws URISyntaxException,CkanException {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL propertyFileUrl = classLoader.getResource(CONFIG_FILE);

        Properties properties = new Properties();
        InputStream input = null;

        try {
            input = propertyFileUrl.openStream();

            // load a properties file
            properties.load(input);
     CkanClient cc = new CkanClient(properties.getProperty("data.portal.url"));
            List<CkanDataset> filteredDatasets = cc.searchDatasets(properties.getProperty("data.portal.dataset"), 1, 0).getResults();
            CkanDataset ckanDataset=new CkanDataset();
            for (CkanDataset d : filteredDatasets) {
	           System.out.println("DATASET Name: " + d.getName());
	             ckanDataset =d;
	        }
            List<CkanResource> crsc=ckanDataset.getResources();
	        CkanResource ckResource = null;
	        for (CkanResource r : crsc) {
	         if(r.getUrl().contains(properties.getProperty("data.portal.dataset.file.path"))) {
	        		 ckResource=r;
	        	 }
	        }
	        
	       URL url = new URL(ckResource.getUrl());
	       System.out.println("URL: " + url);
	        
	     String tDir = System.getProperty("java.io.tmpdir"); 
	       String path = tDir + "baffle-current" + ".csv"; 
	       importFile = new File(path); 
	       importFile.deleteOnExit(); 
	       FileUtils.copyURLToFile(url, importFile);
	      // importFile = new File(classLoader.getResource(properties.getProperty("csv.file.path")).getFile());
	       } catch (IOException e) {
            LOGGER.error("Failed to load properties", e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                   LOGGER.error("Failed to close properties file", e);
                }
            }
        }
	  }
    
    

	
}
