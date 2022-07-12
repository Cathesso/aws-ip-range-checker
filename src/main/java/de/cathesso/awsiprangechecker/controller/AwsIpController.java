package de.cathesso.awsiprangechecker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            return awsIpRangeService.getIPsForeSpecificAWSRegion(region);
        }
        
        else{
            return "I'm sorry, I'm afraid " + region + " is not a valid region. Please use one of the following: All, EU, US,AP, CN, SA, AF or CA. Thank you.";
        }

    }


}
