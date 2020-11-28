FROM adoptopenjdk:15-jdk-hotspot-bionic AS builder

ARG GRADLE_USER=gradle
ARG GRADLE_GROUP=gradle

RUN set -o errexit -o nounset \
    && groupadd --system --gid 1000 ${GRADLE_GROUP} \
    && useradd --system --gid ${GRADLE_GROUP} --uid 1000 --shell /bin/bash --create-home ${GRADLE_USER} \
    && chown --recursive ${GRADLE_USER}:${GRADLE_GROUP} /home/${GRADLE_USER} \
    && mkdir -p /home/src \
    && chown --recursive ${GRADLE_USER}:${GRADLE_GROUP} /home/src \
    && apt-get update \
    && apt-get install git -y

COPY --chown=${GRADLE_USER}:${GRADLE_GROUP} . /home/src
WORKDIR /home/src

USER ${GRADLE_USER}
RUN chmod +x ./gradlew && ./gradlew :nova-starter:installDist --stacktrace --no-daemon

FROM adoptopenjdk:15-jre-hotspot-bionic

ARG NOVA_USER=nova
ARG NOVA_GROUP=nova

ENV NOVA_HOME /opt/nova-framework
ENV DATA_DIR /data

RUN set -o errexit -o nounset \
    && groupadd --system --gid 1000 ${NOVA_GROUP} \
    && useradd --system --gid ${NOVA_GROUP} --uid 1000 --shell /bin/bash --create-home ${NOVA_USER} \
    && mkdir -p ${DATA_DIR} \
    && chown --recursive ${NOVA_USER}:${NOVA_GROUP} ${DATA_DIR} \
    && chown --recursive ${NOVA_USER}:${NOVA_GROUP} /home/${NOVA_USER}

WORKDIR ${DATA_DIR}
COPY --from=builder --chown=${NOVA_USER}:${NOVA_GROUP} /home/src/nova-starter/build/install/nova-starter/ ${NOVA_HOME}/

RUN ln --symbolic ${NOVA_HOME}/bin/nova-starter /usr/bin/nova \
  && chmod +x /usr/bin/nova
USER ${NOVA_USER}

ENTRYPOINT ["nova"]
