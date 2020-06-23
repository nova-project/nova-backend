FROM adoptopenjdk:14-jre-hotspot AS builder

RUN set -o errexit -o nounset \
    && groupadd --system --gid 1000 gradle \
    && useradd --system --gid gradle --uid 1000 --shell /bin/bash --create-home gradle \
    && chown --recursive gradle:gradle /home/gradle \
    && apt-get update \
    && apt-get install git -y

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

USER gradle
RUN chmod +x ./gradlew && ./gradlew installDist --stacktrace --no-daemon

FROM adoptopenjdk:14-jre-hotspot

ENV NOVA_HOME /opt/nova-backend
RUN set -o errexit -o nounset \
    && groupadd --system --gid 1000 nova \
    && useradd --system --gid nova --uid 1000 --shell /bin/bash --create-home nova \
    && mkdir -p /data \
    && chown --recursive nova:nova /data \
    && chown --recursive nova:nova /home/nova

WORKDIR /data
COPY --from=builder /home/gradle/src/nova-bootstrap/build/install/nova-bootstrap/ ${NOVA_HOME}/

RUN ln --symbolic "${NOVA_HOME}/bin/nova-bootstrap" /usr/bin/nova
USER nova

ENTRYPOINT ["nova"]
