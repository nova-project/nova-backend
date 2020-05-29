FROM adoptopenjdk:14-jdk-hotspot-bionic AS builder

WORKDIR /nova-backend
COPY . /nova-backend

RUN apt update \
  && apt install git -y \
  && chmod +x ./gradlew \
  && ./gradlew installDist

FROM adoptopenjdk:14-jdk-hotspot

WORKDIR /nova-backend
COPY --from=builder /nova-backend/nova-bootstrap/build/install/nova-bootstrap/ .

ENTRYPOINT ["./bin/nova-bootstrap"]
