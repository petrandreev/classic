package cz.muni.fi.xharting.classic.util.deltaspike.metadata;

import static cz.muni.fi.xharting.classic.util.ClassicReflections.getField;
import static org.jboss.solder.reflection.Reflections.getFieldValue;
import static org.jboss.solder.reflection.Reflections.setAccessible;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import org.apache.deltaspike.core.util.metadata.builder.AnnotatedTypeBuilder;

public class ClassicAnnotatedTypeBuilder<X> extends AnnotatedTypeBuilder<X> {

    public static final String parameterMustNotBeNull = "%s parameter must not be null";

    private static Field constructorParameters = getField(AnnotatedTypeBuilder.class, "constructorParameters");

    private static Field methodParameters = getField(AnnotatedTypeBuilder.class, "methodParameters");

    private static Field constructors = getField(AnnotatedTypeBuilder.class, "constructors");

    private static Field methods = getField(AnnotatedTypeBuilder.class, "methods");

    private static Field fields = getField(AnnotatedTypeBuilder.class, "fields");

    static {
        setAccessible(constructorParameters);
        setAccessible(methodParameters);
        setAccessible(constructors);
        setAccessible(methods);
        setAccessible(fields);
    }

    /**
     * Redefine any annotations of the specified type. The redefinition callback will be invoked for any annotation on the type
     * definition or any of it's members.
     *
     * @param annotationType the type of the annotation for which to call the redefinition
     * @param redefinition the redefiniton callback
     * @throws IllegalArgumentException if the annotationType or redefinition is null
     */
    public <A extends Annotation> ClassicAnnotatedTypeBuilder<X> redefine(Class<A> annotationType, AnnotationRedefiner<A> redefinition) {
        if (annotationType == null) {
            throw new IllegalArgumentException(String.format(parameterMustNotBeNull, "annotationType"));
        }
        if (redefinition == null) {
            throw new IllegalArgumentException(String.format(parameterMustNotBeNull, "redefinition"));
        }
        redefineAnnotationBuilder(annotationType, redefinition, getJavaClass(), getJavaClass(), null);
        for (Field field : getFields()) {
            redefineAnnotationBuilder(annotationType, redefinition, field, field.getGenericType(), field.getName());
        }
        for (Method method : getMethods()) {
            redefineAnnotationBuilder(annotationType, redefinition, method, method.getGenericReturnType(), method.getName());
            Set<Integer> parameters = getMethodParameters(method);
            if (parameters != null) {
                for (Integer parameter : parameters) {
                    Parameter<?> p = Parameter.create(method, parameter);
                    redefineAnnotationBuilder(annotationType, redefinition, p, p.getBaseType(), null);
                }
            }
        }
        for (Constructor<?> constructor : getConstructors()) {
            redefineAnnotationBuilder(annotationType, redefinition, constructor, constructor.getDeclaringClass(), null);
            Set<Integer> parameters = getConstructorParameters(constructor);
            if (parameters != null) {
                for (Integer parameter : parameters) {
                    Parameter<?> p = Parameter.create(constructor, parameter);
                    redefineAnnotationBuilder(annotationType, redefinition, p, p.getBaseType(), null);
                }
            }
        }
        return this;
    }

    protected <A extends Annotation> void redefineAnnotationBuilder(Class<A> annotationType, AnnotationRedefiner<A> redefinition, AnnotatedElement annotated, Type baseType,
        String elementName) {
        if (annotated.isAnnotationPresent(annotationType)) {
            redefinition.redefine(new RedefinitionContext<A>(annotated, baseType, this, elementName));
        }
    }

    private Set<Integer> getConstructorParameters(Constructor<?> constructor) {
        Map positions = (Map) getFieldValue(constructorParameters, this, Map.class).get(constructor);
        if (positions != null) {
            positions.keySet();
        }
        return null;
    }

    private Set<Integer> getMethodParameters(Method method) {
        Map positions = (Map) getFieldValue(methodParameters, this, Map.class).get(method);
        if (positions != null) {
            positions.keySet();
        }
        return null;
    }

    private Set<Constructor<?>> getConstructors() {
        return getFieldValue(constructors, this, Map.class).keySet();
    }

    private Set<Method> getMethods() {
        return getFieldValue(methods, this, Map.class).keySet();
    }

    private Set<Field> getFields() {
        return getFieldValue(fields, this, Map.class).keySet();
    }
}
