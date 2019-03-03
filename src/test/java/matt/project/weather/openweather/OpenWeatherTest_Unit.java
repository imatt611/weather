package matt.project.weather.openweather;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import matt.project.weather.weather.WeatherData;
import matt.project.weather.weather.WeatherService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static matt.project.weather.openweather.OpenWeatherService.GET_WEATHER_ENDPOINT_TEMPLATE;
import static matt.project.weather.openweather.OpenWeatherService.ROOT_URI;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestToUriTemplate;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(JUnitParamsRunner.class)
public class OpenWeatherTest_Unit {

    private static final String TEST_RESPONSE_OPEN_WEATHER_JSON = "/testResponse_openWeather.json";
    private static final String VALID_TEST_ZIP_CODE = "97209";

    @Test
    @Parameters("5, 6, 7, City Name")
    public void evaluatesEquality_whenTrue(double temp, double lat, double lon, String name)
    {
        //given
        WeatherData weatherData1 = new OpenWeatherData();
        weatherData1.setTemperature(temp);
        weatherData1.setLatitude(lat);
        weatherData1.setLongitude(lon);
        weatherData1.setName(name);

        WeatherData weatherData2 = new OpenWeatherData();
        weatherData2.setTemperature(temp);
        weatherData2.setLatitude(lat);
        weatherData2.setLongitude(lon);
        weatherData2.setName(name);

        //expect
        assertThat(weatherData1, equalTo(weatherData2));
    }

    @Test
    @Parameters({
        "-5 | 6  | 7         | City Name",
        "5  | 16 | 7         | City Name",
        "5  | 6  | 7.0000001 | City Name",
        "5  | 6  | 7         | Not the Same City Name"
    })
    public void evaluatesEquality_whenFalse(double temp, double lat, double lon, String name)
    {
        //given
        WeatherData weatherData1 = new OpenWeatherData();
        weatherData1.setTemperature(5.0);
        weatherData1.setLatitude(6.0);
        weatherData1.setLongitude(7.0);
        weatherData1.setName("City Name");

        WeatherData weatherData2 = new OpenWeatherData();
        weatherData2.setTemperature(temp);
        weatherData2.setLatitude(lat);
        weatherData2.setLongitude(lon);
        weatherData2.setName(name);

        //expect
        assertThat(weatherData1, not(equalTo(weatherData2)));
    }

    @Test
    public void deserializesResults() throws Exception
    {
        // given
        WeatherData expectedWeatherData = new OpenWeatherData();
        expectedWeatherData.setTemperature(285.68);
        expectedWeatherData.setLatitude(37.39);
        expectedWeatherData.setLongitude(-122.09);
        expectedWeatherData.setName("Mountain View");

        // when
        ObjectMapper mapper = new ObjectMapper();
        URL src = getClass().getResource(TEST_RESPONSE_OPEN_WEATHER_JSON);
        WeatherData weatherData = mapper.readValue(src, OpenWeatherData.class);

        // expect
        assertThat(weatherData, equalTo(expectedWeatherData));
    }

    @Test
    public void usesKnownOpenWeatherApiContractAndReturnsWeatherData() throws Exception
    {
        // given
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

    @Test
    @Parameters({
        "362.5, 192.83",
        "220.48374, -62.8",
        "0, -459.67",
        "-1, -461.47",
        "12345678, 22221760.73"
    })
    public void calculatesFahrenheit(double temperatureKelvin, double temperatureFahrenheit)
    {
        WeatherService weatherService = new OpenWeatherService();
        WeatherData weatherData = new OpenWeatherData();
        weatherData.setTemperature(temperatureKelvin);

        assertThat(weatherService.getTemperatureInFahrenheit(weatherData), equalTo(temperatureFahrenheit));
    }
}
