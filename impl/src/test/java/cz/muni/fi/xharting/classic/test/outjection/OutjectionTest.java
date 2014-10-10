package cz.muni.fi.xharting.classic.test.outjection;

import static cz.muni.fi.xharting.classic.test.util.Archives.createSeamWebApp;
import static cz.muni.fi.xharting.classic.test.util.Dependencies.SELENIUM;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URL;

import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.seam.RequiredException;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.solder.el.Expressions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(Arquillian.class)
public class OutjectionTest {

    @Inject
    private InjectingBean injectingBean;
    @Inject
    private OutjectingBean outjectingBean;
    @Inject
    private Expressions expressions;

    @Deployment
    public static WebArchive getDeployment() {
        WebArchive war = createSeamWebApp("test.war").addPackage(OutjectionTest.class.getPackage());
        // since we mix in-container and client tests the WebDriver API must be available in the deployment
        war.addAsLibraries(SELENIUM);
        war.addAsWebResource("cz/muni/fi/xharting/classic/test/outjection/home.xhtml", "home.xhtml");
        war.addAsWebInfResource("cz/muni/fi/xharting/classic/test/outjection/faces-config.xml", "faces-config.xml");
        war.setWebXML("cz/muni/fi/xharting/classic/test/outjection/web.xml");
        return war;
    }

    @Test
    public void testExplicitScopeOutjection() {
        outjectingBean.ping();
        assertEquals("alpha", injectingBean.getAlpha().getValue());
        assertEquals("alpha", expressions.evaluateValueExpression("#{alpha.value}").toString());
    }

    @Test
    public void testImplicitScopeOutjection() {
        outjectingBean.ping();
        assertEquals("bravo", injectingBean.getBravo().getValue());
    }

    @Test
    public void testExplicitNameOutjection() {
        outjectingBean.ping();
        assertEquals("charlie", injectingBean.getCharlie().getValue());
    }

    @Test
    public void testOutjectedValueSelectedOverNonAutoCreateFactory() {
        outjectingBean.ping();
        assertEquals("delta", injectingBean.getDelta().getValue());
    }

    @Test
    public void testOutjectedValueSelectedOverAutoCreateFactory() {
        outjectingBean.ping();
        assertEquals("echo", injectingBean.getEcho().getValue());
    }

    @Test
    public void testAutoCreateFactorySelectedOverOutjectedValue() {
        outjectingBean.ping();
        assertEquals("foxtrot", injectingBean.getFoxtrot().getValue());
    }

    @Test
    public void testNullOutjection() {
        outjectingBean.ping();
        assertEquals("golf", injectingBean.getGolf().getValue());
        outjectingBean.setGolf(null);
        assertEquals("factoryGolf", injectingBean.getGolf().getValue());
        outjectingBean.setGolf(new Message("foobar"));
        assertEquals("foobar", injectingBean.getGolf().getValue());
    }

    @Test
    public void testRequiredValidation(
        @Named("outjectingBeanBrokenRequiredFieldNull") OutjectingBeanBrokenRequiredFieldNull brokenOutjectingBean) {
        try {
            brokenOutjectingBean.ping();
            fail("Expected exception not thrown");
        } catch (RequiredException e) {
            // expected
        }
    }

    @Test
    public void testVoidFactory() {
        assertEquals("hotel", injectingBean.getHotel().getValue());
    }

    @Test
    @RunAsClient
    public void testOutjectedValuesAccessibleFromJsf(@ArquillianResource URL contextPath, @Drone WebDriver browser) throws Exception {
        browser.get(contextPath + "home.jsf");
        String homepage = browser.getPageSource();
        assertTrue(verifyValue(homepage, "alpha", "alpha"));
        assertTrue(verifyValue(homepage, "bravo", "bravo"));
        assertTrue(verifyValue(homepage, "charlie", "charlie"));
        assertTrue(verifyValue(homepage, "delta", "delta"));
        assertTrue(verifyValue(homepage, "echo", "echo"));
        assertTrue(verifyValue(homepage, "foxtrot", "foxtrot"));
        assertTrue(verifyValue(homepage, "hotel", "hotel"));
    }

    private boolean verifyValue(String content, String key, String value) {
        return content.contains(key + ":" + value);
    }
}
