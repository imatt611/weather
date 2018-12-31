package matt.project.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static matt.project.weather.GoogleTimeZoneService.ENDPOINT_TEMPLATE__GET_TIMEZONE;
import static matt.project.weather.GoogleTimeZoneService.ROOT_URI__TIMEZONE;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class GoogleTimeZoneTest_Unit {

    private static final String TEST_RESPONSE_GOOGLE_TIME_ZONE_JSON = "/testResponse_googleTimeZone.json";
    private static final GoogleTimeZoneService timeZoneService = new GoogleTimeZoneServiceImpl(
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

    @Test
    public void deserializesResults() throws Exception
    {
        // expect
        GoogleTimeZoneData expectedTimeZoneData = new GoogleTimeZoneData();
        expectedTimeZoneData.setTimeZoneName("Eastern Daylight Time");

        // when
        ObjectMapper mapper = new ObjectMapper();
        URL src = getClass().getResource(TEST_RESPONSE_GOOGLE_TIME_ZONE_JSON);
        GoogleTimeZoneData timeZoneData = mapper.readValue(src, GoogleTimeZoneData.class);

        assertThat(timeZoneData, equalTo(expectedTimeZoneData));
    }

    @Test
    public void usesKnownGoogleTimeZoneApiContractAndReturnsGoogleTimeZoneData() throws Exception
    {
        // given
        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(ROOT_URI__TIMEZONE).build();
        GoogleTimeZoneService localTestGoogleTimeZoneService = new GoogleTimeZoneServiceImpl(restTemplate);

        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);

        String testDataJsonString = String.join("", Files.readAllLines(
            Paths.get(getClass().getResource(TEST_RESPONSE_GOOGLE_TIME_ZONE_JSON).getPath())));

        Map<String, Double> testLatLongTuple = new HashMap<>(2);
        testLatLongTuple.put("lat", 28.59);
        testLatLongTuple.put("lon", 77.0);

        // expect
        mockServer
            .expect(requestTo(
                stringContainsInOrder(
                    Arrays.asList(ROOT_URI__TIMEZONE,
                                  ENDPOINT_TEMPLATE__GET_TIMEZONE
                                      .substring(0, ENDPOINT_TEMPLATE__GET_TIMEZONE.indexOf('{')),
                                  testLatLongTuple.get("lat").toString(),
                                  testLatLongTuple.get("lon").toString(),
                                  String.valueOf(Instant.now().getEpochSecond())
                                      .substring(0, 6))))) // Close enough for unit test
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(testDataJsonString, MediaType.APPLICATION_JSON));

        // when
        GoogleTimeZoneData mockData = localTestGoogleTimeZoneService
            .retrieveTimeZone(testLatLongTuple.get("lat"), testLatLongTuple.get("lon"));

        mockServer.verify();
        assertThat(mockData, instanceOf(GoogleTimeZoneData.class));
    }
}
