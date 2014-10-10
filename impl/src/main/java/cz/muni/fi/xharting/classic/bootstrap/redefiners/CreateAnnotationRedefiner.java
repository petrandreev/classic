package cz.muni.fi.xharting.classic.bootstrap.redefiners;

import javax.annotation.PostConstruct;

import org.jboss.seam.annotations.Create;

import cz.muni.fi.xharting.classic.util.deltaspike.metadata.AbstractAnnotationRedefiner;
import cz.muni.fi.xharting.classic.util.deltaspike.metadata.RedefinitionContext;
import cz.muni.fi.xharting.classic.util.literal.PostConstructLiteral;

/**
 * Provides trivial syntactic transformation of {@link Create} to {@link PostConstruct}.
 *
 * @author Jozef Hartinger
 *
 */
public class CreateAnnotationRedefiner extends AbstractAnnotationRedefiner<Create> {

    @Override
    public void redefine(RedefinitionContext<Create> ctx) {
        replace(Create.class, PostConstructLiteral.INSTANCE, ctx.getAnnotatedElement(), ctx.getAnnotatedTypeBuilder());
    }
}
