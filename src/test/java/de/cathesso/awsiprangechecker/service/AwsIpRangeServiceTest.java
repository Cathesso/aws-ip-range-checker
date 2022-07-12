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
    @DisplayName("All servers of region 'AP' should be filtered")
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
    @DisplayName("All IPs should be extracted and from a given server list and turned into a String with linebreaks")
    void testgetServerIPsAndConvertToString() {
        //Given
        String expected = "192.168.0.0" + linebreak + "48.151.6.23" + linebreak + "1.23.45.67" + linebreak + "42.42.42.42";

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
    @DisplayName("Integrating all Service methods: Should give a String of filtered server IPs")
    void testgetIPsForeSpecificAWSRegion() {
        //Given
        String region = "eu";
        String expected = "192.168.0.0" + linebreak + "48.151.6.23";
        when(mockedTemplate.getForEntity("https://ip-ranges.amazonaws.com/ip-ranges.json", AwsIpRangeDTO.class))
        .thenReturn(ResponseEntity.ok(mockedApiResponse));
        //When
        String actual = service.getIPsForeSpecificAWSRegion(region);
        //Then
        assertThat(actual, is(expected));
    }

    @Test
    @DisplayName("Integrating all Service methods: Should give a String for no available Servers")
    void testgetIPsForeSpecificAWSRegionNoServers() {
        //Given
        String region = "cn";
        String expected = "I'm Sorry, but I am afraid, there are no servers for this region.";
        when(mockedTemplate.getForEntity("https://ip-ranges.amazonaws.com/ip-ranges.json", AwsIpRangeDTO.class))
        .thenReturn(ResponseEntity.ok(mockedApiResponse));
        //When
        String actual = service.getIPsForeSpecificAWSRegion(region);
        //Then
        assertThat(actual, is(expected));
    }

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

