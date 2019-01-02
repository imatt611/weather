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
import java.util.HashMap;
import java.util.Map;

import static matt.project.weather.GoogleApiInfo.ENDPOINT_TEMPLATE__GET_ELEVATION;
import static matt.project.weather.GoogleApiInfo.ROOT_URI;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestToUriTemplate;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class GoogleElevationTest_Unit {

    private static final String TEST_RESPONSE_GOOGLE_ELEVATION_JSON = "/testResponse_googleElevation.json";
    private static final ElevationService elevationService = new GoogleElevationServiceImpl(
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

    @Test
    public void deserializesResults() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        URL src = getClass().getResource(TEST_RESPONSE_GOOGLE_ELEVATION_JSON);

        ElevationData elevationData = mapper.readValue(src, GoogleElevationData.class);

        assertThat(elevationData.getElevation(), equalTo(1608.637939453125));
    }

    @Test
    public void usesKnownGoogleElevationApiContractAndReturnsGoogleElevationData() throws Exception
    {
        // given
        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(ROOT_URI).build();
        ElevationService localTestElevationService = new GoogleElevationServiceImpl(restTemplate);

        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);

        String testDataJsonString = String.join("", Files.readAllLines(
            Paths.get(getClass().getResource(TEST_RESPONSE_GOOGLE_ELEVATION_JSON).getPath())));

        Map<String, Double> testLatLongTuple = new HashMap<>(2);
        testLatLongTuple.put("lat", 28.59);
        testLatLongTuple.put("lon", 77.0);

        // expect
        mockServer
            .expect(requestToUriTemplate(ROOT_URI + ENDPOINT_TEMPLATE__GET_ELEVATION,
                                         testLatLongTuple.get("lat"),
                                         testLatLongTuple.get("lon"),
                                         ""))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(testDataJsonString, MediaType.APPLICATION_JSON));

        // when
        ElevationData mockData = localTestElevationService
            .retrieveElevation(testLatLongTuple.get("lat"), testLatLongTuple.get("lon"));

        mockServer.verify();
        assertThat(mockData, instanceOf(GoogleElevationData.class));
    }
}
