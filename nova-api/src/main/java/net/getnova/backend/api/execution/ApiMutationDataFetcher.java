package net.getnova.backend.api.execution;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import net.getnova.backend.api.data.ArgumentData;
import net.getnova.backend.api.data.MutationData;

import java.lang.reflect.Method;

@RequiredArgsConstructor
@EqualsAndHashCode
public final class ApiMutationDataFetcher<T> implements DataFetcher<T> {

    private final MutationData<T> mutationData;

    @Override
    public T get(final DataFetchingEnvironment environment) throws Exception {
        final Object[] parameters = new Object[this.mutationData.getArguments().size()];
        int i = 0;
        for (final ArgumentData argument : this.mutationData.getArguments()) {
            parameters[i] = environment.getArgument(argument.getName());
            i++;
        }

        final Method method = this.mutationData.getMethod();
        method.setAccessible(true);
        final T value = this.mutationData.getType().cast(method.invoke(this.mutationData.getInstance(), parameters));
        method.setAccessible(false);
        return value;
    }
}
