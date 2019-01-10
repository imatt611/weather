package matt.project.weather;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@SuppressWarnings("resource")
@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherAppTest_Spring {

    public static final String TEST_CITY_NAME = "Boulder";
    public static final double TEST_TEMPERATURE_KELVIN = 297.15;
    private static final double TEST_TEMPERATURE_FAHRENHEIT = 75.2;
    public static final String TEST_TIMEZONE_NAME = "Mountain Daylight Time";
    public static final Double TEST_ELEVATION = 1500.3784;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams()
    {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams()
    {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void describesRetrievedWeather_TimeZone_Elevation()
    {
        // Must trigger #main from test to ensure it feeds System#out into the stream under test
        WeatherApp.main(new String[]{"80301"});
        Pattern weatherDescriptionPattern = Pattern.compile(
            String.format(
                "At the location %s, the temperature is %f Fahrenheit, the timezone is %s, and the elevation is %f feet\\.",
                TEST_CITY_NAME,
                TEST_TEMPERATURE_FAHRENHEIT,
                TEST_TIMEZONE_NAME,
                TEST_ELEVATION));

        assertThat(weatherDescriptionPattern.matcher(outContent.toString()).find(), is(true));
    }

    @Test
    public void outputsMessageForMissingArgument()
    {
        // Must trigger #main from test to ensure it feeds System#out into the stream under test
        WeatherApp.main(new String[]{});
        Pattern missingArgumentMessagePattern = Pattern.compile(
            "Provide a 5-digit ZIP Code as an argument to receive information about the area\\.");

        assertThat(missingArgumentMessagePattern.matcher(outContent.toString()).find(), is(true));
    }

    @Test
    public void outputsMessageForExtraArguments()
    {
        String firstArg = "12345";
        // Must trigger #main from test to ensure it feeds System#out into the stream under test
        WeatherApp.main(new String[]{firstArg, "54321"});
        Pattern missingArgumentMessagePattern = Pattern.compile(
            String.format("Extra arguments provided\\. The first, %s, will be used as the ZIP Code argument\\.",
                          firstArg));

        assertThat(missingArgumentMessagePattern.matcher(outContent.toString()).find(), is(true));
    }
}
