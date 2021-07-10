package net.getnova.framework.influx;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.Data;
import net.getnova.framework.influx.excpetion.InfluxAuthException;
import net.getnova.framework.influx.excpetion.InfluxException;
import net.getnova.framework.influx.excpetion.InfluxInvalidRequest;
import net.getnova.framework.influx.excpetion.InfluxPayloadToLargeException;
import net.getnova.framework.influx.excpetion.InfluxQuotaException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class InfluxClient {

  private static final String API_VERSION = "v2";
  private static final String LINE_PROTOCOL_TYPE = "text/plain; charset=utf-8";
  private static final String LINE_SEPARATOR = "\n";
  private static final String INFLUXQL = "influxql";
  private static final String VND_FLUX = "application/vnd.flux";
  private static final String CSV_DELIMITER = ",";

  private final InfluxProperties properties;
  private final WebClient client;

  public InfluxClient(final InfluxProperties properties) {
    this.properties = properties;
    this.client = WebClient.builder()
      .baseUrl(properties.getUri() + "/api/" + API_VERSION + "/")
      .defaultHeader(HttpHeaders.AUTHORIZATION, "Token " + properties.getToken())
      .build();
  }

  public Mono<Void> write(final Flux<Measurement> measurements, final WritePrecision precision) {
    final Mono<String> body = measurements.map(measurement -> measurement.toLineProtocol(precision))
      .collectList()
      .map(data -> String.join(LINE_SEPARATOR, data));

    return this.client.post()
      .uri(builder -> builder.path("write")
        .queryParam("bucket", this.properties.getBucket())
        .queryParam("org", this.properties.getOrg())
        .queryParam("precision", precision)
        .build()
      )
      .body(body, String.class)
      .header(HttpHeaders.CONTENT_TYPE, LINE_PROTOCOL_TYPE)
      .exchangeToMono(this::handleResponse);
  }

  public Mono<Void> delete(final OffsetDateTime start, final OffsetDateTime end, final String predicate) {
    final DeleteRequest body = new DeleteRequest(start, end, predicate);

    return this.client.post()
      .uri(builder -> builder.path("delete")
        .queryParam("bucket", this.properties.getBucket())
        .queryParam("org", this.properties.getOrg()).build()
      )
      .body(body, DeleteRequest.class)
      .exchangeToMono(this::handleResponse);
  }

  public Mono<String> query(final String query) {
    return this.client.post()
      .uri(builder -> builder.path("query")
        .queryParam("org", this.properties.getOrg()).build()
      )
      .header(HttpHeaders.CONTENT_TYPE, VND_FLUX)
      .body(Mono.just(query), String.class)
      // string for every line
      .exchangeToMono(response -> handleResponse(response, () -> response.bodyToMono(String.class)));
  }

  private Mono<Void> handleResponse(final ClientResponse response) {
    return this.handleResponse(response, Mono::empty);
  }

  private <T> Mono<T> handleResponse(final ClientResponse response, final Supplier<Mono<T>> success) {
    switch (response.statusCode()) {
      case NO_CONTENT:
        return success.get();
      case BAD_REQUEST:
        return this.createException(response, InfluxInvalidRequest::new);
      case UNAUTHORIZED:
      case FORBIDDEN:
        return this.createException(response, InfluxAuthException::new);
      case NOT_FOUND:
      case PAYLOAD_TOO_LARGE:
        return this.createException(response, InfluxPayloadToLargeException::new);
      case TOO_MANY_REQUESTS:
      case SERVICE_UNAVAILABLE:
        final List<String> retryAfter = response.headers().header(HttpHeaders.RETRY_AFTER); // seconds

        return retryAfter.size() == 0
          ? Mono.error(new InfluxQuotaException())
          : Mono.error(new InfluxQuotaException(Long.parseLong(retryAfter.get(0))));
      default:
        return this.createException(response, InfluxException::new);
    }
  }

  private <T> Mono<T> createException(final ClientResponse response, final Function<String, InfluxException> ex) {
    return response.bodyToMono(InfluxErrorResponse.class).flatMap(error -> Mono.error(ex.apply(error.getMessage())));
  }

  private Flux<Map<String, String>> parseCsv(final Flux<String> data) {
    var ref = new Object() {
      String[] header = null;
    };

    return data.flatMap(line -> {
      if (ref.header == null) {
        ref.header = parseHeader(line);
        return Mono.empty();
      }

      return Mono.just(this.parseCsv(ref.header, line));
    });
  }

  private String[] parseHeader(final String data) {
    return data.split(CSV_DELIMITER);
  }

  private Map<String, String> parseCsv(final String[] header, final String data) {
    final String[] values = data.split(CSV_DELIMITER);

    if (values.length != header.length) {
      throw new IllegalArgumentException("Invalid csv");
    }

    final Map<String, String> result = new HashMap<>(header.length);

    for (int i = 0; i < header.length; i++) {
      result.put(header[i], values[i]);
    }

    return result;
  }

  @Data
  private static final class DeleteRequest {

    private final OffsetDateTime start;
    private final OffsetDateTime end;
    private final String predicate;
  }

  @Data
  private static final class InfluxErrorResponse {

    private final String message;

    public InfluxErrorResponse(
      @JsonProperty("message") final String message
    ) {
      this.message = message;
    }
  }
}
