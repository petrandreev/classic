package cz.muni.fi.xharting.classic.util.reference;

import static cz.muni.fi.xharting.classic.util.reference.DirectReferenceUtils.getOrCreateConcreteClass;
import static org.jboss.solder.reflection.Reflections.cast;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Iterator;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.jboss.solder.reflection.Reflections;

import cz.muni.fi.xharting.classic.util.literal.DefaultLiteral;
import cz.muni.fi.xharting.classic.util.spi.AbstractBean;

/**
 * Bean implementation for {@link DirectReferenceHolderImpl}, which holds a direct reference to a bean instance. This bean has
 * the scope of the original bean, while the scope of the original bean has been altered to {@link Dependent}.
 * 
 * @author Jozef Hartinger
 * 
 * @param <T> type of reference
 */
public class DirectReferenceHolder<T> extends AbstractBean<DirectReferenceHolderImpl<T>> implements Bean<DirectReferenceHolderImpl<T>> {

    private final BeanManager manager;
    private final Annotation qualifier;
    private Bean<?> bean;

    public DirectReferenceHolder(Class<T> type, Class<? extends Annotation> scope, Annotation qualifier, BeanManager manager) {
        super(getOrCreateConcreteClass(Reflections.<Class<DirectReferenceHolderImpl<T>>> cast(DirectReferenceHolderImpl.class), type), scope, Collections
            .<Annotation> singleton(DefaultLiteral.INSTANCE));
        this.qualifier = qualifier;
        this.manager = manager;
        // the Weld complains `about ParameterizedType types
        enableNonDependentScopes();
    }

    /**
     * Removes all <code>ParameterizedType</code>s from the types set since we`ve already added the concrete subclass of the
     * holder to it
     */
    private void enableNonDependentScopes() {
        Iterator<Type> types = getTypes().iterator();
        while (types.hasNext()) {
            Type type = types.next();
            if (type instanceof ParameterizedType) {
                types.remove();
            }
        }
    }

    @Override
    public DirectReferenceHolderImpl<T> create(CreationalContext<DirectReferenceHolderImpl<T>> ctx) {
        T directReference = cast(manager.getReference(getTargetBean(), Object.class, ctx));
        return DirectReferenceUtils.<T> createDirectReferenceHolder(getType(), directReference);
    }

    @Override
    public void destroy(DirectReferenceHolderImpl<T> instance, CreationalContext<DirectReferenceHolderImpl<T>> creationalContext) {
        creationalContext.release();
    }

    public Annotation getQualifier() {
        return qualifier;
    }

    private Bean<?> getTargetBean() {
        if (bean == null) {
            bean = manager.resolve(manager.getBeans(Object.class, qualifier));
            if (bean == null) {
                throw new UnsatisfiedResolutionException("Unsatisfied dependency for bean indentified with " + qualifier);
            }
            if (!Dependent.class.equals(bean.getScope())) {
                throw new IllegalStateException("Target bean is not dependent: " + bean);
            }
        }
        return bean;
    }

    @Override
    public String toString() {
        return "DirectReferenceHolder [getTypes()=" + getTypes() + ", getQualifiers()=" + getQualifiers() + "]";
    }
}
