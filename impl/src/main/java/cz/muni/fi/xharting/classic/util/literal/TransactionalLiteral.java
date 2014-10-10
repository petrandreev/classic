package cz.muni.fi.xharting.classic.util.literal;

import javax.enterprise.util.AnnotationLiteral;
import javax.transaction.Transactional;

/**
 * JTA 1.2 {@link Transactional} literal.
 *
 * @author pan
 *
 */
public class TransactionalLiteral extends AnnotationLiteral<Transactional> implements Transactional {

    private static final long serialVersionUID = 5335554279570697061L;

    public static final TransactionalLiteral INSTANCE = new TransactionalLiteral();

    private static final Class[] EMPTY_CLASS_ARRAY = {};

    private TxType value = TxType.REQUIRED;

    public TransactionalLiteral() {
    }

    public TransactionalLiteral(TxType value) {
        if (value != null) {
            this.value = value;
        }
    }

    @Override
    public TxType value() {
        return value;
    }

    @Override
    public Class[] rollbackOn() {
        return EMPTY_CLASS_ARRAY;
    }

    @Override
    public Class[] dontRollbackOn() {
        return EMPTY_CLASS_ARRAY;
    }
}
