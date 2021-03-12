package net.getnova.framework.api.rest;

import lombok.Data;
import net.getnova.framework.api.data.response.ApiError;

@Data
public class ErrorRestApiResponse {

  private final ApiError[] errors;
}
