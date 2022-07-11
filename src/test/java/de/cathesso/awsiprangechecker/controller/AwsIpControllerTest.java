package de.cathesso.awsiprangechecker.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import de.cathesso.awsiprangechecker.model.AwsIpRangeResponse;
import de.cathesso.awsiprangechecker.model.Server;
import de.cathesso.awsiprangechecker.service.AwsIpRangeService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

public class AwsIpControllerTest {

    private final RestTemplate mockedTemplate = mock(RestTemplate.class);
    private final AwsIpRangeService service = new AwsIpRangeService(mockedTemplate);
    private final AwsIpController controller = new AwsIpController(service);

    @Test
    void testRegionFilter() {
        //Given
        String expected = "42.42.42.42\r\n481.516.23.42\r\n1701.1138.1337.5141";
        
        when(mockedTemplate.getForEntity("https://ip-ranges.amazonaws.com/ip-ranges.json", AwsIpRangeResponse.class))
        .thenReturn(ResponseEntity.ok(mockedApiResponse));
        //When
        String region = "us";
        String actual = controller.regionFilter(region);
        //Then

        
        assertThat(actual, is(expected));

    }

    AwsIpRangeResponse mockedApiResponse = AwsIpRangeResponse.builder()
    .syncToken("123")
    .createDate("456")
    .servers(List.of(Server.builder()
        .ipPrefix("192.168.0.0")
        .region("eu-north")
        .service("Amazing")
        .networkBorderGroup("eu-north")
        .build(), 
        Server.builder()
        .ipPrefix("48.151.6.23")
        .region("eu-south")
        .service("OTTO")
        .networkBorderGroup("eu-south")
        .build(),
        Server.builder()
        .ipPrefix("1.23.45.67")
        .region("ap-central")
        .service("Bonprix")
        .networkBorderGroup("ap-central")
        .build(),
        Server.builder()
        .ipPrefix("42.42.42.42")
        .region("us-north")
        .service("Group")
        .networkBorderGroup("us-north")
        .build(),
        Server.builder()
        .ipPrefix("481.516.23.42")
        .region("us-south")
        .service("Group")
        .networkBorderGroup("us-south")
        .build(),
        Server.builder()
        .ipPrefix("1701.1138.1337.5141")
        .region("us-south")
        .service("Group")
        .networkBorderGroup("cn-central")
        .build()))
    .build();

}