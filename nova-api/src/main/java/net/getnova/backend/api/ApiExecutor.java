package net.getnova.backend.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import net.getnova.backend.json.JsonUtils;

import java.util.Collections;
import java.util.Map;

final class ApiExecutor {

    private ApiExecutor() {
        throw new UnsupportedOperationException();
    }

    static JsonElement execute(final GraphQL graphQL, final JsonObject request) {
        final ExecutionInput input = createInput(
                JsonUtils.fromJson(request.get("query"), String.class),
                JsonUtils.fromJson(request.get("operationName"), String.class),
                JsonUtils.fromJson(request.get("variables"), Map.class)
        );
//        ((Map) ((List) ((Map) execute(graphQL, input).toSpecification().get("data")).get("users")).get(0)).values().forEach(value -> System.out.println(value.getClass()));
        return JsonUtils.toJson(execute(graphQL, input).toSpecification());
    }

    private static ExecutionInput createInput(final String query, final String operationName, final Map<String, Object> variables) {
        return ExecutionInput.newExecutionInput()
                .query(query == null ? "" : query)
                .operationName(operationName)
                .variables(variables == null ? Collections.emptyMap() : variables)
                .build();
    }

    private static ExecutionResult execute(final GraphQL graphQL, final ExecutionInput input) {
        return graphQL.execute(input);
    }
}
