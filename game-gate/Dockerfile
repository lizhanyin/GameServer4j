FROM eclipse-temurin:25-ubi10-minimal

COPY target/lib /usr/src/game-gate/lib
COPY target/game-gate-releases.jar /usr/src/game-gate/game-gate-releases.jar
COPY game-gate-scripts/src /usr/src/game-gate-scripts/src

ENV CLASSPATH=".:${JAVA_HOME}/lib:/usr/src/game-gate/lib"

WORKDIR /usr/src/game-gate

CMD ["java", "-jar", "game-gate-releases.jar"]
