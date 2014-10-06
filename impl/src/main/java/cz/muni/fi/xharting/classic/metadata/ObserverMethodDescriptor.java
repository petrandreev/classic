package cz.muni.fi.xharting.classic.metadata;

import static org.jboss.solder.reflection.Reflections.setAccessible;

import java.lang.reflect.Method;

import org.apache.deltaspike.core.api.exclude.Exclude;

/**
 * Represents a legacy observer method defined on a Seam component.
 * 
 * @author Jozef Hartinger
 * 
 */
@Exclude
public class ObserverMethodDescriptor extends AbstractObserverMethodDescriptor {

    private final BeanDescriptor bean;
    private final Method method;
    private final boolean autoCreate;

    public ObserverMethodDescriptor(String type, BeanDescriptor bean, Method method, boolean autoCreate) {
        super(type);
        this.bean = bean;
        this.method = method;
        this.autoCreate = autoCreate;
        setAccessible(method);
    }

    public ObserverMethodDescriptor(ObserverMethodDescriptor original, BeanDescriptor bean) {
        this(original.getType(), bean, original.getMethod(), original.isAutoCreate());
    }

    public BeanDescriptor getBean() {
        return bean;
    }

    public Method getMethod() {
        return method;
    }

    public boolean isAutoCreate() {
        return autoCreate;
    }

    @Override
    public String toString() {
        return "ObserverMethodDescriptor [method=" + method + "]";
    }
}
