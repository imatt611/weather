package matt.project.weather;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OpenWeatherTest_Integration {

    @Autowired private OpenWeatherService openWeather;

    @Test
    public void retrievesWeatherDataFromOpenWeather()
    {
        int zipCode = 97210;
        WeatherData weatherData = openWeather.getWeather(zipCode);

        // Assertions are for required details only. See resources/openWeather_apiReference.json for [current] sample
        assertThat(weatherData.getName(), notNullValue(String.class));
        assertThat(weatherData.getCoordinates(), hasEntry(is("lon"), any(Double.class)));
        assertThat(weatherData.getCoordinates(), hasEntry(is("lat"), any(Double.class)));
        assertThat(weatherData.getMain(), hasEntry(is("temp"), any(Double.class)));
    }
}
