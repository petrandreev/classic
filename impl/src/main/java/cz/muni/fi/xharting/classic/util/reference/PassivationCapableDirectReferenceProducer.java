package cz.muni.fi.xharting.classic.util.reference;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import javax.enterprise.inject.spi.BeanAttributes;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.PassivationCapable;

import org.jboss.weld.bean.BeanIdentifiers;
import org.jboss.weld.bean.StringBeanIdentifier;
import org.jboss.weld.serialization.spi.BeanIdentifier;

/**
 * Passivation capable version of {@link DirectReferenceProducer}.
 *
 * @author Jozef Hartinger
 */
public class PassivationCapableDirectReferenceProducer<T> extends DirectReferenceProducer<T> implements PassivationCapable {

    private final String id;

    public PassivationCapableDirectReferenceProducer(DirectReferenceHolder<T> directReferenceHolderProducer, Class<T> clazz, Set<Type> types, Set<Annotation> qualifiers,
        String name, BeanManager manager, boolean checkScope) {
        super(directReferenceHolderProducer, clazz, types, qualifiers, name, manager, checkScope);
        id = PassivationCapableDirectReferenceProducer.<T> createId(this, getType()).asString();
    }

    @Override
    public String getId() {
        return id;
    }

    private static <T> BeanIdentifier createId(BeanAttributes<T> attributes, Class<T> beanClass) {
        return new StringBeanIdentifier(BeanIdentifiers.forSyntheticBean(attributes, beanClass));
    }
}
