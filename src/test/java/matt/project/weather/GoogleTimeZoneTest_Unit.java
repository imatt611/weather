package matt.project.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class GoogleTimeZoneTest_Unit {

    private static final String TEST_RESPONSE_GOOGLE_TIME_ZONE_JSON = "/testResponse_googleTimeZone.json";

    @Value(GoogleTimeZoneService.PROP_REF__API_KEY_GOOGLE_TIME_ZONE)
    private String apiKey;

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
        String rootUri = GoogleTimeZoneService.ROOT_URI;
        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(rootUri).build();
        GoogleTimeZoneService localTestGoogleTimeZoneService = new GoogleTimeZoneServiceImpl(restTemplate);

        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);

        String testDataJsonString = String.join("", Files.readAllLines(
                Paths.get(getClass().getResource(TEST_RESPONSE_GOOGLE_TIME_ZONE_JSON).getPath())));

        Map<String, Double> testLatLongTuple = new HashMap<>(2);
        testLatLongTuple.put("lat", 28.59);
        testLatLongTuple.put("lon", 77.0);

        String targetUri = restTemplate
                .getUriTemplateHandler()
                .expand(GoogleTimeZoneService.GET_TIMEZONE_ENDPOINT_TEMPLATE,
                        testLatLongTuple.get("lat"),
                        testLatLongTuple.get("lon"),
                        Instant.now().getEpochSecond(),
                        apiKey)
                .toString();

        // expect
        mockServer
                .expect(requestTo(targetUri))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(testDataJsonString, MediaType.APPLICATION_JSON));

        // when
        GoogleTimeZoneData mockData = localTestGoogleTimeZoneService
                .getTimeZone(testLatLongTuple.get("lat"), testLatLongTuple.get("lon"));

        mockServer.verify();
        assertThat(mockData, instanceOf(GoogleTimeZoneData.class));
    }
}
