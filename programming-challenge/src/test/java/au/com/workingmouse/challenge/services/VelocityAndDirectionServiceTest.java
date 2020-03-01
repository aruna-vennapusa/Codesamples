package au.com.workingmouse.challenge.services;

import au.com.workingmouse.challenge.models.VelocityAndDirectionData;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;



import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VelocityAndDirectionServiceTest {
    private static final String[] VALID_LINE = new String[]{"2019-02-15 10:40:00","94780","420","720","15.36746","208.9371","-13.4357","-7.070466","223.5234","13.6896","-6.570364","5.081869","-39.14706","100","15.29624","16.54015","0.9127843"};

    private static final String[] VALID_LINE2 = new String[] {"2019-02-15 10:40:00","47390","210","360","7.68373","104.46855","-6.71785","-3.535233","111.7617","6.8448","-3.285182","2.5409345","-19.57353","50","7.64812","8.270075","0.45639215"};

    private static final String[] VALID_LINE3 = new String[]{"2019-02-15 10:40:00","47390","210","360","7.68373","104.46855","-6.71785","-3.535233","111.7617","6.8448","-3.285182","2.5409345","-19.57353","50","7.64812","8.270075","0.45639215"};

    private static final Double[] EXPECTED_AVERAGES = {
            94780.0 * 2 / 3,
            420.0 * 2 / 3,
            720.0 * 2 / 3,
            15.36746 * 2 / 3,
            208.9371 * 2 / 3,
            -13.4357 * 2 / 3,
            -7.070466 * 2 / 3,
            223.5234 * 2 / 3,
            13.6896 * 2 / 3,
            -6.570364 * 2 / 3,
            5.081869 * 2 / 3,
            -39.14706 * 2 / 3,
            100.0 * 2 / 3,
            15.29624 * 2 / 3,
            16.54015 * 2 / 3,
            0.9127843 * 2 / 3
    };

    private static final String[] INVALID_LINE = new String[]{"12/01/1995","94780","dog","720","15.36746","208.9371","-13.4357","-7.070466","223.5234","13.6896","-6.570364","5.081869","-39.14706","100","15.29624","16.54015","0.9127843"};

    private static final String[] INVALID_LINE_TWO = new String[]{"2019-02-15 10:40:00","dog","720","15.36746","208.9371","-13.4357","-7.070466","223.5234","13.6896","-6.570364","5.081869","-39.14706","100","15.29624","16.54015","0.9127843"};
    private VelocityAndDirectionData velocityAndDirectionData;
    public static final String VALID_TIMESTAMP_STRING1 = "2018-08-25 10:40";

	public static final String VALID_TIMESTAMP_STRING2 = "2019-02-15 10:40:00";

	public static final String INVALID_TIMESTAMP_STRING = "12/01/1995";

    @BeforeEach
    void setUp() {
        velocityAndDirectionData = new VelocityAndDirectionData(
                Timestamp.valueOf("2019-02-15 10:40:00"),
                94780,
                420,
                720,
                15.36746,
                208.9371,
                -13.4357,
                -7.070466,
                223.5234,
                13.6896,
                -6.570364,
                5.081869,
                -39.14706,
                100.0,
                15.29624,
                16.54015,
                0.9127843
        );
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void parseValidLineTest() throws Exception {
        VelocityAndDirectionData actualVelocityAndDirectionData = VelocityAndDirectionService.parseLine(VALID_LINE);
        assertEquals(velocityAndDirectionData, actualVelocityAndDirectionData);
    }

    @Test
    void parseInvalidLineTestTimeStamp() {
       Executable parseLineTest = () -> VelocityAndDirectionService.parseLine(INVALID_LINE);
        assertThrows(IllegalArgumentException.class, parseLineTest, "Invalid Timestamp");
    }



    @Test
    void parseMultipleLinesTest() throws Exception {
       var lines = VelocityAndDirectionService.parseLines(Arrays.asList(
                VALID_LINE,
                VALID_LINE
        ));

       assertEquals(1, lines.size());
    }

    @Test
    void summariseTest() throws Exception {
      
    	VelocityAndDirectionData actualVelocityAndDirectionDataOne = VelocityAndDirectionService.parseLine(VALID_LINE);
    	VelocityAndDirectionData actualVelocityAndDirectionDataTwo = VelocityAndDirectionService.parseLine(VALID_LINE2);
    	VelocityAndDirectionData actualVelocityAndDirectionDataThree = VelocityAndDirectionService.parseLine(VALID_LINE3);
    	List<VelocityAndDirectionData> dataToSummarise = new ArrayList<>();
    	dataToSummarise.add(actualVelocityAndDirectionDataOne);
    	dataToSummarise.add(actualVelocityAndDirectionDataTwo);
    	dataToSummarise.add(actualVelocityAndDirectionDataThree);
    	JSONObject testResult=VelocityAndDirectionService.calculateAverage(dataToSummarise);
    	assertEquals(63186.666666666664, testResult.get("RecordAvg"));
    }
    
    @Test
    void validStringToTimestampTest() throws Exception {
    	Timestamp TIMESTAMP_ONE=VelocityAndDirectionService.stringToTimestamp(VALID_TIMESTAMP_STRING1);
    	Timestamp TIMESTAMP_TWO=VelocityAndDirectionService.stringToTimestamp(VALID_TIMESTAMP_STRING2);
    	assertEquals("2018-08-25 10:40:00.0", TIMESTAMP_ONE.toString());
    	assertEquals("2019-02-15 10:40:00.0", TIMESTAMP_TWO.toString());
    	
    
    }
    @Test
    void inValidStringToTimestampTest() throws Exception {
    	Executable inValidStringToTimestampTest = () -> VelocityAndDirectionService.stringToTimestamp(INVALID_TIMESTAMP_STRING);
    	assertThrows(IllegalArgumentException.class, inValidStringToTimestampTest, "invalid format of timestamp sring");
    }
    
}