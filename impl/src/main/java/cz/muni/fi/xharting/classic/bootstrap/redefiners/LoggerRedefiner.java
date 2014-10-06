package cz.muni.fi.xharting.classic.bootstrap.redefiners;

import org.jboss.seam.annotations.Logger;

import cz.muni.fi.xharting.classic.util.deltaspike.metadata.AbstractAnnotationRedefiner;
import cz.muni.fi.xharting.classic.util.deltaspike.metadata.RedefinitionContext;
import cz.muni.fi.xharting.classic.util.literal.InjectLiteral;

public class LoggerRedefiner extends AbstractAnnotationRedefiner<Logger> {

    @Override
    public void redefine(RedefinitionContext<Logger> ctx) {
        // make this a CDI injection point
        add(InjectLiteral.INSTANCE, ctx.getAnnotatedElement(), ctx.getAnnotatedTypeBuilder());
    }
}
