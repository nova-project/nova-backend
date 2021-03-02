package net.getnova.framework.api.processor.controller;

import java.lang.annotation.Annotation;
import javax.lang.model.element.TypeElement;

public interface ControllerProcessor<A extends Annotation> {

  void process(A annotation, TypeElement element);
}
