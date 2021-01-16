package net.getnova.framework.api.data;

import java.util.function.Function;
import net.getnova.framework.api.ApiAuthenticator;

public interface AuthenticatorSupplier extends Function<Class<? extends ApiAuthenticator>, ApiAuthenticator> {

}
