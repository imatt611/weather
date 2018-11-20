package matt.project.weather;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoogleTimeZoneTest_Integration {

    @Autowired private GoogleTimeZoneService googleTimeZoneService;

    @Test
    public void retrievesTimeZoneDataFromGoogle()
    {
        GoogleTimeZoneData timeZoneData = googleTimeZoneService.getTimeZone(-122.67, 45.52);

        System.out.println(timeZoneData);

        // Assertions are for required details only. See resources/googleTimeZone_apiReference.json for [current] sample
        assertThat(timeZoneData.getTimeZoneName(), notNullValue(String.class));
        // TODO? Assert that this is actually an external call
    }
}
