package matt.project.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class OpenWeatherTest_Unit {

    private static OpenWeatherService openWeather;

    @BeforeClass
    public static void setup()
    {
        openWeather = new OpenWeatherServiceImpl();
    }

    @Test(expected = IllegalArgumentException.class)
    public void refusesNegativeZipCode() throws IllegalArgumentException
    {
        openWeather.getWeather("-97209");
    }

    @Test(expected = IllegalArgumentException.class)
    public void refusesTooFewDigits() throws IllegalArgumentException
    {
        openWeather.getWeather("972");
    }

    @Test(expected = IllegalArgumentException.class)
    public void refusesTooManyDigits() throws IllegalArgumentException
    {
        openWeather.getWeather("972103009");
    }

    @Test(expected = IllegalArgumentException.class)
    public void refusesNonDigits() throws IllegalArgumentException
    {
        openWeather.getWeather("972g8");
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
        URL src = getClass().getResource("/sampleResponse_openWeather.json");
        OpenWeatherData weatherData = mapper.readValue(src, OpenWeatherData.class);

        assertThat(weatherData, equalTo(expectedWeatherData));
    }
}
