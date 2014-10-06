package cz.muni.fi.xharting.classic.util.servlet.http;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.deltaspike.core.api.common.DeltaSpike;

/**
 * Produces the replacement for the Solder <code>RequestParam</code> using CDI-style one.
 * 
 * @author pan
 * @see <a
 *      href="http://docs.jboss.org/weld/reference/latest/en-US/html/injection.html#_the_literal_injectionpoint_literal_object">4.11.
 *      The InjectionPoint object</a>
 */
public class HttpServletRequestParameterProducer {

    @Inject
    @DeltaSpike
    private HttpServletRequest request;

    // TODO: it only works in JSF environment
    @Produces
    @RequestParameter
    public String getParamValue(InjectionPoint injectionPoint) {
        String parameterName = injectionPoint.getAnnotated().getAnnotation(RequestParameter.class).value();
        if (parameterName == null || parameterName.isEmpty()) {
            parameterName = injectionPoint.getMember().getName();
        }
        return request.getParameter(parameterName);
    }
}
