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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class GoogleElevationTest_Unit {

    private static final String TEST_RESPONSE_GOOGLE_ELEVATION_JSON = "/testResponse_googleElevation.json";

    @Test
    public void deserializesResults() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        URL src = getClass().getResource(TEST_RESPONSE_GOOGLE_ELEVATION_JSON);

        GoogleElevationData elevationData = mapper.readValue(src, GoogleElevationData.class);

        assertThat(elevationData.getElevation(), equalTo(1608.637939453125));
    }

    @Test
    public void usesKnownGoogleElevationApiContractAndReturnsGoogleElevationData() throws Exception
    {
        // given
        String rootUri = GoogleElevationService.ROOT_URI__ELEVATION;
        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(rootUri).build();
        GoogleElevationService localTestGoogleElevationService = new GoogleElevationServiceImpl(restTemplate);

        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);

        String testDataJsonString = String.join("", Files.readAllLines(
            Paths.get(getClass().getResource(TEST_RESPONSE_GOOGLE_ELEVATION_JSON).getPath())));

        Map<String, Double> testLatLongTuple = new HashMap<>(2);
        testLatLongTuple.put("lat", 28.59);
        testLatLongTuple.put("lon", 77.0);

        String targetUri = restTemplate
            .getUriTemplateHandler()
            .expand(GoogleElevationService.ENDPOINT_TEMPLATE__GET_ELEVATION,
                    testLatLongTuple.get("lat"),
                    testLatLongTuple.get("lon"),
                    "")
            .toString();

        // expect
        mockServer
            .expect(requestTo(targetUri))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(testDataJsonString, MediaType.APPLICATION_JSON));

        // when
        GoogleElevationData mockData = localTestGoogleElevationService
            .getElevation(testLatLongTuple.get("lat"), testLatLongTuple.get("lon"));

        mockServer.verify();
        assertThat(mockData, instanceOf(GoogleElevationData.class));
    }
}
