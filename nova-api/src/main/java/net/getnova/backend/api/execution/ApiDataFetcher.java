package net.getnova.backend.api.execution;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import net.getnova.backend.api.data.MutationData;
import net.getnova.backend.api.data.QueryData;

import java.util.Set;

@RequiredArgsConstructor
@EqualsAndHashCode
public class ApiDataFetcher implements DataFetcher<Object> {

    private final Set<QueryData> queries;
    private final Set<MutationData> mutations;

    @Override
    public Object get(final DataFetchingEnvironment environment) throws Exception {
        System.out.println(environment);

        return null;
    }
}
