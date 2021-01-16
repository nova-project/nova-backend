package net.getnova.framework.network.server.http.route;

import io.netty.handler.codec.http.HttpResponseStatus;
import java.io.File;
import net.getnova.framework.network.server.http.HttpUtils;
import org.reactivestreams.Publisher;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

public class FileHttpRoute implements HttpRoute {

  private final File baseDir;

  public FileHttpRoute(final File baseDir) {
    this.baseDir = baseDir.getAbsoluteFile();
  }

  @Override
  public Publisher<Void> execute(final HttpServerRequest request, final HttpServerResponse response) {
    final File file = new File(this.baseDir, request.path());

    if (!file.getPath().startsWith(this.baseDir.getPath())
      || !file.exists() || file.isDirectory() || !file.canRead() || !file.canExecute()) {
      return HttpUtils.sendStatus(response, HttpResponseStatus.NOT_FOUND);
    }

    return response.sendFile(file.toPath());
  }
}
