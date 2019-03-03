package matt.project.weather.timezone;

import matt.project.weather.google.GoogleTimeZoneService;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.mock;

public class TimeZoneTest_Unit {

    private static final TimeZoneService timeZoneService = new GoogleTimeZoneService(
        mock(RestTemplate.class));

    @Test(expected = IllegalArgumentException.class)
    public void refusesLatitudeOver90() throws IllegalArgumentException
    {
        timeZoneService.retrieveTimeZone(90.000000000001, 45.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void refusesLatitudeUnderNegative90() throws IllegalArgumentException
    {
        timeZoneService.retrieveTimeZone(-90.000000000001, 45.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void refusesLongitudeOver180() throws IllegalArgumentException
    {
        timeZoneService.retrieveTimeZone(45.0, 180.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void refusesLongitudeUnderNegative180() throws IllegalArgumentException
    {
        timeZoneService.retrieveTimeZone(-45.0, -180.00001);
    }

    @SuppressWarnings("JUnitTestMethodWithNoAssertions") // no throw passes test
    @Test
    public void acceptsValidArguments()
    {
        timeZoneService.retrieveTimeZone(45.0, 99.5);
    }

}
