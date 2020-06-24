package net.getnova.backend.api.execution;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import net.getnova.backend.api.data.ArgumentData;
import net.getnova.backend.api.data.QueryData;

import java.lang.reflect.Method;

@RequiredArgsConstructor
@EqualsAndHashCode
public final class ApiQueryDataFetcher<T> implements DataFetcher<T> {

    private final QueryData<T> typeData;

    @Override
    public T get(final DataFetchingEnvironment environment) throws Exception {
        final Object[] parameters = new Object[this.typeData.getArguments().size()];
        int i = 0;
        for (final ArgumentData argument : this.typeData.getArguments()) {
            parameters[i] = environment.getArgument(argument.getName());
            i++;
        }

        final Method method = this.typeData.getMethod();
        method.setAccessible(true);
        final T value = this.typeData.getType().cast(method.invoke(this.typeData.getInstance(), parameters));
        method.setAccessible(false);
        return value;
    }
}
