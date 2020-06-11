# docker build -t hazelcast/vaadin-session .

FROM node:14.4-buster-slim as build
RUN apt-get update \
 && apt-get install -y wget gnupg software-properties-common \
 && wget -qO - https://adoptopenjdk.jfrog.io/adoptopenjdk/api/gpg/key/public | apt-key add - \
 && add-apt-repository --yes https://adoptopenjdk.jfrog.io/adoptopenjdk/deb/ \
 && apt-get update \
 && mkdir -p /usr/share/man/man1 \
 && apt-get install -y adoptopenjdk-14-hotspot maven
WORKDIR /root
COPY pom.xml .
RUN mvn -B dependency:resolve-plugins dependency:resolve
COPY src src
COPY types.d.ts .
COPY tsconfig.json .
RUN mvn package -Pproduction

FROM tomcat:9.0-jdk14-openjdk-slim-buster
COPY --from=build /root/target/session-replication-1.0-SNAPSHOT.war webapps/ROOT.war
ADD https://repo1.maven.org/maven2/com/hazelcast/hazelcast-all/4.0.1/hazelcast-all-4.0.1.jar lib/
ADD https://repo1.maven.org/maven2/com/hazelcast/hazelcast-tomcat9-sessionmanager/2.1/hazelcast-tomcat9-sessionmanager-2.1.jar lib/
COPY tomcat/context.xml conf/
COPY tomcat/server.xml conf/
COPY tomcat/hazelcast.xml conf/
RUN echo "export CATALINA_OPTS='--add-modules java.se --add-exports java.base/jdk.internal.ref=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.nio=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED --add-opens java.management/sun.management=ALL-UNNAMED --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED -Dorg.apache.tomcat.util.digester.PROPERTY_SOURCE=org.apache.tomcat.util.digester.EnvironmentPropertySource'" >> /root/.bashrc
CMD . /root/.bashrc && bin/catalina.sh run