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
        // TODO Improve test incrementally by replacing ".*" elements in pattern
        Pattern weatherDescriptionPattern = Pattern.compile(
            String.format("At the location %s, the temperature is %f, the timezone is %s, and the elevation is .*\\.",
                          TestConfig.CITY_NAME,
                          TestConfig.TEMP,
                          TestConfig.TIMEZONE_NAME));

        assertThat(weatherDescriptionPattern.matcher(outContent.toString()).find(), is(true));
    }

    @TestConfiguration
    private static class TestConfig {

        static final String TIMEZONE_NAME = "Mountain Daylight Time";
        static final String CITY_NAME = "Boulder";
        static final double TEMP = 75.2;
        private static final Double LATITUDE = 1.0;
        private static final Double LONGITUDE = 1.0;

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
            weatherDataMain.put("temp", TEMP);
            openWeatherData.setMain(weatherDataMain);
            Map<String, Double> weatherDataCoordinates = new HashMap<>(2);
            weatherDataCoordinates.put("lat", LATITUDE);
            weatherDataCoordinates.put("lon", LONGITUDE);
            openWeatherData.setCoordinates(weatherDataCoordinates);
            when(mockRestTemplate.getForObject(any(), any(), any(), any())).thenReturn(openWeatherData);
            return new OpenWeatherServiceImpl(mockRestTemplate);
        }

        @Bean
        @Primary
        static GoogleTimeZoneService googleTimeZoneService()
        {
            RestTemplate mockRestTemplate = mock(RestTemplate.class);
            GoogleTimeZoneData timeZoneData = new GoogleTimeZoneData();
            timeZoneData.setTimeZoneName(TIMEZONE_NAME);
            when(mockRestTemplate.getForObject(any(), any(), any(), any(), any(), any())).thenReturn(
                timeZoneData); // TODO This is absurd. Use Map Impl instead for easier testing
            return new GoogleTimeZoneServiceImpl(mockRestTemplate);
        }
    }
}
