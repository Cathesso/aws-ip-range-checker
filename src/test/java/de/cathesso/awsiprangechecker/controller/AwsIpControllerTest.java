package de.cathesso.awsiprangechecker.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import de.cathesso.awsiprangechecker.model.AwsIpRangeDTO;
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
    private final String linebreak = System.getProperty("line.separator");
    @Value("${AWS_IP_DATABASE}")
    private String awsIpRangeUrl;

    @Test
    @DisplayName("Integrating Controller and Service: US servers should be shown as String")
    void testRegionFilter() {
        //Given
        String expected = "42.42.42.42" + linebreak + "481.516.23.42" + linebreak + "1701.1138.1337.5141";

        String region = "us";
        
        when(mockedTemplate.getForEntity(awsIpRangeUrl, AwsIpRangeDTO.class))
        .thenReturn(ResponseEntity.ok(mockedApiResponse));
        //When
        String actual = controller.regionFilter(region);
        //Then
        assertThat(actual, is(expected));
    }

    @Test
    @DisplayName("Invalid regions result in an explanation")
    void testRegionFilterWrongRegion() {
        //Given
        String expected = "I'm sorry, I'm afraid 42 is not a valid region. Please use one of the following: All, EU, US,AP, CN, SA, AF or CA. Thank you.";
        
        String region = "42";
        //When
        String actual = controller.regionFilter(region);
        //Then
        assertThat(actual, is(expected));
    }

    // @Test
    // @DisplayName("Integrating all Service methods: Should give a String for no available Servers")
    // void testgetIPsForeSpecificAWSRegionNoServers() {
    //     //Given
    //     String region = "cn";
    //     String expected = "I'm Sorry, but I am afraid, there are no servers for this region.";
    //     when(mockedTemplate.getForEntity("https://ip-ranges.amazonaws.com/ip-ranges.json", AwsIpRangeDTO.class))
    //     .thenReturn(ResponseEntity.ok(mockedApiResponse));
    //     //When
    //     String actual = service.getIPsForSpecificAWSRegion(region);
    //     //Then
    //     assertThat(actual, is(expected));
    // }

    AwsIpRangeDTO mockedApiResponse = AwsIpRangeDTO.builder()
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