package net.getnova.framework.api.parser;

import java.util.Optional;
import net.getnova.framework.api.data.ApiController;

public interface ApiControllerParser {

  Optional<ApiController> parse(Object object);
}
