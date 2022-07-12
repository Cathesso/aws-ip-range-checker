package de.cathesso.awsiprangechecker.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AwsIpRangeDTO {
    private String syncToken;
    private String createDate;
    @JsonProperty("prefixes")
    private List<Server> servers;
}
