package matt.project.weather.weather;

import matt.project.weather.weather.impl.OpenWeatherService;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.mock;

public class WeatherTest_Unit {

    private static final String VALID_TEST_ZIP_CODE = "97210";
    private static final WeatherService WEATHER_SERVICE = new OpenWeatherService(mock(RestTemplate.class));

    @Test(expected = IllegalArgumentException.class)
    public void refusesNegativeZipCode() throws IllegalArgumentException
    {
        WEATHER_SERVICE.retrieveWeather("-97209");
    }

    @Test(expected = IllegalArgumentException.class)
    public void refusesTooFewDigits() throws IllegalArgumentException
    {
        WEATHER_SERVICE.retrieveWeather("972");
    }

    @Test(expected = IllegalArgumentException.class)
    public void refusesTooManyDigits() throws IllegalArgumentException
    {
        WEATHER_SERVICE.retrieveWeather("972103009");
    }

    @Test(expected = IllegalArgumentException.class)
    public void refusesNonDigits() throws IllegalArgumentException
    {
        WEATHER_SERVICE.retrieveWeather("972g8");
    }

    @SuppressWarnings("JUnitTestMethodWithNoAssertions") // no throw passes test
    @Test
    public void acceptsValidArgument()
    {
        WEATHER_SERVICE.retrieveWeather(VALID_TEST_ZIP_CODE);
    }
}
