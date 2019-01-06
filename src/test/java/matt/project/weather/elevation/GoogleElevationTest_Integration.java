package matt.project.weather.elevation;

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
public class GoogleElevationTest_Integration {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired private GoogleElevationServiceImpl elevationService;

    @Test
    public void retrievesElevationDataFromGoogle()
    {
        ElevationData elevationData = elevationService.retrieveElevation(45.52, -122.67);

        log.info("Retrieved Elevation Data:\n\n{}\n", elevationData);

        // Assertions are for required details only. See resources/testResponse_googleElevation.json for sample
        assertThat(elevationData.getElevation(), notNullValue(Double.class));
    }
}
