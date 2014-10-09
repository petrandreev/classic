package cz.muni.fi.xharting.classic.bootstrap.redefiners;

import org.apache.deltaspike.core.util.metadata.builder.AnnotatedTypeBuilder;
import org.jboss.seam.annotations.web.RequestParameter;

import cz.muni.fi.xharting.classic.util.deltaspike.metadata.AbstractAnnotationRedefiner;
import cz.muni.fi.xharting.classic.util.deltaspike.metadata.RedefinitionContext;
import cz.muni.fi.xharting.classic.util.literal.InjectLiteral;
import cz.muni.fi.xharting.classic.util.literal.RequestParameterLiteral;

/**
 * Provides trivial syntactic transformation of {@link RequestParameter} to its counterpart in Classic.
 * 
 * @author Jozef Hartinger
 * 
 */
public class RequestParameterRedefiner extends AbstractAnnotationRedefiner<RequestParameter> {

    @Override
    public void redefine(RedefinitionContext<RequestParameter> ctx) {
        AnnotatedTypeBuilder<?> builder = ctx.getAnnotatedTypeBuilder();
        RequestParameter requestParameter = ctx.getAnnotatedElement().getAnnotation(RequestParameter.class);
        RequestParameterLiteral replacement = new RequestParameterLiteral(requestParameter.value());
        replace(RequestParameter.class, replacement, ctx.getAnnotatedElement(), builder);
        add(InjectLiteral.INSTANCE, ctx.getAnnotatedElement(), builder);
    }
}
