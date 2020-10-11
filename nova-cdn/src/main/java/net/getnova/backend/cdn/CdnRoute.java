package net.getnova.backend.cdn;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.RequiredArgsConstructor;
import net.getnova.backend.cdn.data.CdnFileResolver;
import net.getnova.backend.network.server.http.HttpMimeTypeUtils;
import net.getnova.backend.network.server.http.HttpUtils;
import net.getnova.backend.network.server.http.route.HttpRoute;
import org.reactivestreams.Publisher;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.io.File;
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
    final String uuidString = request.path().substring(4);
    final UUID uuid;

    try {
      uuid = UUID.fromString(uuidString);
    } catch (Exception e) {
      return HttpUtils.sendStatus(response, HttpResponseStatus.BAD_REQUEST, "BAD_UUID");
    }

    final CdnFileResolver.Result result = this.resolver.resolve(uuid).orElse(null);

    if (result == null) {
      return HttpUtils.sendStatus(response, HttpResponseStatus.NOT_FOUND);
    }

    response.header(HttpHeaderNames.DATE, OffsetDateTime.now(ZoneOffset.UTC).format(HTTP_DATE_TIME_FORMATTER));
    response.header(HttpHeaderNames.CONTENT_DISPOSITION, "inline; " + HttpHeaderValues.FILENAME + "=\"" + result.getCdnFile().getName() + "\"");
    HttpMimeTypeUtils.getMediaType(result.getCdnFile().getName()).ifPresentOrElse(
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
  }
}
