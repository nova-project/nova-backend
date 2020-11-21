package net.getnova.framework.api.data;

import net.getnova.framework.api.ApiAuthenticator;

import java.util.function.Function;

public interface AuthenticatorSupplier extends Function<Class<? extends ApiAuthenticator>, ApiAuthenticator> {
}
