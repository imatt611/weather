package matt.project.weather.elevation.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import matt.project.weather.elevation.ElevationData;
import matt.project.weather.elevation.ElevationService;
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

import static matt.project.weather.util.GoogleApiConstants.ENDPOINT_TEMPLATE__GET_ELEVATION;
import static matt.project.weather.util.GoogleApiConstants.ROOT_URI;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestToUriTemplate;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class GoogleElevationTest_Unit {

    private static final String TEST_RESPONSE_GOOGLE_ELEVATION_JSON = "/testResponse_googleElevation.json";

    @Test
    public void evaluatesEquality_whenTrue()
    {
        //given
        ElevationData elevationData1 = new GoogleElevationData();
        elevationData1.setElevation(5.0);
        ElevationData elevationData2 = new GoogleElevationData();
        elevationData2.setElevation(5.0);

        //expect
        assertThat(elevationData1, equalTo(elevationData2));
    }

    @Test
    public void evaluatesEquality_whenFalse()
    {
        //given
        ElevationData elevationData1 = new GoogleElevationData();
        elevationData1.setElevation(5.0);
        ElevationData elevationData2 = new GoogleElevationData();
        elevationData2.setElevation(1.4);

        //expect
        assertThat(elevationData2, not(equalTo(elevationData1)));
    }

    @Test
    public void deserializesResults() throws Exception
    {
        // expect
        ElevationData expectedElevationData = new GoogleElevationData();
        expectedElevationData.setElevation(1608.637939453125);

        // when
        ObjectMapper mapper = new ObjectMapper();
        URL src = getClass().getResource(TEST_RESPONSE_GOOGLE_ELEVATION_JSON);
        ElevationData elevationData = mapper.readValue(src, GoogleElevationData.class);

        assertThat(elevationData, equalTo(expectedElevationData));
    }

    @Test
    public void usesKnownGoogleElevationApiContractAndReturnsElevationData() throws Exception
    {
        // given
        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(ROOT_URI).build();
        ElevationService localTestElevationService = new GoogleElevationService(restTemplate);

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
        assertThat(mockData, instanceOf(ElevationData.class));
    }

}
