package net.getnova.framework.cdn;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CdnRoute /*implements HttpRouteHandler */ {
//
//  private static final DateTimeFormatter HTTP_DATE_TIME_FORMATTER = DateTimeFormatter.RFC_1123_DATE_TIME;
//  private static final long HTTP_CACHE_SECONDS = Duration.ofDays(7).getSeconds();
//  private final CdnFileResolver resolver;
//
//  @Override
//  public Publisher<Void> handle(final HttpRoute route, final HttpServerRequest request,
//    final HttpServerResponse response) throws Exception {
////    return HttpUtils.checkMethod(request, response, HttpMethod.GET, HttpMethod.POST)
////      .orElseGet(() -> {
////        final Optional<UUID> uuid = this.parseUuid(request.path());
////        if (uuid.isEmpty()) {
////          return HttpUtils.status(response, HttpResponseStatus.BAD_REQUEST, "BAD_UUID");
////        }
////
////        return request.method().equals(HttpMethod.GET)
////          ? this.handleFile(request, response, uuid.get())
////          : this.handleUpload(request, response, uuid.get());
////      });
//
//    return HttpUtils.status(response, HttpResponseStatus.NOT_IMPLEMENTED);
//  }
//
//  private Optional<UUID> parseUuid(final String uuid) {
//    try {
//      return Optional.of(UUID.fromString(uuid));
//    }
//    catch (Exception e) {
//      return Optional.empty();
//    }
//  }
//
//  private Publisher<Void> handleUpload(final HttpServerRequest request, final HttpServerResponse response,
//    final UUID uuid) {
//    final HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new CdnHttpRequest(request));
//
//    return request.receiveContent()
//      .doOnNext(decoder::offer)
//      .thenEmpty(d -> {
//        final InterfaceHttpData httpData = decoder.getBodyHttpData("uploadedfile");
//        if (httpData.getHttpDataType().equals(InterfaceHttpData.HttpDataType.FileUpload)) {
//          final FileUpload httpData1 = (FileUpload) httpData;
//          try {
//            System.out.println(new String(httpData1.get(), StandardCharsets.UTF_8));
//          }
//          catch (IOException e) {
//            e.printStackTrace();
//          }
//        }
//      })
//      .thenEmpty(HttpUtils.status(response, HttpResponseStatus.OK));
//  }
//
//  private Publisher<Void> handleFile(final HttpServerRequest request, final HttpServerResponse response,
//    final UUID uuid) {
//    return this.resolver.resolve(uuid)
//      .map(result -> {
//        response.header(HttpHeaderNames.DATE, OffsetDateTime.now(ZoneOffset.UTC).format(HTTP_DATE_TIME_FORMATTER));
//        response.header(HttpHeaderNames.CONTENT_DISPOSITION,
//          "inline; " + HttpHeaderValues.FILENAME + "=\"" + result.getCdnFile().getName() + "\"");
//
//        HttpMimeTypeUtils.getMediaTypes(result.getCdnFile().getName())
//          .ifPresentOrElse(
//            mediaType -> response.header(HttpHeaderNames.CONTENT_TYPE, mediaType),
//            () -> response.header(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_OCTET_STREAM)
//          );
//
//        final File file = result.getFile();
//        if (!file.exists()) {
//          return response.status(HttpResponseStatus.NO_CONTENT).send();
//        }
//
//        // working with seconds, because the http date time format does not has milliseconds
//        final long lastModified = file.lastModified() / 1000;
//
//        final boolean isModified = Optional
//          .ofNullable(request.requestHeaders().get(HttpHeaderNames.IF_MODIFIED_SINCE))
//          .filter(ifModifiedSince -> !ifModifiedSince.isEmpty())
//          .map(ifModifiedSince -> lastModified == OffsetDateTime.parse(ifModifiedSince, HTTP_DATE_TIME_FORMATTER)
//            .toEpochSecond())
//          .orElse(false);
//
//        if (isModified) {
//          return response.status(HttpResponseStatus.NOT_MODIFIED).send();
//        }
//
//        response.header(HttpHeaderNames.CACHE_CONTROL, "private, max-age=" + HTTP_CACHE_SECONDS);
//        response.header(HttpHeaderNames.LAST_MODIFIED,
//          Instant.ofEpochSecond(lastModified).atOffset(ZoneOffset.UTC).format(HTTP_DATE_TIME_FORMATTER));
//
//        return response.sendFile(file.toPath());
//      })
//      .orElseGet(() -> HttpUtils.status(response, HttpResponseStatus.NOT_FOUND));
//  }
}
