FROM eclipse-temurin:17-jre

RUN apt update && \
        apt install -y tzdata bash && \
        apt clean && \
        rm -rf /var/lib/apt/lists/*
ENV TZ Europe/London

COPY ./build/libs/order-service.jar /usr/app/

EXPOSE 8443:8443

WORKDIR /usr/app/

ENTRYPOINT ["java","-jar","order-service.jar"]