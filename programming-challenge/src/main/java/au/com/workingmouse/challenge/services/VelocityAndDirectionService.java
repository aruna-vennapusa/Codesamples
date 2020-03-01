package au.com.workingmouse.challenge.services;

import au.com.workingmouse.challenge.models.VelocityAndDirectionData;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.OptionalDouble;

import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONObject;

public class VelocityAndDirectionService {

	
  public static VelocityAndDirectionData parseLine(String[] line) throws Exception {
	  VelocityAndDirectionData velocityAndDirectionData = new VelocityAndDirectionData();
       velocityAndDirectionData.setTimestamp(stringToTimestamp(line[0]));
        velocityAndDirectionData.setRecord(NumberUtils.toInt(line[1]));
        velocityAndDirectionData.setDcsModel(NumberUtils.toInt(line[2]));
        velocityAndDirectionData.setDcsSerial(NumberUtils.toInt(line[3]));
        velocityAndDirectionData.setDcsAbsspdAvg(NumberUtils.toDouble(line[4]));
        velocityAndDirectionData.setDcsDirectionAvg(NumberUtils.toDouble(line[5]));
        velocityAndDirectionData.setDcsNorthCurAvg(NumberUtils.toDouble(line[6]));
        velocityAndDirectionData.setDcsEastCurAvg(NumberUtils.toDouble(line[7]));
        velocityAndDirectionData.setDcsHeadingAvg(NumberUtils.toDouble(line[8]));
        velocityAndDirectionData.setDcsTiltXAvg(NumberUtils.toDouble(line[9]));
        velocityAndDirectionData.setDcsTiltYAvg(NumberUtils.toDouble(line[10]));
        velocityAndDirectionData.setDcsSpStdAvg(NumberUtils.toDouble(line[11]));
        velocityAndDirectionData.setDcsSigStrengthAvg(NumberUtils.toDouble(line[12]));
        velocityAndDirectionData.setDcsPingCntAvg(NumberUtils.toDouble(line[13]));
        velocityAndDirectionData.setDcsAbsTiltAvg(NumberUtils.toDouble(line[14]));
        velocityAndDirectionData.setDscMaxTiltAvg(NumberUtils.toDouble(line[15]));
        velocityAndDirectionData.setDcsStdTiltAvg(NumberUtils.toDouble(line[16]));

        return velocityAndDirectionData;
    }

    /**
     * Takes the given ordered list of VelocityAndDirectionData properties and initialises a new
     * VelocityAndDirectionData object.
     * @param line - row to parse
     * @return VelocityAndDirectionData
     */
    public static VelocityAndDirectionData parseLine(List<String> line) {
    	  if (line.size() != 17) {
            throw new IllegalArgumentException("VelocityAndDirectionData Objects require 17 input arguments.");
        }

        return new VelocityAndDirectionData(
                Timestamp.valueOf(line.get(0)),
                Integer.parseInt(line.get(1)),
                Integer.parseInt(line.get(2)),
                Integer.parseInt(line.get(3)),
                Double.parseDouble(line.get(4)),
                Double.parseDouble(line.get(5)),
                Double.parseDouble(line.get(6)),
                Double.parseDouble(line.get(7)),
                Double.parseDouble(line.get(8)),
                Double.parseDouble(line.get(9)),
                Double.parseDouble(line.get(10)),
                Double.parseDouble(line.get(11)),
                Double.parseDouble(line.get(12)),
                Double.parseDouble(line.get(13)),
                Double.parseDouble(line.get(14)),
                Double.parseDouble(line.get(15)),
                Double.parseDouble(line.get(16))
        );
    }

    public static List<VelocityAndDirectionData> parseLines(List<String[]> lines) throws Exception {
        var parsedLines = new ArrayList<VelocityAndDirectionData>();

        int count = 0;
        for (String[] line : lines) {
            if (count++ == 0) {
            	 // Skip header
            	 continue;
            }
           parsedLines.add(VelocityAndDirectionService.parseLine(line));
       }

        return parsedLines;
    }


