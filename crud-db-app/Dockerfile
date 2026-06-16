FROM tomcat:9.0-jdk8

COPY target/*.war /usr/local/tomcat/webapps/employee-directory.war

COPY newrelic.jar /opt/newrelic/newrelic.jar
COPY newrelic.yml /opt/newrelic/newrelic.yml

ENV CATALINA_OPTS="$CATALINA_OPTS -javaagent:/opt/newrelic/newrelic.jar"

EXPOSE 8080
