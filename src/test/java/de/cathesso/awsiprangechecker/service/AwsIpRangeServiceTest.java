package de.cathesso.awsiprangechecker.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import de.cathesso.awsiprangechecker.model.AwsIpRangeResponse;
import de.cathesso.awsiprangechecker.model.Server;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AwsIpRangeServiceTest {

    private final RestTemplate mockedTemplate = mock(RestTemplate.class);
    private final AwsIpRangeService service = new AwsIpRangeService(mockedTemplate);

    @Test
    @DisplayName("All Servers should be extracted from the API response")
    void testExtractServersFromApiResponse() {
        //Given
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
        .build());
        //When
        List<Server> actual = service.extractServersFromApiResponse(mockedApiResponse);
        //Then
        assertThat(actual, is(expected));

    }

    @Test
    void testFilterServerForRegions() {
        //Given

        String region = "ap";

        List<Server> givenServers = List.of(Server.builder()
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
        .build());

        List<Server> expected = List.of(
        Server.builder()
        .ipPrefix("1.23.45.67")
        .region("ap-central")
        .service("Bonprix")
        .networkBorderGroup("ap-central")
        .build());
        //When

        List<Server> actual = service.filterServersForRegion(region, givenServers);
        //Then
        assertThat(actual, is(expected));
    }

    @Test
    void testgetServerIPsAndConvertToString() {
        //Given
        String expected = "192.168.0.0\r\n48.151.6.23\r\n1.23.45.67\r\n42.42.42.42";
        expected = expected.replace("\r\n", System.getProperty("line.separator"));

        List<Server> mockedServersAll = List.of(Server.builder()
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
        .build());

        //When
        String actual = service.getServerIPsAndConvertToString(mockedServersAll);
        //Then
        assertThat(actual, is(expected));
    }

    @Test
    void testgetIPsForeSpecificAWSRegion() {
        //Given
        String region = "eu";
        String expected = "192.168.0.0\r\n48.151.6.23";
        when(mockedTemplate.getForEntity("https://ip-ranges.amazonaws.com/ip-ranges.json", AwsIpRangeResponse.class))
        .thenReturn(ResponseEntity.ok(mockedApiResponse));
        //When
        String actual = service.getIPsForeSpecificAWSRegion(region);
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
        .build()))
    .build();
}

