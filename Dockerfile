FROM openjdk:17-oracle

MAINTAINER Harro Fabian Fromme <h-f.fromme@posteo.de>

ADD target/AwsIpRangeChecker.jar app.jar

CMD [ "sh", "-c", "java -jar /app.jar" ]