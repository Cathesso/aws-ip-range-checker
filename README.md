# AWS IP Range Checker
Tool to filter Server IPs of AWS Servers for specific Regions.

## Usage
Go to https://aws-ip-checker.herokuapp.com/awsChecker?region= and enter a region for the URL-query-parameter "region".

Valid regions are: "EU", "US", "AP", "CN", "SA", "AF" and "CA".

To see all IPs without filtering use "ALL".

## Example
To see all IPs of European servers, please use this call: 

https://aws-ip-checker.herokuapp.com/awsChecker?region=eu 

## Data
The data source can be found here: 

https://ip-ranges.amazonaws.com/ip-ranges.json
