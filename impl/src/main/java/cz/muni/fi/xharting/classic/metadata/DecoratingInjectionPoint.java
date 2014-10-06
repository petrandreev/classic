package cz.muni.fi.xharting.classic.metadata;

import static org.jboss.solder.reflection.Reflections.getFieldValue;
import static org.jboss.solder.reflection.Reflections.setFieldValue;

import java.lang.reflect.Field;

import javax.persistence.PersistenceContext;

import org.apache.deltaspike.core.api.exclude.Exclude;

/**
 * Represents an EE injected field that can be decorated, e.g. {@link PersistenceContext} field.
 * 
 * @author Jozef Hartinger
 * 
 * @param <T> type of field
 */
@Exclude
public class DecoratingInjectionPoint<T> {

    private Field field;

    public DecoratingInjectionPoint(Field field) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        this.field = field;
    }

    @SuppressWarnings("unchecked")
    public T get(Object target) {
        return (T) getFieldValue(field, target);
    }

    public void setValue(Object target, T value) {
        setFieldValue(field, target, value);
    }

    public Field getField() {
        return field;
    }
}
