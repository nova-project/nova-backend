package net.getnova.backend.cdn;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.RequiredArgsConstructor;
import net.getnova.backend.cdn.data.CdnFileResolver;
import net.getnova.backend.network.server.http.HttpUtils;
import net.getnova.backend.network.server.http.route.HttpRoute;
import org.reactivestreams.Publisher;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.util.UUID;

@RequiredArgsConstructor
public class CdnRoute implements HttpRoute {

    private final CdnFileResolver resolver;

    @Override
    public Publisher<Void> execute(final HttpServerRequest request, final HttpServerResponse response) {
        final CdnFileResolver.Result result = this.resolver.resolve(UUID.fromString(request.path().substring(4)));

        if (result == null) return HttpUtils.sendStatus(response, HttpResponseStatus.NOT_FOUND);

        response.header(HttpHeaderNames.CONTENT_DISPOSITION,
                HttpHeaderValues.ATTACHMENT + "; " + HttpHeaderValues.FILENAME + "=\"" + result.getCdnFile().getName() + "\"");
        return response.sendFile(result.getFile().toPath());
    }
}
