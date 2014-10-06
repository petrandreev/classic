package cz.muni.fi.xharting.classic.metadata;

import static org.jboss.solder.reflection.Reflections.setAccessible;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;

import org.apache.deltaspike.core.api.exclude.Exclude;
import org.jboss.seam.ScopeType;

import cz.muni.fi.xharting.classic.scope.stateless.StatelessScoped;
import cz.muni.fi.xharting.classic.util.Seam2Utils;

/**
 * Represents a Seam factory method.
 * 
 * @author Jozef Hartinger
 * 
 */
@Exclude
public class FactoryDescriptor extends AbstractFactoryDescriptor {

    private BeanDescriptor bean;
    private Method method;
    private Class<?> productType;

    public FactoryDescriptor(String name, ScopeType specifiedScope, boolean autoCreate, BeanDescriptor bean, Method method) {
        super(name, specifiedScope, autoCreate);
        this.bean = bean;
        this.method = method;
        this.productType = method.getReturnType();
        setAccessible(method);
    }

    public FactoryDescriptor(FactoryDescriptor original, BeanDescriptor bean) {
        this(original.getName(), original.getScope(), original.isAutoCreate(), bean, original.getMethod());
    }

    public BeanDescriptor getBean() {
        return bean;
    }

    public Method getMethod() {
        return method;
    }

    /**
     * Translates Seam 2 ScopeType to matching CDI scope. Default scope rules for Seam 2 factories are considered.
     */
    public Class<? extends Annotation> getCdiScope() {
        if (isVoid()) {
            return StatelessScoped.class;
        }
        if (getScope().equals(ScopeType.UNSPECIFIED)) {
            Class<? extends Annotation> hostScope = bean.getImplicitRole().getCdiScope();
            if (hostScope.equals(Dependent.class)) {
                return RequestScoped.class;
            }
            return hostScope;
        } else {
            return Seam2Utils.transformExplicitLegacyScopeToCdiScope(getScope());
        }
    }

    public Class<?> getProductType() {
        return productType;
    }

    public boolean isVoid() {
        return void.class.equals(productType);
    }

    @Override
    public String toString() {
        return "FactoryDescriptor [method=" + method + "]";
    }
}
