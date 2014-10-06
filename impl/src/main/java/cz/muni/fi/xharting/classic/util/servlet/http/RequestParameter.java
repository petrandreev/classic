package cz.muni.fi.xharting.classic.util.servlet.http;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

/**
 * Replaces the Solder <code>RequestParam</code>s by CDI-style ones.
 * 
 * @author pan
 * @see <a
 *      href="http://docs.jboss.org/weld/reference/latest/en-US/html/injection.html#_the_literal_injectionpoint_literal_object">4.11.
 *      The InjectionPoint object</a>
 */
@Qualifier
@Retention(RUNTIME)
@Target({ TYPE, METHOD, FIELD, PARAMETER })
@Documented
public @interface RequestParameter {
    @Nonbinding
    public String value() default "";
}
