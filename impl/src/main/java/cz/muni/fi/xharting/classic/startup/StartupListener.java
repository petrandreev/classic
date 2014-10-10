package cz.muni.fi.xharting.classic.startup;

import static cz.muni.fi.xharting.classic.util.StartupUtils.startup;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.jboss.seam.annotations.Startup;

import cz.muni.fi.xharting.classic.metadata.MetadataRegistry;

/**
 * Listens to the activation of the application and session scopes and eagerly instantiates components marked with
 * {@link Startup}.
 *
 * @author Jozef Hartinger
 * @author pan
 *
 */
@WebListener
public class StartupListener implements HttpSessionListener {

    @Inject
    private MetadataRegistry registry;

    @Inject
    private BeanManager manager;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        startup(registry, SessionScoped.class, manager);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        // noop
    }
}
