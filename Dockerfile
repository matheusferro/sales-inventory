# the first stage of our build will extract the layers
FROM amazoncorretto:18-alpine as builder
WORKDIR application
COPY target/*.jar application.jar

RUN apk update && apk add gcompat binutils
RUN java -Djarmode=layertools -jar application.jar extract

RUN $JAVA_HOME/bin/jlink \
         --verbose \
         #TODO: run jdeps to snapshot dependencies
         --add-modules $(jdeps --print-module-deps --ignore-missing-deps --recursive --multi-release 11 --class-path="/application/dependencies/BOOT-INF/lib/*" --module-path="/application/dependencies/BOOT-INF/lib/*" /application/application.jar) \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /customjre

# the second stage of our build will copy the extracted layers
FROM alpine:3.18

RUN addgroup appgroup; adduser  --ingroup appgroup --disabled-password appusr
USER appusr

ENV PATH="/jre/bin:${PATH}"

COPY --chown=appusr:appgroup --from=builder /customjre /jre
COPY --chown=appusr:appgroup --from=builder application/dependencies/ ./
COPY --chown=appusr:appgroup --from=builder application/spring-boot-loader/ ./
COPY --chown=appusr:appgroup --from=builder application/snapshot-dependencies/ ./
COPY --chown=appusr:appgroup --from=builder application/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
#ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "org.springframework.boot.loader.JarLauncher"]
#CMD java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 org.springframework.boot.loader.JarLauncher
