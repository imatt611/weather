package matt.project.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.net.URL;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class GoogleElevationTest_Unit {

    @Test
    public void unpacksResults() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        URL src = getClass().getResource("/sampleResponse_googleElevation.json");

        GoogleElevationData elevationData = mapper.readValue(src, GoogleElevationData.class);

        assertThat(elevationData.getElevation(), equalTo(1608.637939453125));
    }

}
