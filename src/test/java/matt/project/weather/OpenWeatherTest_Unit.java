package matt.project.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class OpenWeatherTest_Unit {

    private static final String TEST_RESPONSE_OPEN_WEATHER_JSON = "/testResponse_openWeather.json";
    private static final String VALID_TEST_ZIP_CODE = "97210";
    private static OpenWeatherService openWeatherService;

    @Value(OpenWeatherService.PROP_REF__API_KEY_OPEN_WEATHER)
    private String apiKey;

    @BeforeClass
    public static void setup()
    {
        openWeatherService = new OpenWeatherServiceImpl();
    }

    @Test(expected = IllegalArgumentException.class)
    public void refusesNegativeZipCode() throws IllegalArgumentException
    {
        openWeatherService.getWeather("-97209");
    }

    @Test(expected = IllegalArgumentException.class)
    public void refusesTooFewDigits() throws IllegalArgumentException
    {
        openWeatherService.getWeather("972");
    }

    @Test(expected = IllegalArgumentException.class)
    public void refusesTooManyDigits() throws IllegalArgumentException
    {
        openWeatherService.getWeather("972103009");
    }

    @Test(expected = IllegalArgumentException.class)
    public void refusesNonDigits() throws IllegalArgumentException
    {
        openWeatherService.getWeather("972g8");
    }

    @SuppressWarnings("JUnitTestMethodWithNoAssertions") // no throw passes test
    @Test
    public void acceptsValidArgument()
    {
        OpenWeatherService localTestOpenWeatherService = new OpenWeatherServiceImpl(mock(RestTemplate.class));

        localTestOpenWeatherService.getWeather(VALID_TEST_ZIP_CODE);
    }

    @Test
    public void deserializesResults() throws Exception
    {
        // expect
        Map<String, Double> expectedCoords = new HashMap<>(2);
        expectedCoords.put("lon", -122.09);
        expectedCoords.put("lat", 37.39);
        Map<String, Double> expectedMain = new HashMap<>(5);
        expectedMain.put("temp", 285.68);
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
    public void usesKnownOpenWeatherApiContractAndReturnsOpenWeatherData() throws Exception
    {
        // given
        String rootUri = OpenWeatherService.ROOT_URI;
        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(rootUri).build();
        OpenWeatherService localTestOpenWeatherService = new OpenWeatherServiceImpl(restTemplate);

        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);

        String testDataJsonString = String.join("", Files.readAllLines(
                Paths.get(getClass().getResource(TEST_RESPONSE_OPEN_WEATHER_JSON).getPath())));

        String targetUri = restTemplate
                .getUriTemplateHandler()
                .expand(OpenWeatherService.GET_WEATHER_ENDPOINT_TEMPLATE, VALID_TEST_ZIP_CODE, apiKey)
                .toString();

        // expect
        mockServer
                .expect(requestTo(targetUri))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(testDataJsonString, MediaType.APPLICATION_JSON));

        // when
        OpenWeatherData mockData = localTestOpenWeatherService.getWeather(VALID_TEST_ZIP_CODE);

        mockServer.verify();
        assertThat(mockData, instanceOf(OpenWeatherData.class));
    }

}
