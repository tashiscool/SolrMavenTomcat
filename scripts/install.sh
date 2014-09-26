#!/bin/sh

wget http://archive.apache.org/dist/tomcat/tomcat-7/v7.0.53/bin/apache-tomcat-7.0.53.tar.gz
mvn install:install-file -DgroupId=org.apache -DartifactId=tomcat -Dversion=7.0.53 -Dpackaging=tar.gz -Dfile=$(pwd)/apache-tomcat-7.0.53.tar.gz
cd ..
mvn clean install