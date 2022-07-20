package de.cathesso.awsiprangechecker.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.cathesso.awsiprangechecker.model.Server;
import de.cathesso.awsiprangechecker.service.*;

@RestController
@RequestMapping(value = "/awsChecker", produces = "text/plain")
public class AwsIpController {

    private final AwsIpRangeService awsIpRangeService;

    public AwsIpController(AwsIpRangeService awsIpRangeService){
        this.awsIpRangeService = awsIpRangeService;
    }

    @GetMapping("")
    public String regionFilter (@RequestParam String region){
        if(region.isEmpty()){
            return "Please enter a region as '?region='. Valid regions are: All, EU, US,AP, CN, SA, AF or CA. Thank you.";
        }
        region = region.toLowerCase();
        if(region.matches("all|eu|us|ap|cn|sa|af|ca")){
            List<Server> filteredServers = awsIpRangeService.getIPsForSpecificAWSRegion(region);
            return getServerIPsAndConvertToString(filteredServers);
        }
        
        else{
            return "I'm sorry, I'm afraid " + region + " is not a valid region. Please use one of the following: All, EU, US,AP, CN, SA, AF or CA. Thank you.";
        }

    }

    private String getServerIPsAndConvertToString (List<Server> servers){
        List<String> filteredIPs = servers.stream().map(Server::getIpPrefix).collect(Collectors.toList());
        if (filteredIPs.isEmpty())
        {return "I'm Sorry, but I am afraid, there are no servers for this region.";}
        else 
        {return filteredIPs.toString().replace(", ", System.getProperty("line.separator")).replace("[", "").replace("]", "");}
    }

}
