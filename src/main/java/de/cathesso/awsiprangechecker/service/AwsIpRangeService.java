package de.cathesso.awsiprangechecker.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import de.cathesso.awsiprangechecker.model.AwsIpRangeDTO;
import de.cathesso.awsiprangechecker.model.Server;

@Service
public class AwsIpRangeService {
    private final RestTemplate restTemplate;

    public AwsIpRangeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

        public List<Server> getIPsForSpecificAWSRegion(String region){
            AwsIpRangeDTO ipResponse = getAwsApiResponse();
            List<Server> extractedServers = extractServersFromApiResponse(ipResponse);
            return filterServersForRegion(region, extractedServers);
    }

        private AwsIpRangeDTO getAwsApiResponse() {

            String awsIpRangeUrl = "https://ip-ranges.amazonaws.com/ip-ranges.json";
            ResponseEntity<AwsIpRangeDTO> response = restTemplate.getForEntity(awsIpRangeUrl, AwsIpRangeDTO.class);
            if (response.getBody() != null) {
                return response.getBody();
            }
            return null;
        }

        private List<Server> extractServersFromApiResponse(AwsIpRangeDTO response){
            return response.getServers();
        }

        private List<Server> filterServersForRegion(String region, List<Server> serverList){
            List<Server> allServers = serverList;
            if (region.equals("all")){
                return allServers;
            }
            return allServers.stream().filter(server -> server.getRegion().startsWith(region + "-")).collect(Collectors.toList());
        }
    
}
