package matt.project.weather.weather;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherTest_Integration {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired private WeatherService weatherService;

    @Test
    public void retrievesWeatherData()
    {
        WeatherData weatherData = weatherService.retrieveWeather("97211");

        log.info("Retrieved Weather Data:\n\n{}\n", weatherData);

        // Assertions for required details only
        assertThat(weatherData.getName(), notNullValue(String.class));
        assertThat(weatherData.getTemperature(), notNullValue(Double.class));
        assertThat(weatherData.getLatitude(), notNullValue(Double.class));
        assertThat(weatherData.getLongitude(), notNullValue(Double.class));
    }
}
