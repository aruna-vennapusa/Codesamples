package au.com.workingmouse.challenge;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import au.com.workingmouse.challenge.config.Configuration;
import au.com.workingmouse.challenge.models.VelocityAndDirectionData;
import au.com.workingmouse.challenge.services.FileService;
import au.com.workingmouse.challenge.services.VelocityAndDirectionService;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class MainTest extends Main {
	public static final String TEST_FILE_NAME_ONE = "testFile.csv";
	private static final String VALID_LINE = "2019-02-15 10:40:00,94780,420,720,15.36746,208.9371,-13.4357,-7.070466,"
	        + "223.5234,13.6896,-6.570364,5.081869,-39.14706,100,15.29624,16.54015,0.9127843";

	    private static final String VALID_LINE2 = "2019-02-15 10:40:00,47390,210,360,7.68373,104.46855,-6.71785,-3.535233,"
	        + "111.7617,6.8448,-3.285182,2.5409345,-19.57353,50,7.64812,8.270075,0.45639215";
	@BeforeEach
	void setUp() throws IOException {
		writeToFile(TEST_FILE_NAME_ONE, Arrays.asList(VALID_LINE,VALID_LINE2));
	}

	@AfterEach
	void tearDown() {
		File outputFile = new File(OUTPUT_FILENAME);
		if (outputFile.exists()) {
			outputFile.delete();
		}
	}

	@Test
	void main() {
	}

	@Test
	void writeFileSingleLine() throws IOException {
		final String basicText = "Yolo";
		Main.writeFile(basicText);

		String actualOutput = readFile();
		assertEquals(basicText, actualOutput);
	}

	@Test
	void writeFileMultilineLine() throws IOException {
		final String basicText = "Yolo\n"
				+ "But on two lines";
		Main.writeFile(basicText);

		String actualOutput = readFile();
		assertEquals(basicText, actualOutput);
	}

	@Test
	void loadAndParseFileTest() throws Exception, ParseException {
		File file = new File(TEST_FILE_NAME_ONE);
		List<String[]> lines = FileService.readLines(file);
		List<VelocityAndDirectionData> parsedLines = VelocityAndDirectionService.parseLines(lines);
		assertEquals(47390, parsedLines.get(0).getRecord());
	}

	private String readFile() throws IOException {
		return new Scanner(new File(OUTPUT_FILENAME)).useDelimiter("\\A").next();
	}
	
	private void writeToFile(String fileName, List<String> contents) throws IOException {
		Path file = Paths.get(fileName);
		
	Files.write(file, contents, Charset.forName("UTF-8"));
	}
}