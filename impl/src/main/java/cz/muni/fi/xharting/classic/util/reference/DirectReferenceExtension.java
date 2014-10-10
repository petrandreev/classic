package cz.muni.fi.xharting.classic.util.reference;

import static cz.muni.fi.xharting.classic.util.ScopeUtils.getScope;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessBean;
import javax.enterprise.inject.spi.WithAnnotations;

import org.apache.deltaspike.core.util.metadata.builder.AnnotatedTypeBuilder;
import org.jboss.solder.reflection.Synthetic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import cz.muni.fi.xharting.classic.util.Annotations;
import cz.muni.fi.xharting.classic.util.deltaspike.metadata.AbstractAnnotationRedefiner;
import cz.muni.fi.xharting.classic.util.deltaspike.metadata.ClassicAnnotatedTypeBuilder;
import cz.muni.fi.xharting.classic.util.deltaspike.metadata.RedefinitionContext;

/**
 * The extension scans for {@link DirectReference} occurrences on bean definitions (including producer methods and fields),
 * suppresses such beans by using the synthetic qualifier and register {@link DirectReferenceHolder} and
 * {@link DirectReferenceProducer} for each such bean.
 *
 * @author Jozef Hartinger
 *
 */
public class DirectReferenceExtension implements Extension {

    private static final Logger log = LoggerFactory.getLogger(DirectReferenceExtension.class);

    private BeanManager manager;
    private DirectReferenceRedefiner redefiner = new DirectReferenceRedefiner();

    private final SetMultimap<Synthetic, Annotation> qualifiers = HashMultimap.create();
    private final Map<Synthetic, Class<? extends Annotation>> scopes = new HashMap<Synthetic, Class<? extends Annotation>>();

    private final List<Bean<?>> beansToRegister = new LinkedList<Bean<?>>();

    void getManager(@Observes BeforeBeanDiscovery event, BeanManager manager) {
        this.manager = manager;
    }

    <T> void scanDirectReferenceDeclaringTypes(@Observes @WithAnnotations(DirectReference.class) ProcessAnnotatedType<T> event, BeanManager manager) {
        ClassicAnnotatedTypeBuilder<T> builder = createAnnotatedTypeBuilder(event.getAnnotatedType());
        builder.redefine(DirectReference.class, redefiner);
        event.setAnnotatedType(builder.create());
    }

    <T> void processDirectReferenceBeans(@Observes ProcessBean<T> event) {
        Bean<T> bean = event.getBean();
        Synthetic synthetic = Annotations.getAnnotation(bean.getQualifiers(), Synthetic.class);
        if (synthetic != null && DirectReferenceFactory.NAMESPACE.equals(synthetic.namespace())) {
            beansToRegister.addAll(DirectReferenceFactory.createDirectReferenceHolder(bean.getBeanClass(), bean.getTypes(), qualifiers.get(synthetic), null, synthetic,
                scopes.get(synthetic), manager, true));
        }
    }

    void registerHoldersAndProducers(@Observes AfterBeanDiscovery event) {
        for (Bean<?> bean : beansToRegister) {
            log.trace("adding: {} ({})", bean, bean.getClass().getName());
            event.addBean(bean);
        }
    }

    private <T> ClassicAnnotatedTypeBuilder<T> createAnnotatedTypeBuilder(AnnotatedType<T> annotatedType) {
        return (ClassicAnnotatedTypeBuilder<T>) new ClassicAnnotatedTypeBuilder<T>().readFromType(annotatedType);
    }

    public class DirectReferenceRedefiner extends AbstractAnnotationRedefiner<DirectReference> {

        public void redefine(RedefinitionContext<DirectReference> ctx) {
            AnnotatedTypeBuilder<?> builder = ctx.getAnnotatedTypeBuilder();
            // process scope
            Class<? extends Annotation> scope = getScope(ctx.getAnnotatedElement(), manager);
            if (scope == null) {
                throw new IllegalArgumentException(ctx.getAnnotatedElement() + " does not declare a normal scope.");
            }
            remove(scope, ctx.getAnnotatedElement(), builder);

            // process qualifiers
            Set<Annotation> qualifiers = Annotations.getQualifiers(ctx.getAnnotatedElement(), manager);
            for (Annotation qualifier : qualifiers) {
                remove(qualifier.annotationType(), ctx.getAnnotatedElement(), builder);
            }
            Synthetic synthetic = DirectReferenceFactory.syntheticProvider.get();
            add(synthetic, ctx.getAnnotatedElement(), builder);

            scopes.put(synthetic, scope);
            DirectReferenceExtension.this.qualifiers.putAll(synthetic, qualifiers);
        }
    }
}
