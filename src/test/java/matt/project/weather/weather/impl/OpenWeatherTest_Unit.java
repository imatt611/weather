package matt.project.weather.weather.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import matt.project.weather.weather.WeatherData;
import matt.project.weather.weather.WeatherService;
import org.junit.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static matt.project.weather.weather.impl.OpenWeatherService.GET_WEATHER_ENDPOINT_TEMPLATE;
import static matt.project.weather.weather.impl.OpenWeatherService.ROOT_URI;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestToUriTemplate;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class OpenWeatherTest_Unit {

    private static final String TEST_RESPONSE_OPEN_WEATHER_JSON = "/testResponse_openWeather.json";
    private static final String VALID_TEST_ZIP_CODE = "97209";

    // TODO Parameterized test
    @Test
    public void evaluatesEquality_whenTrue()
    {
        //given
        WeatherData weatherData1 = new OpenWeatherData();
        weatherData1.setTemperature(5.0);
        weatherData1.setLatitude(6.0);
        weatherData1.setLongitude(7.0);
        weatherData1.setName("City Name");

        WeatherData weatherData2 = new OpenWeatherData();
        weatherData2.setTemperature(5.0);
        weatherData2.setLatitude(6.0);
        weatherData2.setLongitude(7.0);
        weatherData2.setName("City Name");

        //expect
        assertThat(weatherData1, equalTo(weatherData2));
    }

    @Test
    public void evaluatesEquality_whenFalse_forTemperature()
    {
        //given
        WeatherData weatherData1 = new OpenWeatherData();
        weatherData1.setTemperature(-5.0);
        weatherData1.setLatitude(6.0);
        weatherData1.setLongitude(7.0);
        weatherData1.setName("City Name");

        WeatherData weatherData2 = new OpenWeatherData();
        weatherData2.setTemperature(5.0);
        weatherData2.setLatitude(6.0);
        weatherData2.setLongitude(7.0);
        weatherData2.setName("City Name");

        //expect
        assertThat(weatherData1, not(equalTo(weatherData2)));
    }

    @Test
    public void evaluatesEquality_whenFalse_forLatitude()
    {
        //given
        WeatherData weatherData1 = new OpenWeatherData();
        weatherData1.setTemperature(5.0);
        weatherData1.setLatitude(6.0);
        weatherData1.setLongitude(7.0);
        weatherData1.setName("City Name");

        WeatherData weatherData2 = new OpenWeatherData();
        weatherData2.setTemperature(5.0);
        weatherData2.setLatitude(16.0);
        weatherData2.setLongitude(7.0);
        weatherData2.setName("City Name");

        //expect
        assertThat(weatherData1, not(equalTo(weatherData2)));
    }

    @Test
    public void evaluatesEquality_whenFalse_forLongitude()
    {
        //given
        WeatherData weatherData1 = new OpenWeatherData();
        weatherData1.setTemperature(5.0);
        weatherData1.setLatitude(6.0);
        weatherData1.setLongitude(7.0);
        weatherData1.setName("City Name");

        WeatherData weatherData2 = new OpenWeatherData();
        weatherData2.setTemperature(5.0);
        weatherData2.setLatitude(6.0);
        weatherData2.setLongitude(7.0000001);
        weatherData2.setName("City Name");

        //expect
        assertThat(weatherData1, not(equalTo(weatherData2)));
    }

    @Test
    public void evaluatesEquality_whenFalse_forName()
    {
        //given
        WeatherData weatherData1 = new OpenWeatherData();
        weatherData1.setTemperature(5.0);
        weatherData1.setLatitude(6.0);
        weatherData1.setLongitude(7.0);
        weatherData1.setName("City Name");

        WeatherData weatherData2 = new OpenWeatherData();
        weatherData2.setTemperature(5.0);
        weatherData2.setLatitude(6.0);
        weatherData2.setLongitude(7.0);
        weatherData2.setName("Not the Same City Name");

        //expect
        assertThat(weatherData1, not(equalTo(weatherData2)));
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
    public void usesKnownOpenWeatherApiContractAndReturnsWeatherData() throws Exception
    {
        //         given
        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(ROOT_URI).build();
        WeatherService localTestWeatherService = new OpenWeatherService(restTemplate);

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
        assertThat(mockData, instanceOf(WeatherData.class));
    }

    //TODO Parametrize
    @Test
    public void calculatesFahrenheit()
    {
        WeatherService weatherService = new OpenWeatherService();

        WeatherData weatherData1 = new OpenWeatherData();
        weatherData1.setTemperature(362.5);

        assertThat(weatherService.getTemperatureInFahrenheit(weatherData1), equalTo(192.83));

        WeatherData weatherData2 = new OpenWeatherData();
        weatherData2.setTemperature(220.48374);

        assertThat(weatherService.getTemperatureInFahrenheit(weatherData2), equalTo(-62.80));
    }
}
