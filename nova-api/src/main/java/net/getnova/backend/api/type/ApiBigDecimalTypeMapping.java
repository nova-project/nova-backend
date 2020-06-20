package net.getnova.backend.api.type;

import graphql.Scalars;

import java.math.BigDecimal;

public class ApiBigDecimalTypeMapping extends ApiTypeMapping {

    public ApiBigDecimalTypeMapping() {
        super(Scalars.GraphQLBigDecimal, BigDecimal.class);
    }
}
