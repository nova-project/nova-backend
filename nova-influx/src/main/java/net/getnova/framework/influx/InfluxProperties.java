package net.getnova.framework.influx;

import lombok.Data;

@Data
public class InfluxProperties {

  private String uri = "http://localhost:8086";
  private String bucket;
  private String org;
  private String token;
}
