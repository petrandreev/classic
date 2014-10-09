package cz.muni.fi.xharting.classic.util.deltaspike.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.deltaspike.core.util.metadata.builder.AnnotatedTypeBuilder;
import org.jboss.solder.reflection.Reflections;

import cz.muni.fi.xharting.classic.util.deltaspike.metadata.Parameter.ConstructorParameter;
import cz.muni.fi.xharting.classic.util.deltaspike.metadata.Parameter.MethodParameter;

public abstract class AbstractAnnotationRedefiner<A extends Annotation> implements AnnotationRedefiner<A> {

    public <T> AnnotatedTypeBuilder<T> add(Annotation annotation, AnnotatedElement annotatedElement, AnnotatedTypeBuilder<T> builder) {
        if (annotatedElement instanceof Field) {
            builder.addToField((Field) annotatedElement, annotation);
        } else if (annotatedElement instanceof Method) {
            builder.addToMethod((Method) annotatedElement, annotation);
        } else if (annotatedElement instanceof Constructor<?>) {
            builder.addToConstructor(Reflections.<Constructor<T>> cast(annotatedElement), annotation);
        } else if (annotatedElement instanceof Class) {
            builder.addToClass(annotation);
        } else if (annotatedElement instanceof Parameter<?>) {
            if (annotatedElement instanceof MethodParameter<?>) {
                MethodParameter<?> parameter = Reflections.<MethodParameter<?>> cast(annotatedElement);
                builder.addToMethodParameter(parameter.getDeclaringMember(), parameter.getPosition(), annotation);
            } else if (annotatedElement instanceof ConstructorParameter<?>) {
                ConstructorParameter<T> parameter = Reflections.<ConstructorParameter<T>> cast(annotatedElement);
                builder.addToConstructorParameter(parameter.getDeclaringMember(), parameter.getPosition(), annotation);
            }
            else {
                throw new IllegalArgumentException("Cannot add " + annotation + " - cannot operate on parameter " + annotatedElement);
            }
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
            builder.removeFromConstructor(Reflections.<Constructor<T>> cast(annotatedElement), annotationType);
        } else if (annotatedElement instanceof Class) {
            builder.removeFromClass(annotationType);
        } else if (annotatedElement instanceof Parameter<?>) {
            if (annotatedElement instanceof MethodParameter<?>) {
                MethodParameter<?> parameter = Reflections.<MethodParameter<?>> cast(annotatedElement);
                builder.removeFromMethodParameter(parameter.getDeclaringMember(), parameter.getPosition(), annotationType);
            } else if (annotatedElement instanceof ConstructorParameter<?>) {
                ConstructorParameter<T> parameter = Reflections.<ConstructorParameter<T>> cast(annotatedElement);
                builder.removeFromConstructorParameter(parameter.getDeclaringMember(), parameter.getPosition(), annotationType);
            }
            else {
                throw new IllegalArgumentException("Cannot remove " + annotationType + " - cannot operate on parameter " + annotatedElement);
            }
        }
        else {
            throw new IllegalArgumentException("Cannot remove " + annotationType + " - cannot operate on member " + annotatedElement);
        }
        return builder;
    }

    public <T> AnnotatedTypeBuilder<T> replace(Class<? extends Annotation> sourceAnnotationType, Annotation targetAnnotation,
        AnnotatedElement annotatedElement, AnnotatedTypeBuilder<T> builder) {
        add(targetAnnotation, annotatedElement, builder);
        remove(sourceAnnotationType, annotatedElement, builder);
        return builder;
    }
}
