FROM openjdk:17-alpine
ARG WAR_FILE=restful-api/target/*.war
COPY ${WAR_FILE} ROOT.war
ENTRYPOINT ["java","-jar","/ROOT.war"]
EXPOSE 9876

#FROM tomcat:9.0-alpine
#COPY restful/target/restful-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
#EXPOSE 9876
#CMD ["catalina.sh", "run"]


#FROM openjdk:17-alpine
#ARG WAR_FILE=restful/target/*.war
#COPY ${WAR_FILE} ROOT.war
#
#
#FROM bitnami/tomcat:9.0
#RUN rm -rf /opt/bitnami/tomcat/webapps_default/ROOT
#RUN rm -rf /opt/bitnami/tomcat/webapps_default/ROOT.war
#COPY ROOT.war /opt/bitnami/tomcat/webapps_default/ROOT.war
#EXPOSE 8080
#CMD ["/opt/bitnami/scripts/tomcat/run.sh", "nami start"]