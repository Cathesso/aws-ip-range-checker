FROM openjdk:17-oracle

MAINTAINER Harro Fabian Fromme <h-f.fromme@posteo.de>

ADD target/AwsIpRangeChecker.jar AwsIpRangeChecker.jar

CMD [ "sh", "-c", "java -jar /AwsIpRangeChecker.jar" ]