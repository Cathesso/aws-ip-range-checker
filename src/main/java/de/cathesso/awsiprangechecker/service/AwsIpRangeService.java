package de.cathesso.awsiprangechecker.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import de.cathesso.awsiprangechecker.model.AwsIpRangeResponse;
import de.cathesso.awsiprangechecker.model.Server;

@Service
public class AwsIpRangeService {
    private final RestTemplate restTemplate;

    public AwsIpRangeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

        //0. Call functions
        public String getIPsForeSpecificAWSRegion(String region){
            AwsIpRangeResponse ipResponse = getAwsApiResponse();
            List<Server> extractedServers = extractServersFromApiResponse(ipResponse);
            List<Server> filteredServers = filterServersForRegion(region, extractedServers);
            return getServerIPsAndConvertToString(filteredServers);
    }



        //1. Get all Data
        public AwsIpRangeResponse getAwsApiResponse() {

            String awsIpRangeUrl = "https://ip-ranges.amazonaws.com/ip-ranges.json";
            ResponseEntity<AwsIpRangeResponse> response = restTemplate.getForEntity(awsIpRangeUrl, AwsIpRangeResponse.class);
            if (response.getBody() != null) {
                return response.getBody();
            }
            return null;
        }

        //2. Extract all Servers from the API
        public List<Server> extractServersFromApiResponse(AwsIpRangeResponse response){
            return response.getServers();
        }

        //3. Filter to only my specific IP Range
        public List<Server> filterServersForRegion(String region, List<Server> serverList){
            List<Server> allServers = serverList;
            if (region.equals("all")){
                return allServers;
            }
            return allServers.stream().filter(server -> server.getRegion().startsWith(region + "-")).collect(Collectors.toList());
        }

        //4. Display only IPs and add linebreaks
        public String getServerIPsAndConvertToString (List<Server> servers){
            List<String> filteredIPs = servers.stream().map(Server::getIpPrefix).collect(Collectors.toList());
            if (filteredIPs.isEmpty())
            {return "I'm Sorry, but I am afraid, there are no servers for this region.";}
            else 
            {return filteredIPs.toString().replace(", ", System.getProperty("line.separator")).replace("[", "").replace("]", "");}
        }
    
}
