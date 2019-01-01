package matt.project.weather;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static matt.project.weather.OpenWeatherData.KEY_COORD_LAT;
import static matt.project.weather.OpenWeatherData.KEY_COORD_LON;
import static matt.project.weather.OpenWeatherData.KEY_MAIN_TEMP;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("resource")
@RunWith(SpringRunner.class)
@SpringBootTest
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
                          TestConfig.CITY_NAME,
                          TestConfig.TEMP,
                          TestConfig.TIMEZONE_NAME,
                          TestConfig.ELEVATION));

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

    @TestConfiguration
    private static class TestConfig {

        static final Double ELEVATION = 1500.3784;
        static final String TIMEZONE_NAME = "Mountain Daylight Time";
        static final String CITY_NAME = "Boulder";
        static final double TEMP = 75.2;

        TestConfig()
        {
        }

        @Bean
        @Primary
        static OpenWeatherService openWeatherService()
        {
            RestTemplate mockRestTemplate = mock(RestTemplate.class);
            OpenWeatherData openWeatherData = new OpenWeatherData();
            openWeatherData.setName(CITY_NAME);
            Map<String, Double> weatherDataMain = new HashMap<>(1);
            weatherDataMain.put(KEY_MAIN_TEMP, TEMP);
            openWeatherData.setMain(weatherDataMain);
            Map<String, Double> weatherDataCoordinates = new HashMap<>(2);
            weatherDataCoordinates.put(KEY_COORD_LAT, 1.0);
            weatherDataCoordinates.put(KEY_COORD_LON, 1.0);
            openWeatherData.setCoordinates(weatherDataCoordinates);
            when(mockRestTemplate.getForObject(any(), any(), any(), any())).thenReturn(openWeatherData);
            return new OpenWeatherServiceImpl((endpointTemplate, zipCode, apiKey) -> new OpenWeatherData());
        }

        @Bean
        @Primary
        static GoogleTimeZoneService googleTimeZoneService()
        {
            RestTemplate mockRestTemplate = mock(RestTemplate.class);
            GoogleTimeZoneData timeZoneData = new GoogleTimeZoneData();
            timeZoneData.setTimeZoneName(TIMEZONE_NAME);
            when(mockRestTemplate.getForObject(any(), any(), any(), any(), any(), any())).thenReturn(
                timeZoneData); // TODO This is absurd. Use Map Impl instead for easier testing OR consider injecting providers, not just Impls (see TODO in WeatherApp)
            return new GoogleTimeZoneServiceImpl(mockRestTemplate);
        }

        @Bean
        @Primary
        static GoogleElevationService googleElevationService()
        {
            RestTemplate mockRestTemplate = mock(RestTemplate.class);
            GoogleElevationData elevationData = new GoogleElevationData();
            elevationData.setElevation(ELEVATION);
            when(mockRestTemplate.getForObject(any(), any(), any(), any(), any())).thenReturn(
                elevationData); // TODO This is absurd. Use Map Impl instead for easier testing OR consider injecting providers, not just Impls (see TODO in WeatherApp)
            return new GoogleElevationServiceImpl(mockRestTemplate);
        }
    }
}
