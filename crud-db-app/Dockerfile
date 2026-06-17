FROM tomcat:9.0-jdk8

RUN mkdir -p /opt/newrelic

COPY target/*.war /usr/local/tomcat/webapps/employee-directory.war

COPY newrelic.jar /opt/newrelic/newrelic.jar
COPY newrelic.yml /opt/newrelic/newrelic.yml

ENV CATALINA_OPTS="-javaagent:/opt/newrelic/newrelic.jar"

EXPOSE 8080

# -----------

FROM tomcat:9.0-jdk8

COPY target/*.war /usr/local/tomcat/webapps/employee-directory.war

EXPOSE 8080