package net.getnova.backend.api.parser;

import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.api.annotations.ApiParameter;
import net.getnova.backend.api.data.ApiParameterData;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
final class ApiParameterParser {

    private ApiParameterParser() {
        throw new UnsupportedOperationException();
    }

    static Set<ApiParameterData> parseParameters(final Class<?> clazz, final Method method) {
        final Set<ApiParameterData> parameterData = new LinkedHashSet<>();
        for (final Parameter parameter : method.getParameters()) {

            final ApiParameterData currentParameterData = parseParameter(parameter);
            if (currentParameterData == null) {
                log.error("Parameter {}.{}.{} is missing annotation {}.",
                        clazz.getName(), method.getName(), parameter.getName(), ApiParameter.class.getName());
                return null;
            } else parameterData.add(currentParameterData);

        }
        return parameterData;
    }

    private static ApiParameterData parseParameter(final Parameter parameter) {
        if (!parameter.isAnnotationPresent(ApiParameter.class)) return null;
        final ApiParameter parameterAnnotation = parameter.getAnnotation(ApiParameter.class);

        return new ApiParameterData(parameterAnnotation.name(),
                parameterAnnotation.required(),
                String.join("\n", parameterAnnotation.description()),
                parameter.getType());
    }
}
