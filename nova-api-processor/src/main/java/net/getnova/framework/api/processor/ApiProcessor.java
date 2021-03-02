package net.getnova.framework.api.processor;

import com.google.auto.service.AutoService;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import net.getnova.framework.api.annotation.GetEndpoint;
import net.getnova.framework.api.processor.controller.ControllerProcessor;
import net.getnova.framework.api.processor.controller.RestApiControllerProcessor;
import net.getnova.framework.api.processor.endpoint.EndpointProcessor;
import net.getnova.framework.api.processor.endpoint.GetEndpointProcessor;
import net.getnova.framework.api.rest.annotation.RestApiController;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class ApiProcessor extends AbstractProcessor {

  private Map<String, EndpointProcessor> endpointProcessors;
  private Map<String, ControllerProcessor> controllerProcessors;

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return Set.of(
      GetEndpoint.class.getName(),
      RestApiController.class.getName()
    );
  }

  @Override
  public synchronized void init(final ProcessingEnvironment processingEnv) {
    super.init(processingEnv);

    this.endpointProcessors = Map.of(
      GetEndpoint.class.getName(), new GetEndpointProcessor()
    );

    this.controllerProcessors = Map.of(
      RestApiController.class.getName(), new RestApiControllerProcessor(this.endpointProcessors)
    );
  }

  @Override
  public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
    if (annotations.isEmpty()) {
      return false;
    }

    for (final TypeElement annotation : annotations) {
      final ControllerProcessor processor = this.controllerProcessors.get(annotation.getQualifiedName().toString());

      if (processor == null) {
        continue;
      }

//      for (final TypeElement element : ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(annotation))) {
//        processor.process(annotation, element);
//      }
    }

    return false;
  }
}
