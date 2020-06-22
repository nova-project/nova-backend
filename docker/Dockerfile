FROM gradle:jdk14 AS builder

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

RUN apt-get update \
  && apt-get install git -y \
  && chmod -R +x /opt/java

USER gradle

RUN gradle :nova-bootstrap:installDist --stacktrace

FROM adoptopenjdk:14-jre-hotspot

ENV NOVA_HOME /opt/nova-backend
RUN set -o errexit -o nounset \
    && echo "Adding nova user and group" \
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
