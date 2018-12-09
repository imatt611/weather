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
public class GoogleElevationTest_Integration {

    @Autowired private GoogleElevationService googleElevationService;

    @Test
    public void retrievesElevationDataFromGoogle()
    {
        GoogleElevationData elevationData = googleElevationService.getElevation(45.52, -122.67);

        // TODO Actually log in test output
        System.out.println(elevationData);

        // Assertions are for required details only. See resources/googleElevation_apiReference.json for [current] sample
        assertThat(elevationData.getElevation(), notNullValue(Double.class));
        // TODO? Assert that this is actually an external call
    }
}
