package net.getnova.backend.api.types;

import java.util.LinkedHashSet;
import java.util.Set;

public final class ApiTypeMappingHandler {

    public static ApiTypeMappingHandler INSTANCE = new ApiTypeMappingHandler();

    private final Set<ApiTypeMapping> types;

    private ApiTypeMappingHandler() {
        this.types = new LinkedHashSet<>();
        this.registerTypes();
    }

    private void registerTypes() {
        this.types.add(new ApiBigDecimalTypeMapping());
        this.types.add(new ApiBigIntegerTypeMapping());
        this.types.add(new ApiBooleanTypeMapping());
        this.types.add(new ApiByteTypeMapping());
        this.types.add(new ApiCharacterTypeMapping());
        this.types.add(new ApiFloatTypeMapping());
        this.types.add(new ApiIntegerTypeMapping());
        this.types.add(new ApiLongTypeMapping());
        this.types.add(new ApiShortTypeMapping());
        this.types.add(new ApiStringTypeMapping());
        this.types.add(new ApiUUIDTypeMapping());
        this.types.add(new ApiZonedDateTimeMapping());
    }

    public ApiTypeMapping getType(final Class<?> clazz) {
        for (final ApiTypeMapping type : this.types) {
            for (final Class<?> classType : type.getTypes()) {
                if (clazz.isAssignableFrom(classType)) {
                    return type;
                }
            }
        }
        return null;
    }
}
