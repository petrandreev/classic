package cz.muni.fi.xharting.classic.util.reference;

import java.io.Serializable;

import javax.enterprise.inject.Vetoed;

/**
 * Simple reference carrier. It is registered as bean using {@link DirectReferenceHolder}.
 * 
 * @author Jozef Hartinger
 * 
 * @param <T> type of reference
 */
@Vetoed
public class DirectReferenceHolderImpl<T> implements Serializable {

    private static final long serialVersionUID = 7589341795506176847L;

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
