package cz.muni.fi.xharting.classic.factory;

import java.lang.reflect.Method;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.BeanAttributes;
import javax.enterprise.inject.spi.BeanManager;

import org.jboss.solder.reflection.HierarchyDiscovery;
import org.jboss.solder.reflection.Reflections;
import org.jboss.weld.bean.BeanIdentifiers;
import org.jboss.weld.bean.StringBeanIdentifier;
import org.jboss.weld.serialization.spi.BeanIdentifier;

import cz.muni.fi.xharting.classic.metadata.FactoryDescriptor;
import cz.muni.fi.xharting.classic.util.CdiUtils;

/**
 * Represents a Seam 2 factory method.
 *
 * @author Jozef Hartinger
 *
 */
public class LegacyFactory extends AbstractLegacyFactory<Object> {

    private Class<?> beanClass;
    private String hostName;
    private Class<?> hostType;
    private Method method;

    public LegacyFactory(FactoryDescriptor descriptor, BeanManager manager) {
        super(descriptor.getName(), descriptor.getCdiScope(), manager);
        this.method = descriptor.getMethod();
        this.hostName = descriptor.getBean().getImplicitRole().getName();
        this.hostType = descriptor.getBean().getJavaClass();
        this.beanClass = descriptor.getMethod().getReturnType();
        initType(descriptor);
        this.identifier = createId(descriptor, this);
    }

    private static BeanIdentifier createId(FactoryDescriptor descriptor, BeanAttributes<Object> attributes) {
        return new StringBeanIdentifier(BeanIdentifiers.forSyntheticBean(attributes, descriptor.getMethod().getReturnType()));
    }

    protected void initType(FactoryDescriptor descriptor) {
        addTypes(new HierarchyDiscovery(descriptor.getProductType()).getTypeClosure());
    }

    @Override
    public Object create(CreationalContext<Object> creationalContext) {
        CdiUtils.ManagedBeanInstance<?> host = CdiUtils.lookupBeanByName(hostName, hostType, getManager());
        try {
            return create(host);
        } finally {
            host.getCreationalContext().release();
        }
    }

    protected Object create(CdiUtils.ManagedBeanInstance<?> host) {
        return Reflections.invokeMethod(true, method, beanClass, host.getInstance());
    }

    @Override
    public Class<?> getBeanClass() {
        return beanClass;
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return "LegacyFactory [getMethod()=" + getMethod() + ", getScope()=" + getScope() + ", getName()=" + getName() + "]";
    }
}
