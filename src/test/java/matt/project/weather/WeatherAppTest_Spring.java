package matt.project.weather;

import matt.project.weather.elevation.GoogleElevationConfig;
import matt.project.weather.timezone.GoogleTimeZoneConfig;
import matt.project.weather.weather.OpenWeatherConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@SuppressWarnings("resource")
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = GoogleElevationConfig.class)
public class WeatherAppTest_Spring {
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
            String.format("At the location %s, the temperature is %f, the timezone is %s, and the elevation is %f\\.",
                          OpenWeatherConfig.CITY_NAME,
                          OpenWeatherConfig.TEMP,
                          GoogleTimeZoneConfig.TIMEZONE_NAME,
                          GoogleElevationConfig.ELEVATION));

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
