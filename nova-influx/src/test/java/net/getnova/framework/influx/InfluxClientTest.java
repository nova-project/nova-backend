package net.getnova.framework.influx;

import org.junit.jupiter.api.Test;

class InfluxClientTest {

  @Test
//  @Disabled
  void write() {
    final InfluxProperties properties = new InfluxProperties();
    properties.setBucket("root");
    properties.setOrg("root");
    properties.setToken("root");

    final InfluxClient client = new InfluxClient(properties);

    final String query = "from(bucket: \"" + properties.getBucket() + "\")\n"
                         + "  |> range(start: -30d)\n";

    System.out.println(query);
    System.out.println(client.query(query).block());
  }
}
