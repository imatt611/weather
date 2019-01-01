package matt.project.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplateHandler;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static matt.project.weather.OpenWeatherData.KEY_COORD_LAT;
import static matt.project.weather.OpenWeatherData.KEY_COORD_LON;
import static matt.project.weather.OpenWeatherData.KEY_MAIN_TEMP;
import static matt.project.weather.OpenWeatherService.GET_WEATHER_QUERY_TEMPLATE;
import static matt.project.weather.OpenWeatherService.WEATHER_ROOT_URI;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;

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
        // given
        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(WEATHER_ROOT_URI).build();
        UriTemplateHandler uriTemplateHandler = restTemplate.getUriTemplateHandler();
        Function<String, URI> endpointExpander = endpoint ->
            uriTemplateHandler.expand(endpoint, VALID_TEST_ZIP_CODE, "");

        OpenWeatherService localTestOpenWeatherService = new OpenWeatherServiceImpl(
            (endpointTemplate, zipCode, apiKey) -> {
                // expect
                // TODO Is this any more meaningful than testing mocks of assumed implementation? This assumes an identical UriTemplateHandler
                assertThat(endpointExpander.apply(endpointTemplate),
                           equalTo(endpointExpander.apply(WEATHER_ROOT_URI + GET_WEATHER_QUERY_TEMPLATE)));

                // TODO Would this be any more meaningful? Probably not with constants, as it's then just testing that the same constant is referenced
                assertThat(endpointTemplate, equalTo(WEATHER_ROOT_URI + GET_WEATHER_QUERY_TEMPLATE));

                // TODO Is this what it takes? Is it worth it?
                // Example: "https://api.openweathermap.org/data/2.5/weather?zip={zipCode}&appid={apiKey}"
                assertThat(endpointTemplate, startsWith(WEATHER_ROOT_URI));
                assertThat(endpointTemplate, containsString("zip="));
                assertThat(endpointTemplate, containsString("appid="));
                return new OpenWeatherData();
            });

        // when
        localTestOpenWeatherService.retrieveWeather(VALID_TEST_ZIP_CODE);
    }

}
