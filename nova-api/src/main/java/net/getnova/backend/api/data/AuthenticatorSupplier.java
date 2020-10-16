package net.getnova.backend.api.data;

import net.getnova.backend.api.ApiAuthenticator;

import java.util.function.Function;

public interface AuthenticatorSupplier extends Function<Class<? extends ApiAuthenticator>, ApiAuthenticator> {
}
