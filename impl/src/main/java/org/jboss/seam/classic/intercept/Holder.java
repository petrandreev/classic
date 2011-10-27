package org.jboss.seam.classic.intercept;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.util.AnnotationLiteral;

@Inherited
@Target({ TYPE, METHOD })
@Retention(RUNTIME)
public @interface Holder {

    @SuppressWarnings("all")
    public static class HolderLiteral extends AnnotationLiteral<Holder> implements Holder {
    }
}
