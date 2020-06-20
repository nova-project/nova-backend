package net.getnova.backend.api.type;

import graphql.Scalars;

import java.math.BigInteger;

public class ApiBigIntegerTypeMapping extends ApiTypeMapping {

    public ApiBigIntegerTypeMapping() {
        super(Scalars.GraphQLBigInteger, BigInteger.class);
    }
}
