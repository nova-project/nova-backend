package net.getnova.framework.web.client.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import java.time.Duration;
import java.util.function.Consumer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.getnova.framework.core.JsonUtils;
import net.getnova.framework.web.server.http.HttpUtils;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufMono;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient.ResponseReceiver;
import reactor.netty.http.client.HttpClientResponse;
import reactor.netty.resources.LoopResources;

@Getter
@EqualsAndHashCode
public class HttpClient {

  private static final Duration TIMEOUT = Duration.ofSeconds(5);
  private final reactor.netty.http.client.HttpClient client;
  private final ObjectMapper objectMapper;

  public HttpClient(
    final LoopResources resources,
    final String baseUrl,
    final boolean compress,
    final ObjectMapper objectMapper
  ) {
    this(resources, baseUrl, compress, objectMapper, null);
  }

  public HttpClient(
    final LoopResources resources,
    final String baseUrl,
    final boolean compress,
    final ObjectMapper objectMapper,
    final Consumer<HttpHeaders> headers
  ) {
    reactor.netty.http.client.HttpClient client = reactor.netty.http.client.HttpClient.create()
      .runOn(resources)
      .baseUrl(baseUrl)
      .followRedirect(false)
      .disableRetry(false)
      .keepAlive(true)
      .compress(compress)
      .responseTimeout(TIMEOUT)
      .protocol(HttpProtocol.HTTP11, HttpProtocol.H2C, HttpProtocol.H2);

    if (headers != null) {
      client = client.headers(headers);
    }

    this.client = client;
    this.objectMapper = objectMapper;
  }

  public ResponseReceiver<?> request(final HttpMethod method, final String uri) {
    return this.client.request(method)
      .uri(uri);
  }

  public <T> Mono<T> request(final HttpMethod method, final String uri, final Class<T> clazz) {
    return this.request(method, uri)
      .responseSingle((response, responseData) -> this.handleJson(method, uri, response, responseData, clazz));
  }

  public ResponseReceiver<?> request(final HttpMethod method, final String uri, final ByteBufMono data) {
    return this.client.request(method)
      .uri(uri)
      .send(data);
  }

  public <T> Mono<T> request(final HttpMethod method, final String uri, final ByteBufMono data, final Class<T> clazz) {
    return this.request(method, uri, data)
      .responseSingle((response, responseData) -> this.handleJson(method, uri, response, responseData, clazz));
  }

  // =========================================
  // Http Methods
  // =========================================

  public ResponseReceiver<?> get(final String uri) {
    return this.request(HttpMethod.GET, uri);
  }

  public <T> Mono<T> get(final String uri, final Class<T> clazz) {
    return this.request(HttpMethod.GET, uri, clazz);
  }

  public Mono<HttpClientResponse> head(final String uri) {
    return this.request(HttpMethod.HEAD, uri)
      .response();
  }

  public ResponseReceiver<?> post(final String uri, final ByteBufMono data) {
    return this.request(HttpMethod.POST, uri, data);
  }

  public <T> Mono<T> post(final String uri, final ByteBufMono data, final Class<T> clazz) {
    return this.request(HttpMethod.POST, uri, data, clazz);
  }

  public ResponseReceiver<?> put(final String uri, final ByteBufMono data) {
    return this.request(HttpMethod.PUT, uri, data);
  }

  public <T> Mono<T> put(final String uri, final ByteBufMono data, final Class<T> clazz) {
    return this.request(HttpMethod.PUT, uri, data, clazz);
  }

  public ResponseReceiver<?> patch(final String uri, final ByteBufMono data) {
    return this.request(HttpMethod.PATCH, uri, data);
  }

  public <T> Mono<T> patch(final String uri, final ByteBufMono data, final Class<T> clazz) {
    return this.request(HttpMethod.PATCH, uri, data, clazz);
  }

  public ResponseReceiver<?> delete(final String uri) {
    return this.request(HttpMethod.DELETE, uri);
  }

  public <T> Mono<T> delete(final String uri, final Class<T> clazz) {
    return this.request(HttpMethod.DELETE, uri, clazz);
  }

  // =========================================
  // Internal
  // =========================================

  private <T> Mono<T> handleJson(
    final HttpMethod method,
    final String uri,
    final HttpClientResponse response,
    final ByteBufMono data,
    final Class<T> clazz
  ) {
    if (response.status() != HttpResponseStatus.OK) {
      return Mono.error(new HttpClientStatusException(method, uri, response, data));
    }

    return HttpUtils.getCharset(response.responseHeaders())
      .map(charset -> JsonUtils.readValue(this.objectMapper, data, clazz, charset))
      .orElseGet(() -> JsonUtils.readValue(this.objectMapper, data, clazz));
  }
}
