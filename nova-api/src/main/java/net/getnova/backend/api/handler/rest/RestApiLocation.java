package net.getnova.backend.api.handler.rest;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObjectAggregator;
import lombok.EqualsAndHashCode;
import net.getnova.backend.api.data.ApiEndpointData;
import net.getnova.backend.api.data.ApiRequest;
import net.getnova.backend.api.data.ApiResponse;
import net.getnova.backend.api.executor.ApiExecutor;
import net.getnova.backend.codec.http.server.HttpLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@EqualsAndHashCode
public final class RestApiLocation extends HttpLocation<ApiRequest> {

  private final Map<String, ApiEndpointData> endpoints;

  public RestApiLocation(@NotNull final Map<String, ApiEndpointData> endpoints) {
    super(new HttpObjectAggregator(65536), new RestApiCodec());
    this.endpoints = endpoints;
  }

  @Override
  protected void channelRead0(@NotNull final ChannelHandlerContext ctx, @NotNull final ApiRequest msg) throws Exception {
    final ApiResponse apiResponse = ApiExecutor.execute(this.endpoints, msg);
    if (apiResponse != null) ctx.write(apiResponse);
  }
}
