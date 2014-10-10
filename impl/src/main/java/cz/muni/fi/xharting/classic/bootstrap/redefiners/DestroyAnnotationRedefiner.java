package cz.muni.fi.xharting.classic.bootstrap.redefiners;

import javax.annotation.PreDestroy;

import org.jboss.seam.annotations.Destroy;

import cz.muni.fi.xharting.classic.util.deltaspike.metadata.AbstractAnnotationRedefiner;
import cz.muni.fi.xharting.classic.util.deltaspike.metadata.RedefinitionContext;
import cz.muni.fi.xharting.classic.util.literal.PreDestroyLiteral;

/**
 * Provides trivial syntactic transformation of {@link Destroy} to {@link PreDestroy}.
 *
 * @author Jozef Hartinger
 *
 */
public class DestroyAnnotationRedefiner extends AbstractAnnotationRedefiner<Destroy> {

    @Override
    public void redefine(RedefinitionContext<Destroy> ctx) {
        replace(Destroy.class, PreDestroyLiteral.INSTANCE, ctx.getAnnotatedElement(), ctx.getAnnotatedTypeBuilder());
    }
}
