package de.cathesso.awsiprangechecker.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import de.cathesso.awsiprangechecker.model.AwsIpRangeDTO;
import de.cathesso.awsiprangechecker.model.Server;
import java.util.Collections;

@Service
public class AwsIpRangeService {
    private final RestTemplate restTemplate;
    @Value("${AWS_IP_DATABASE}")
    private String awsIpRangeUrl;

    public AwsIpRangeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

        public List<Server> getIPsForSpecificAWSRegion(String region){
            AwsIpRangeDTO ipResponse = getAwsApiResponse();
            List<Server> extractedServers = extractServersFromApiResponse(ipResponse);
            return filterServersForRegion(region, extractedServers);
    }

        private AwsIpRangeDTO getAwsApiResponse() {
             // = "https://ip-ranges.amazonaws.com/ip-ranges.json";
            ResponseEntity<AwsIpRangeDTO> response = restTemplate.getForEntity(awsIpRangeUrl, AwsIpRangeDTO.class);
            if (response.getBody() != null) {
                return response.getBody();
            }
            return null;
        }

        private List<Server> extractServersFromApiResponse(AwsIpRangeDTO response){
            List<Server> ipv4Server = response.getServers();
            List<Server> ipv6Server = response.getServersv6();
            if (ipv4Server != null && ipv6Server != null){
                return Stream.concat(response.getServers().stream(), response.getServersv6().stream()).toList();
            }
            else if (ipv4Server == null){
                return response.getServersv6();
            }
            else if (ipv6Server == null){
                return response.getServers();
            }
            return Collections.emptyList();
        }

        private List<Server> filterServersForRegion(String region, List<Server> serverList){
            List<Server> allServers = serverList;
            if (region.equals("all")){
                return allServers;
            }
            return allServers.stream().filter(server -> server.getRegion().startsWith(region + "-")).collect(Collectors.toList());
        }
    
}
