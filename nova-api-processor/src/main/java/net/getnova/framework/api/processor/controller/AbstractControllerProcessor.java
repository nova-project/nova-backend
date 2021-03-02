package net.getnova.framework.api.processor.controller;

import java.lang.annotation.Annotation;
import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import lombok.RequiredArgsConstructor;
import net.getnova.framework.api.processor.endpoint.EndpointProcessor;

@RequiredArgsConstructor
public abstract class AbstractControllerProcessor<A extends Annotation> implements ControllerProcessor<A> {

  private final Map<String, EndpointProcessor> endpointProcessors;

  @Override
  public void process(final A annotation, final TypeElement element) {
    for (final ExecutableElement method : ElementFilter.methodsIn(element.getEnclosedElements())) {

      for (final AnnotationMirror annotationMirror : method.getAnnotationMirrors()) {
        final TypeElement endpointAnnotation = (TypeElement) annotationMirror.getAnnotationType().asElement();

        final EndpointProcessor endpointProcessor =
          this.endpointProcessors.get(endpointAnnotation.getQualifiedName().toString());

        if (endpointProcessor == null) {
          continue;
        }

        endpointProcessor.process(method);
      }
    }
  }

  public abstract String getPath(A annotation);
}
