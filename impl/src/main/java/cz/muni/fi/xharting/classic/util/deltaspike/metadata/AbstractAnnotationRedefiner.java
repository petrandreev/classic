package cz.muni.fi.xharting.classic.util.deltaspike.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.enterprise.inject.spi.AnnotatedParameter;

import org.apache.deltaspike.core.util.metadata.builder.AnnotatedTypeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAnnotationRedefiner<A extends Annotation> implements AnnotationRedefiner<A> {

    private Logger log = LoggerFactory.getLogger(getClass());

    public <T> AnnotatedTypeBuilder<T> add(Annotation annotation, AnnotatedElement annotatedElement, AnnotatedTypeBuilder<T> builder) {
        if (annotatedElement instanceof Field) {
            builder.addToField((Field) annotatedElement, annotation);
        } else if (annotatedElement instanceof Method) {
            builder.addToMethod((Method) annotatedElement, annotation);
        } else if (annotatedElement instanceof Constructor<?>) {
            builder.addToConstructor((Constructor)annotatedElement, annotation);
        } else if (annotatedElement instanceof Class) {
            builder.addToClass(annotation);
        } else if (annotatedElement instanceof AnnotatedParameter<?>) {
            builder.addToParameter((AnnotatedParameter)annotatedElement, annotation);
        }
        else {
            throw new IllegalArgumentException("Cannot add " + annotation + " - cannot operate on member " + annotatedElement);
        }
        return builder;
    }

    public <T> AnnotatedTypeBuilder<T> remove(Class<? extends Annotation> annotationType, AnnotatedElement annotatedElement, AnnotatedTypeBuilder<T> builder) {
        if (annotatedElement instanceof Field) {
            builder.removeFromField((Field) annotatedElement, annotationType);
        } else if (annotatedElement instanceof Method) {
            builder.removeFromMethod((Method) annotatedElement, annotationType);
        } else if (annotatedElement instanceof Constructor<?>) {
            builder.removeFromConstructor((Constructor) annotatedElement, annotationType);
        } else if (annotatedElement instanceof Class) {
            builder.removeFromClass(annotationType);
        } else if (annotatedElement instanceof AnnotatedParameter<?>) {
            builder.removeFromParameter((AnnotatedParameter) annotatedElement, annotationType);
        }
        else {
            throw new IllegalArgumentException("Cannot remove " + annotationType + " - cannot operate on member " + annotatedElement);
        }
        return builder;
    }
}
