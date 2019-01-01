package matt.project.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static matt.project.weather.OpenWeatherData.KEY_COORD_LAT;
import static matt.project.weather.OpenWeatherData.KEY_COORD_LON;
import static matt.project.weather.OpenWeatherData.KEY_MAIN_TEMP;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class OpenWeatherTest_Unit {

    static final String TEST_RESPONSE_OPEN_WEATHER_JSON = "/testResponse_openWeather.json"; // TODO Move
    private static final String VALID_TEST_ZIP_CODE = "97210";
    private static final OpenWeatherService openWeatherService = new OpenWeatherServiceImpl(
        ((endpointTemplate, zipCode, apiKey) -> new OpenWeatherData()));

    @Test(expected = IllegalArgumentException.class)
    public void refusesNegativeZipCode() throws IllegalArgumentException
    {
        openWeatherService.retrieveWeather("-97209");
    }

    @Test(expected = IllegalArgumentException.class)
    public void refusesTooFewDigits() throws IllegalArgumentException
    {
        openWeatherService.retrieveWeather("972");
    }

    @Test(expected = IllegalArgumentException.class)
    public void refusesTooManyDigits() throws IllegalArgumentException
    {
        openWeatherService.retrieveWeather("972103009");
    }

    @Test(expected = IllegalArgumentException.class)
    public void refusesNonDigits() throws IllegalArgumentException
    {
        openWeatherService.retrieveWeather("972g8");
    }

    @SuppressWarnings("JUnitTestMethodWithNoAssertions") // no throw passes test
    @Test
    public void acceptsValidArgument()
    {
        openWeatherService.retrieveWeather(VALID_TEST_ZIP_CODE);
    }

    @Test
    public void deserializesResults() throws Exception
    {
        // expect
        Map<String, Double> expectedCoords = new HashMap<>(2);
        expectedCoords.put(KEY_COORD_LON, -122.09);
        expectedCoords.put(KEY_COORD_LAT, 37.39);
        Map<String, Double> expectedMain = new HashMap<>(5);
        expectedMain.put(KEY_MAIN_TEMP, 285.68);
        expectedMain.put("humidity", 74.0);
        expectedMain.put("pressure", 1016.8);
        expectedMain.put("temp_min", 284.82);
        expectedMain.put("temp_max", 286.48);
        OpenWeatherData expectedWeatherData = new OpenWeatherData();
        expectedWeatherData.setCoordinates(expectedCoords);
        expectedWeatherData.setMain(expectedMain);
        expectedWeatherData.setName("Mountain View");

        // when
        ObjectMapper mapper = new ObjectMapper();
        URL src = getClass().getResource(TEST_RESPONSE_OPEN_WEATHER_JSON);
        OpenWeatherData weatherData = mapper.readValue(src, OpenWeatherData.class);

        assertThat(weatherData, equalTo(expectedWeatherData));
    }

    @Test
    public void usesKnownOpenWeatherApiContract()
    {
        // expect
        Consumer<String> asserter = endpointTemplate -> {
            // Example: "https://api.openweathermap.org/data/2.5/weather?zip={zipCode}&appid={apiKey}"
            // FIXME Nope, fuck it. This can't work because this ISN'T actually the endpointTemplate, it's the query
            //  template! Thus, all we can test here with the provider approach and complete avoidance of mocks is the
            //  query string.
            //assertThat(endpointTemplate, startsWith(WEATHER_ROOT_URI));
            assertThat(endpointTemplate, containsString("zip="));
            assertThat(endpointTemplate, containsString("appid="));
        };

        // given
        OpenWeatherService localTestOpenWeatherService = new OpenWeatherServiceImpl(
            (endpointTemplate, zipCode, apiKey) -> {
                asserter.accept(endpointTemplate);
                return new OpenWeatherData();
            });

        // when
        localTestOpenWeatherService.retrieveWeather(VALID_TEST_ZIP_CODE);
    }

}
