package matt.project.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.net.URL;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class GoogleTimeZoneTest_Unit {

    @Test
    public void deserializesResults() throws Exception
    {
        // expect
        GoogleTimeZoneData expectedTimeZoneData = new GoogleTimeZoneData();
        expectedTimeZoneData.setTimeZoneName("Eastern Daylight Time");

        // when
        ObjectMapper mapper = new ObjectMapper();
        URL src = getClass().getResource("/testResponse_googleTimeZone.json");
        GoogleTimeZoneData timeZoneData = mapper.readValue(src, GoogleTimeZoneData.class);

        assertThat(timeZoneData, equalTo(expectedTimeZoneData));
    }

}
