package cz.muni.fi.xharting.classic.bootstrap.redefiners;

import javax.transaction.Transactional.TxType;

import org.apache.deltaspike.core.util.metadata.builder.AnnotatedTypeBuilder;
import org.jboss.seam.annotations.Transactional;

import cz.muni.fi.xharting.classic.util.deltaspike.metadata.AbstractAnnotationRedefiner;
import cz.muni.fi.xharting.classic.util.deltaspike.metadata.RedefinitionContext;
import cz.muni.fi.xharting.classic.util.literal.TransactionalLiteral;

/**
 * Provides a trivial syntactic transformation of Seam 2 {@link Transactional} to its counterpart in JTA 1.2
 * {@link javax.transaction.Transactional}.
 *
 * @author pan
 *
 */
public class TransactionalAnnotationRedefiner extends AbstractAnnotationRedefiner<Transactional> {

    @Override
    public void redefine(RedefinitionContext<Transactional> ctx) {
        AnnotatedTypeBuilder<?> builder = ctx.getAnnotatedTypeBuilder();
        Transactional transactional = ctx.getAnnotatedElement().getAnnotation(Transactional.class);
        // translate the TX attributes according to the propagation modes supported by Seam 2
        TxType txType = TxType.valueOf(transactional.value().name());
        TransactionalLiteral replacement = new TransactionalLiteral(txType);
        replace(Transactional.class, replacement, ctx.getAnnotatedElement(), builder);
    }
}
