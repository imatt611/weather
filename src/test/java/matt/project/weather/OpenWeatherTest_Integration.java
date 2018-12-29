package matt.project.weather;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired private OpenWeatherService openWeatherService;

    @Test
    public void retrievesWeatherDataFromOpenWeather()
    {
        OpenWeatherData openWeatherData = openWeatherService.getWeather("97211");

        log.info("Retrieved Weather Data:\n\n{}\n", openWeatherData);

        // Assertions are for required details only. See resources/testResponse_openWeather.json for sample
        assertThat(openWeatherData.getName(), notNullValue(String.class));
        assertThat(openWeatherData.getCoordinates(), hasEntry(is("lon"), any(Double.class)));
        assertThat(openWeatherData.getCoordinates(), hasEntry(is("lat"), any(Double.class)));
        assertThat(openWeatherData.getMain(), hasEntry(is("temp"), any(Double.class)));
    }
}
