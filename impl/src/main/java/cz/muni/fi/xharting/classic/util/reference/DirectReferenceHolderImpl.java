package cz.muni.fi.xharting.classic.util.reference;

import java.io.Serializable;

import org.apache.deltaspike.core.api.exclude.Exclude;

/**
 * Simple reference carrier. It is registered as bean using {@link DirectReferenceHolder}.
 * 
 * @author Jozef Hartinger
 * 
 * @param <T> type of reference
 */
@Exclude
public class DirectReferenceHolderImpl<T> implements Serializable {

    private static final long serialVersionUID = 8143843055252632981L;

    private final T reference;

    public DirectReferenceHolderImpl() {
        this.reference = null;
    }

    public DirectReferenceHolderImpl(T reference) {
        this.reference = reference;
    }

    public T getReference() {
        return reference;
    }
}
