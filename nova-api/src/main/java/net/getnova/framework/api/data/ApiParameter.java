package net.getnova.framework.api.data;

import net.getnova.framework.api.exception.ParameterApiException;

public interface ApiParameter<T> {

  T parse(ApiRequest request) throws ParameterApiException;
}