    public static String summarise(List<VelocityAndDirectionData> velocityAndDirectionDataset) {
        JSONObject result=calculateAverage(velocityAndDirectionDataset);
       
        var summaryBuilder = new StringBuilder() ;

        // Transform dataset to be listed in columns rather than rows

        summaryBuilder.append("<head></head>")
                .append("<body>")
                .append("<h2>Summary</h2>")
                .append("<br />")
                .append("<strong>Total Lines:</strong>" + velocityAndDirectionDataset.size())
                .append("<p>") ;// Add p element to HTML for displaying averages
        
        // TODO: You will also have to do some work here to ensure the details are complete.
        try {
        result.keys().forEachRemaining(key -> {
        	summaryBuilder.append("<strong>"+key+": </strong>")
        	               .append(( result.get(key)))
        				   .append("<p>");
        	              
        });
        }catch (Exception e) {
        	System.out.println("Failed to calculate average of values: "+e);
        }
        
        summaryBuilder.append("</p>")
                .append("</body>");

        return summaryBuilder.toString();
    }
    
      
    public static JSONObject calculateAverage(List<VelocityAndDirectionData> velocityAndDirectionDataset) {
    	
    	JSONObject result = new JSONObject();
    	
    	Double recordAvg = velocityAndDirectionDataset.stream().map(c->(Integer)c.getRecord()).filter(Objects::nonNull).mapToInt (Integer::intValue).average().getAsDouble();
    	result.put("RecordAvg", recordAvg);
    	Double dcsModelAvg = velocityAndDirectionDataset.stream().filter(c-> (Integer)c.getDcsModel()!=0).map(c->(Integer)c.getDcsModel()).filter(Objects::nonNull).mapToInt (Integer::intValue).average().getAsDouble();
    	result.put("dcsModelAvg", dcsModelAvg);
    	Double dcsSerialAvg = velocityAndDirectionDataset.stream().filter(c-> (Integer)c.getDcsSerial()!=0).map(c->(Integer)c.getDcsSerial()).filter(Objects::nonNull).mapToInt (Integer::intValue).average().getAsDouble();
    	result.put("dcsSerialAvg", dcsSerialAvg);
    	Double dcsAbsspdAvg = velocityAndDirectionDataset.stream().filter(c-> (Double)c.getDcsAbsspdAvg()!=0).map(c->(Double)c.getDcsAbsspdAvg()).filter(Objects::nonNull).mapToDouble (Double::doubleValue).average().getAsDouble();
    	result.put("dcsAbsspdAvg", dcsAbsspdAvg);
    	Double dcsDirectionAvg = velocityAndDirectionDataset.stream().filter(c-> (Double)c.getDcsDirectionAvg()!=0).map(c->(Double)c.getDcsDirectionAvg()).filter(Objects::nonNull).mapToDouble (Double::doubleValue).average().getAsDouble();
    	result.put("dcsDirectionAvg", dcsDirectionAvg);
    	Double dcsNorthCurAvg = velocityAndDirectionDataset.stream().filter(c-> (Double)c.getDcsNorthCurAvg()!=0).map(c->(Double)c.getDcsNorthCurAvg()).filter(Objects::nonNull).mapToDouble (Double::doubleValue).average().getAsDouble();
    	result.put("dcsNorthCurAvg", dcsNorthCurAvg);
    	Double dcsEastCurAvg = velocityAndDirectionDataset.stream().filter(c-> (Double)c.getDcsEastCurAvg()!=0).map(c->(Double)c.getDcsEastCurAvg()).filter(Objects::nonNull).mapToDouble (Double::doubleValue).average().getAsDouble();
    	result.put("dcsEastCurAvg", dcsEastCurAvg);
    	Double dcsHeadingAvg = velocityAndDirectionDataset.stream().filter(c-> (Double)c.getDcsHeadingAvg()!=0).map(c->(Double)c.getDcsHeadingAvg()).filter(Objects::nonNull).mapToDouble (Double::doubleValue).average().getAsDouble();
    	result.put("dcsHeadingAvg", dcsHeadingAvg);
    	Double dcsTiltXAvg = velocityAndDirectionDataset.stream().filter(c-> (Double)c.getDcsTiltXAvg()!=0).map(c->(Double)c.getDcsTiltXAvg()).filter(Objects::nonNull).mapToDouble (Double::doubleValue).average().getAsDouble();
    	result.put("dcsTiltXAvg", dcsTiltXAvg);
    	Double dcsTiltYAvg = velocityAndDirectionDataset.stream().filter(c-> (Double)c.getDcsTiltYAvg()!=0).map(c->(Double)c.getDcsTiltYAvg()).filter(Objects::nonNull).mapToDouble (Double::doubleValue).average().getAsDouble();
    	result.put("dcsTiltYAvg", dcsTiltYAvg);
    	Double dcsSpStdAvg = velocityAndDirectionDataset.stream().filter(c-> (Double)c.getDcsSpStdAvg()!=0).map(c->(Double)c.getDcsSpStdAvg()).filter(Objects::nonNull).mapToDouble (Double::doubleValue).average().getAsDouble();
    	result.put("dcsSpStdAvg", dcsSpStdAvg);
    	Double dcsSigStrengthAvg = velocityAndDirectionDataset.stream().filter(c-> (Double)c.getDcsSigStrengthAvg()!=0).map(c->(Double)c.getDcsSigStrengthAvg()).filter(Objects::nonNull).mapToDouble (Double::doubleValue).average().getAsDouble();
    	result.put("dcsSigStrengthAvg", dcsSigStrengthAvg);
    	Double dcsPingCntAvg = velocityAndDirectionDataset.stream().filter(c-> (Double)c.getDcsPingCntAvg()!=0).map(c->(Double)c.getDcsPingCntAvg()).filter(Objects::nonNull).mapToDouble (Double::doubleValue).average().getAsDouble();
    	result.put("dcsPingCntAvg", dcsPingCntAvg);
    	Double dcsAbsTiltAvg = velocityAndDirectionDataset.stream().filter(c-> (Double)c.getDcsAbsTiltAvg()!=0).map(c->(Double)c.getDcsAbsTiltAvg()).filter(Objects::nonNull).mapToDouble (Double::doubleValue).average().getAsDouble();
    	result.put("dcsAbsTiltAvg", dcsAbsTiltAvg);
    	Double dscMaxTiltAvg = velocityAndDirectionDataset.stream().filter(c-> (Double)c.getDscMaxTiltAvg()!=0).map(c->(Double)c.getDscMaxTiltAvg()).filter(Objects::nonNull).mapToDouble (Double::doubleValue).average().getAsDouble();
    	result.put("dscMaxTiltAvg", dscMaxTiltAvg);
    	Double dcsStdTiltAvg = velocityAndDirectionDataset.stream().filter(c-> (Double)c.getDcsStdTiltAvg()!=0).map(c->(Double)c.getDcsStdTiltAvg()).filter(Objects::nonNull).mapToDouble (Double::doubleValue).average().getAsDouble();
    	result.put("dcsStdTiltAvg", dcsStdTiltAvg);
    	
    	return result;
        
    }
    
    public static Timestamp stringToTimestamp(String timeStampStr) throws ParseException{
    	
      	List<DateTimeFormatter> formatList = new ArrayList<>();
		formatList.add(new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm").toFormatter(Locale.ENGLISH));
		formatList.add(new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm:ss").toFormatter());
			
    	if (timeStampStr == null)
			return null;
		Timestamp date = null;
		for (DateTimeFormatter sdf : formatList) {
			try {
				LocalDateTime localTime = LocalDateTime.parse(timeStampStr, sdf);
				    String dateString = localTime.format(new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm:ss").toFormatter());
				    date=Timestamp.valueOf(dateString);
			} catch (Exception e) {
				date=Timestamp.valueOf(timeStampStr);
			}
			if (date != null)
				break;
		}
		// return date if parsed successfully or else throw exception
		if (date != null)
			return date;
		throw new ParseException("invalid format for String:" + timeStampStr, 0);
    	
    	
    }
}
