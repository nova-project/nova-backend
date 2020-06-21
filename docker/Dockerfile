FROM adoptopenjdk/openjdk14:alpine-slim AS builder

WORKDIR /nova-backend
COPY . /nova-backend

RUN apk add --update --no-cache git \
  && chmod +x ./gradlew \
  && ./gradlew installDist

FROM adoptopenjdk/openjdk14:alpine-jre

WORKDIR /data
COPY --from=builder /nova-backend/nova-bootstrap/build/install/nova-bootstrap/ /nova-backend/

ENTRYPOINT ["/nova-backend/bin/nova-bootstrap"]
