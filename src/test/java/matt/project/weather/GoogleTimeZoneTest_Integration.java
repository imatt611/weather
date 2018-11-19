package matt.project.weather;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoogleTimeZoneTest_Integration {

    @Autowired private GoogleTimeZoneService googleTimeZoneService;

    @Test
    public void retrievesWeatherDataFromOpenWeather()
    {
        GoogleTimeZoneData timeZoneData = googleTimeZoneService.getTimeZone("97210");
    }
}
