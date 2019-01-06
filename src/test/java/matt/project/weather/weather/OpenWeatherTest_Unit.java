package matt.project.weather.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static matt.project.weather.weather.OpenWeatherServiceImpl.GET_WEATHER_ENDPOINT_TEMPLATE;
import static matt.project.weather.weather.OpenWeatherServiceImpl.ROOT_URI;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestToUriTemplate;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class OpenWeatherTest_Unit {

    private static final String TEST_RESPONSE_OPEN_WEATHER_JSON = "/testResponse_openWeather.json";
    private static final String VALID_TEST_ZIP_CODE = "97210";
    private static final WeatherService WEATHER_SERVICE = new OpenWeatherServiceImpl(mock(RestTemplate.class));

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

    @Test
    public void deserializesResults() throws Exception
    {
        // expect
        WeatherData expectedWeatherData = new OpenWeatherData();
        expectedWeatherData.setTemperature(285.68);
        expectedWeatherData.setLatitude(37.39);
        expectedWeatherData.setLongitude(-122.09);
        expectedWeatherData.setName("Mountain View");

        // when
        ObjectMapper mapper = new ObjectMapper();
        URL src = getClass().getResource(TEST_RESPONSE_OPEN_WEATHER_JSON);
        WeatherData weatherData = mapper.readValue(src, OpenWeatherData.class);

        assertThat(weatherData, equalTo(expectedWeatherData));
    }

    @Test
    public void usesKnownOpenWeatherApiContractAndReturnsOpenWeatherData() throws Exception
    {
        // given
        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(ROOT_URI).build();
        WeatherService localTestWeatherService = new OpenWeatherServiceImpl(restTemplate);

        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);

        String testDataJsonString = String.join("", Files.readAllLines(
            Paths.get(getClass().getResource(TEST_RESPONSE_OPEN_WEATHER_JSON).getPath())));

        // expect
        mockServer
            .expect(requestToUriTemplate(ROOT_URI + GET_WEATHER_ENDPOINT_TEMPLATE, VALID_TEST_ZIP_CODE, ""))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(testDataJsonString, MediaType.APPLICATION_JSON));

        // when
        WeatherData mockData = localTestWeatherService.retrieveWeather(VALID_TEST_ZIP_CODE);

        mockServer.verify();
        assertThat(mockData, instanceOf(OpenWeatherData.class));
    }

}
