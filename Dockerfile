FROM tomcat
ADD ["spring-boot-jpa.war" , "/usr/local/tomcat/webapps"]