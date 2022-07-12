package de.cathesso.awsiprangechecker.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Server {
    @JsonProperty("ip_prefix")
    private String ipPrefix;
    private String region;
    private String service;
    @JsonProperty("network_border_group")
    private String networkBorderGroup;
   
}
