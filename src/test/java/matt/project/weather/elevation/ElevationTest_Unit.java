package matt.project.weather.elevation;

import matt.project.weather.google.GoogleElevationService;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.mock;

public class ElevationTest_Unit {

    private static final ElevationService elevationService = new GoogleElevationService(
        mock(RestTemplate.class));

    @Test(expected = IllegalArgumentException.class)
    public void refusesLatitudeOver90() throws IllegalArgumentException
    {
        elevationService.retrieveElevation(90.000000000001, 45.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void refusesLatitudeUnderNegative90() throws IllegalArgumentException
    {
        elevationService.retrieveElevation(-90.000000000001, 45.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void refusesLongitudeOver180() throws IllegalArgumentException
    {
        elevationService.retrieveElevation(45.0, 180.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void refusesLongitudeUnderNegative180() throws IllegalArgumentException
    {
        elevationService.retrieveElevation(-45.0, -180.00001);
    }

    @SuppressWarnings("JUnitTestMethodWithNoAssertions") // no throw passes test
    @Test
    public void acceptsValidArguments()
    {
        elevationService.retrieveElevation(45.0, 99.5);
    }

}
