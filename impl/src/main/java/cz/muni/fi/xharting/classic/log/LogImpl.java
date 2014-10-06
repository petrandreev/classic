package cz.muni.fi.xharting.classic.log;

import java.io.Serializable;
import java.util.logging.Level;

import org.jboss.seam.log.Log;
import org.jboss.solder.el.Expressions;
import org.slf4j.Logger;

/**
 * Log implementation that delegates calls to Solder logging
 * 
 * @author Jozef Hartinger
 * 
 */
public class LogImpl implements Log, Serializable {

    private static final long serialVersionUID = -5096988340912397556L;
    private Logger delegate;
    private Expressions expressions;

    public LogImpl(Logger delegate, Expressions expressions) {
        this.delegate = delegate;
        this.expressions = expressions;
    }

    public boolean isDebugEnabled() {
        return delegate.isDebugEnabled();
    }

    public boolean isErrorEnabled() {
        return delegate.isErrorEnabled();
    }

    public boolean isFatalEnabled() {
        return delegate.isErrorEnabled();
    }

    public boolean isInfoEnabled() {
        return delegate.isInfoEnabled();
    }

    public boolean isTraceEnabled() {
        return delegate.isTraceEnabled();
    }

    public boolean isWarnEnabled() {
        return delegate.isWarnEnabled();
    }

    @Override
    public void trace(Object object, Object... params) {
        delegate.trace(interpolate(object), params);
    }

    @Override
    public void trace(Object object, Throwable t, Object... params) {
        delegate.trace(interpolate(object), t,params);
    }

    @Override
    public void debug(Object object, Object... params) {
        delegate.debug(interpolate(object), params);
    }

    @Override
    public void debug(Object object, Throwable t, Object... params) {
        delegate.debug(interpolate(object), t, params);
    }

    @Override
    public void info(Object object, Object... params) {
        delegate.info(interpolate(object), params);
    }

    @Override
    public void info(Object object, Throwable t, Object... params) {
        delegate.info(interpolate(object), t, params);
    }

    @Override
    public void warn(Object object, Object... params) {
        delegate.warn(interpolate(object), params);
    }

    @Override
    public void warn(Object object, Throwable t, Object... params) {
        delegate.warn(interpolate(object),t, params);
    }

    @Override
    public void error(Object object, Object... params) {
        delegate.error(interpolate(object), params);
    }

    @Override
    public void error(Object object, Throwable t, Object... params) {
        delegate.error(interpolate(object),t, params);
    }

    @Override
    public void fatal(Object object, Object... params) {
        delegate.error(interpolate(object), params);
    }

    @Override
    public void fatal(Object object, Throwable t, Object... params) {
        delegate.error(interpolate(object), t, params);
    }

    /**
     * Converts Object to String. If the parameter is an instance of String containing the '#' character, EL expressions are
     * evaluated. Furthermore, parameter placeholders written in the old format (#0) are replaced by placeholders used by
     * MessageFormat ({0}).
     */
    protected String interpolate(Object object) {
        if (object instanceof String) {
            String message = (String) object;
            if (message.contains("#")) {
                message = expressions.evaluateValueExpression(message);
            }
            if (message.contains("#")) {
                // convert the format used in Seam 2 (#0) to the format used by MessageFormat
                message = message.replaceAll("#(\\d+)", "\\{$1\\}");
            }
            return message;
        }
        return object.toString();
    }

    @Override
    public String toString() {
        return "LogImpl [delegate=" + delegate + "]";
    }
}
