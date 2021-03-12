package net.getnova.framework.api.data;

import net.getnova.framework.api.data.request.ApiRequest;

public interface ApiParameter<T> {

  T parse(ApiRequest request);
}
