package de.cathesso.awsiprangechecker.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import de.cathesso.awsiprangechecker.model.AwsIpRangeDTO;
import de.cathesso.awsiprangechecker.model.Server;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AwsIpRangeServiceTest {

    private final RestTemplate mockedTemplate = mock(RestTemplate.class);
    private final AwsIpRangeService service = new AwsIpRangeService(mockedTemplate);
    private final String linebreak = System.getProperty("line.separator");


    @Test
    @DisplayName("Integrating all Service methods: Should give a List of filtered Servers")
    void testgetIPsForeSpecificAWSRegion() {
        //Given
        String region = "eu";
        List<Server> expected = List.of(Server.builder()
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
        .build());
        when(mockedTemplate.getForEntity("https://ip-ranges.amazonaws.com/ip-ranges.json", AwsIpRangeDTO.class))
        .thenReturn(ResponseEntity.ok(mockedApiResponse));
        //When
        List<Server> actual = service.getIPsForSpecificAWSRegion(region);
        //Then
        assertThat(actual, is(expected));
    }

//ToDo: Add test for region without servers
//ToDo: Add test for IPv6

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
        .build()))
    .build();
}

