package cz.muni.fi.xharting.classic.test.startup;

import static cz.muni.fi.xharting.classic.test.util.Archives.createSeamWebApp;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.servlet.AfterServlet;
import org.jboss.seam.servlet.SeamListener;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(Arquillian.class)
@RunAsClient
@WarpTest
public class SessionScopeStartupTest {

    @ArquillianResource
    private URL contextPath;

    @Drone
    private WebDriver browser;

    @Deployment
    public static WebArchive getDeployment() {
        return createSeamWebApp("test.war", Alpha.class, Bravo.class, Charlie.class, Delta.class, Echo.class, Golf.class, Hotel.class,
            StartupEventListener.class, Superclass.class, SeamListener.class, SessionCreatorServlet.class);
    }

    @Test
    public void testSessionScopedStartupWithDependencies() {
        Warp
            .initiate(new Activity() {
                public void perform() {
                    browser.navigate().to(contextPath + "classic/test");
                }
            })
            .inspect(new Inspection() {
                private static final long serialVersionUID = 1L;

                @Inject
                private StartupEventListener listener;

                @AfterServlet
                public void afterServlet() {
                    List<String> startedComponents = listener.getStartedComponents();
                    for (String component : new String[] { "alpha", "bravo", "charlie/foxtrot", "delta", "echo", "golf", "hotel" }) {
                        assertTrue(component + " not started", startedComponents.contains(component));
                        startedComponents.remove(component);
                    }
                }
            });
    }
}
