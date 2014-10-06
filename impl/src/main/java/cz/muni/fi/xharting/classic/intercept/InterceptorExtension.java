package cz.muni.fi.xharting.classic.intercept;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.jboss.seam.annotations.intercept.Interceptor;
import org.jboss.seam.annotations.intercept.InterceptorType;
import org.jboss.seam.annotations.intercept.Interceptors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.muni.fi.xharting.classic.bootstrap.ScanningCompleteEvent;
import cz.muni.fi.xharting.classic.intercept.ClassicInterceptorBinding.ClassicInterceptorBindingLiteral;

/**
 * Replaces Legacy Seam 2 intercepors with CDI interceptors
 * 
 * @author Jozef Hartinger
 * 
 */
public class InterceptorExtension implements Extension {

    private Logger log = LoggerFactory.getLogger(InterceptorExtension.class);

    private Set<ClassicInterceptor<?>> transformedInterceptors = new HashSet<ClassicInterceptor<?>>();

    @SuppressWarnings("unchecked")
    void searchForInterceptorBindings(@Observes ScanningCompleteEvent event) {
        Set<Class<?>> possibleInterceptorBindings = event.getScanner().getTypesAnnotatedWith(Interceptors.class);

        for (Class<?> clazz : possibleInterceptorBindings) {
            if (clazz.isAnnotation()) {
                registerInterceptorBinding((Class<? extends Annotation>) clazz, event); // this is checked above
            } else {
                log.warn("@Interceptors is a meta annotation. Should only be applied on annotation but is on {}.", clazz);
            }
        }
    }

    private void registerInterceptorBinding(Class<? extends Annotation> annotationType, ScanningCompleteEvent event) {
        if (!annotationType.isAnnotationPresent(Interceptors.class)) {
            // should never happen
            throw new IllegalStateException("Legacy interceptor binding not annotated with @Interceptors " + annotationType);
        }

        Class<?>[] interceptorClasses = annotationType.getAnnotation(Interceptors.class).value();
        if (interceptorClasses.length == 0) {
            throw new IllegalArgumentException("Must specify at least one interceptor binding.");
        }
        if (interceptorClasses.length > 1) {
            throw new IllegalArgumentException(
                    "Legacy interceptor bindings that bind to multiple interceptors are not supported. Sorry");
        }
        if (interceptorClasses.length == 1) {
            Annotation binding = new ClassicInterceptorBindingLiteral(interceptorClasses[0]);
            log.info("Registering interceptor binding {} for {}.", annotationType, interceptorClasses[0]);
            event.addInterceptorBinding(annotationType, binding);
        }
    }

    /**
     * Registers ClassicInterceptorBinding as a CDI binding
     */
    void registerInterceptorBindings(@Observes BeforeBeanDiscovery event) {
        event.addInterceptorBinding(ClassicInterceptorBinding.class);
    }

    /**
     * Scan for legacy interceptors.
     */
    <T> void registerInterceptors(@Observes ProcessAnnotatedType<T> event, BeanManager manager) {
        if (event.getAnnotatedType().isAnnotationPresent(Interceptor.class)) {
            AnnotatedType<T> type = event.getAnnotatedType();

            validateInterceptorAnnotation(type.getAnnotation(Interceptor.class), type.getJavaClass());

            ClassicInterceptor<T> interceptor = null;
            if (Serializable.class.isAssignableFrom(type.getJavaClass())) {
                interceptor = new PassivationCapableClassicInterceptor<T>(type, manager);
            } else {
                interceptor = new ClassicInterceptor<T>(event.getAnnotatedType(), manager);
            }
            transformedInterceptors.add(interceptor);
            event.veto();
        }
    }

    /**
     * Issues warning logs for unsupported interceptor configuration.
     */
    protected void validateInterceptorAnnotation(Interceptor interceptor, Class<?> interceptorClass) {
        if (interceptor.within().length != 0 || interceptor.around().length != 0) {
            log.warn("Relative interceptor ordering not supported. Ignoring for {}.", interceptorClass);
        }
        if (interceptor.stateless()) {
            log.warn("Stateless interceptors not supported. Ignoring for {}.", interceptorClass);
        }
        if (interceptor.type() == InterceptorType.CLIENT) {
            log.warn("Client interceptors not supported. Ignoring for {}.", interceptorClass);
        }
    }

    /**
     * Register interceptor replacements
     */
    void registerInterceptors(@Observes AfterBeanDiscovery event) {
        log.debug("Registering {} interceptors.", transformedInterceptors.size());
        for (ClassicInterceptor<?> interceptor : transformedInterceptors) {
            event.addBean(interceptor);
        }
    }

    void cleanup(@Observes AfterDeploymentValidation event) {
        transformedInterceptors.clear();
    }
}
