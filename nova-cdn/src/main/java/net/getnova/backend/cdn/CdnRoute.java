package net.getnova.backend.cdn;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import lombok.RequiredArgsConstructor;
import net.getnova.backend.cdn.data.CdnFileResolver;
import net.getnova.backend.network.server.http.HttpMimeTypeUtils;
import net.getnova.backend.network.server.http.HttpUtils;
import net.getnova.backend.network.server.http.route.HttpRoute;
import org.reactivestreams.Publisher;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class CdnRoute implements HttpRoute {

  private static final DateTimeFormatter HTTP_DATE_TIME_FORMATTER = DateTimeFormatter.RFC_1123_DATE_TIME;
  private static final long HTTP_CACHE_SECONDS = Duration.ofDays(7).getSeconds();
  private final CdnFileResolver resolver;

  @Override
  public Publisher<Void> execute(final HttpServerRequest request, final HttpServerResponse response) {
    return HttpUtils.checkMethod(request, response, HttpMethod.GET, HttpMethod.POST)
      .orElseGet(() -> {
        final Optional<UUID> uuid = this.parseUuid(request.path());
        if (uuid.isEmpty()) {
          return HttpUtils.sendStatus(response, HttpResponseStatus.BAD_REQUEST, "BAD_UUID");
        }

        return request.method().equals(HttpMethod.GET)
          ? this.handleFile(request, response, uuid.get())
          : this.handleUpload(request, response, uuid.get());
      });
  }

  private Optional<UUID> parseUuid(final String uuid) {
    try {
      return Optional.of(UUID.fromString(uuid));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  private Publisher<Void> handleUpload(final HttpServerRequest request, final HttpServerResponse response, final UUID uuid) {
    final HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new CdnHttpRequest(request));

    return request.receiveContent()
      .doOnNext(decoder::offer)
      .thenEmpty(d -> {
        final InterfaceHttpData httpData = decoder.getBodyHttpData("uploadedfile");
        if (httpData.getHttpDataType().equals(InterfaceHttpData.HttpDataType.FileUpload)) {
          final FileUpload httpData1 = (FileUpload) httpData;
          try {
            System.out.println(new String(httpData1.get()));
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      })
      .thenEmpty(HttpUtils.sendStatus(response, HttpResponseStatus.OK));
  }

  private Publisher<Void> handleFile(final HttpServerRequest request, final HttpServerResponse response, final UUID uuid) {
    return this.resolver.resolve(uuid)
      .map(result -> {
        response.header(HttpHeaderNames.DATE, OffsetDateTime.now(ZoneOffset.UTC).format(HTTP_DATE_TIME_FORMATTER));
        response.header(HttpHeaderNames.CONTENT_DISPOSITION, "inline; " + HttpHeaderValues.FILENAME + "=\"" + result.getCdnFile().getName() + "\"");

        HttpMimeTypeUtils.getMediaType(result.getCdnFile().getName())
          .ifPresentOrElse(
            mediaType -> response.header(HttpHeaderNames.CONTENT_TYPE, mediaType.toString()),
            () -> response.header(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_OCTET_STREAM)
          );

        final File file = result.getFile();
        if (!file.exists()) {
          return HttpUtils.sendStatus(response, HttpResponseStatus.NO_CONTENT, false);
        }

        // working with seconds, because the http date time format does not has milliseconds
        final long lastModified = file.lastModified() / 1000;

        final boolean isModified = Optional.ofNullable(request.requestHeaders().get(HttpHeaderNames.IF_MODIFIED_SINCE))
          .filter(ifModifiedSince -> !ifModifiedSince.isEmpty())
          .map(ifModifiedSince -> lastModified == OffsetDateTime.parse(ifModifiedSince, HTTP_DATE_TIME_FORMATTER).toEpochSecond())
          .orElse(false);

        if (isModified) {
          return HttpUtils.sendStatus(response, HttpResponseStatus.NOT_MODIFIED, false);
        }

        response.header(HttpHeaderNames.CACHE_CONTROL, "private, max-age=" + HTTP_CACHE_SECONDS);
        response.header(HttpHeaderNames.LAST_MODIFIED, Instant.ofEpochSecond(lastModified).atOffset(ZoneOffset.UTC).format(HTTP_DATE_TIME_FORMATTER));

        return response.sendFile(file.toPath());
      })
      .orElseGet(() -> HttpUtils.sendStatus(response, HttpResponseStatus.NOT_FOUND));
  }
}
