FROM adoptopenjdk:14-jdk-hotspot AS builder

ARG GRADLE_USER=gradle
ARG GRADLE_GROUP=gradle

RUN set -o errexit -o nounset \
    && groupadd --system --gid 1000 ${GRADLE_GROUP} \
    && useradd --system --gid ${GRADLE_GROUP} --uid 1000 --shell /bin/bash --create-home ${GRADLE_USER} \
    && chown --recursive ${GRADLE_USER}:${GRADLE_GROUP} /home/${GRADLE_USER} \
    && apt-get update \
    && apt-get install git -y

COPY --chown=gradle:gradle . /home/${GRADLE_USER}/src
WORKDIR /home/${GRADLE_USER}/src

USER ${GRADLE_USER}
RUN chmod +x ./gradlew && ./gradlew installDist --stacktrace --no-daemon

FROM adoptopenjdk:14-jre-hotspot

ARG NOVA_USER=nova
ARG NOVA_GROUP=nova

ENV NOVA_HOME /opt/nova-backend
ENV DATA_DIR /data

RUN set -o errexit -o nounset \
    && groupadd --system --gid 1000 ${NOVA_GROUP} \
    && useradd --system --gid ${NOVA_GROUP} --uid 1000 --shell /bin/bash --create-home ${NOVA_USER} \
    && mkdir -p ${DATA_DIR} \
    && chown --recursive ${NOVA_USER}:${NOVA_GROUP} ${DATA_DIR} \
    && chown --recursive ${NOVA_USER}:${NOVA_GROUP} /home/${NOVA_USER}

WORKDIR ${DATA_DIR}
COPY --from=builder /home/gradle/src/nova-bootstrap/build/install/nova-bootstrap/ ${NOVA_HOME}/

RUN ln --symbolic ${NOVA_HOME}/bin/nova-bootstrap /usr/bin/nova
USER ${NOVA_USER}

ENTRYPOINT ["nova"]
