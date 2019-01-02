package matt.project.weather.timezone;

import matt.project.weather.TimeZoneData;
import matt.project.weather.TimeZoneService;
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
public class GoogleTimeZoneTest_Integration {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired private TimeZoneService timeZoneService;

    @Test
    public void retrievesTimeZoneDataFromGoogle()
    {
        TimeZoneData timeZoneData = timeZoneService.retrieveTimeZone(45.52, -122.67);

        log.info("Retrieved Time Zone Data:\n\n{}\n", timeZoneData);

        // Assertions are for required details only. See resources/testResponse_googleTimeZone.json for sample
        assertThat(timeZoneData.getTimeZoneName(), notNullValue(String.class));
    }
}
