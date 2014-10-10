package cz.muni.fi.xharting.classic.test.web;

import static cz.muni.fi.xharting.classic.test.util.Archives.createSeamWebApp;
import static org.junit.Assert.assertEquals;

import java.net.URL;

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
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(Arquillian.class)
@RunAsClient
@WarpTest
public class RequestParameterTest {

    @ArquillianResource
    private URL contextPath;

    @Drone
    private WebDriver browser;

    @Deployment
    public static WebArchive getDeployment() {
        return createSeamWebApp("test.war", InjectedBean.class, SimpleServlet.class);
    }

    @Test
    public void testRequestParameter() throws Exception {

        final String expectedName = "mississippi";

        final String expectedId = "12345";

        Warp
            .initiate(new Activity() {
                public void perform() {
                    browser.navigate().to(contextPath + "classic/test?name=" + expectedName + "&id=" + expectedId);
                }
            })
            .inspect(new Inspection() {
                private static final long serialVersionUID = 1L;

                @Inject
                private InjectedBean injectedBean;

                @AfterServlet
                public void afterServlet() {
                    assertEquals(expectedName, injectedBean.getName());
                    assertEquals(expectedId, injectedBean.getId());
                }
            });
    }
}
