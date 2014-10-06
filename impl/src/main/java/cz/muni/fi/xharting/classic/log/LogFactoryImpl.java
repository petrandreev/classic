package cz.muni.fi.xharting.classic.log;

import static cz.muni.fi.xharting.classic.util.Annotations.getAnnotation;
import static org.jboss.solder.reflection.Reflections.getRawType;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.jboss.solder.el.Expressions;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class LogFactoryImpl extends Logging {

    @Inject
    private Expressions expressions;
    
    @Override
    protected Log internalGetLog(String category) {
        return new LogImpl(LoggerFactory.getLogger(category), expressions);
    }

    @Override
    protected Log internalGetLog(Class<?> clazz) {
        return new LogImpl(LoggerFactory.getLogger(clazz), expressions);
    }

    @Produces
    @org.jboss.seam.annotations.Logger
    public Log getLog(InjectionPoint ip)
    {
        org.jboss.seam.annotations.Logger annotation = getAnnotation(ip.getQualifiers(), org.jboss.seam.annotations.Logger.class);
        if (annotation == null || "".equals(annotation.value()))
        {
            Class<?> clazz = getRawType(ip.getType());
            return getLog(clazz);
        }
        return getLog(annotation.value());
    }
}
