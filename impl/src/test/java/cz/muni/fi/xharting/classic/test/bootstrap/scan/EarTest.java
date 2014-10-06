package cz.muni.fi.xharting.classic.test.bootstrap.scan;

import static cz.muni.fi.xharting.classic.test.util.Archives.addDependenciesToManifest;
import static cz.muni.fi.xharting.classic.test.util.Archives.createSeamJar;
import static cz.muni.fi.xharting.classic.test.util.Archives.createSeamWebApp;
import static cz.muni.fi.xharting.classic.test.util.Dependencies.DELTASPIKE_CORE;
import static cz.muni.fi.xharting.classic.test.util.Dependencies.REFLECTIONS;
import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.annotations.Name;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.muni.fi.xharting.classic.test.bootstrap.scan.subpackage.AlphaJet;

@RunWith(Arquillian.class)
public class EarTest extends ScanTest {

    private static final Logger log = LoggerFactory.getLogger(EarTest.class);

    @Deployment
    public static Archive<?> getDeployment() {

        EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "test.ear");
        addDependenciesToManifest(ear, "org.slf4j, org.dom4j, org.javassist, com.google.guava");
        ear.addAsLibrary(createSeamJar("alpha.jar", Alpha.class, ScanTest.class, EarTest.class).addPackage(
            AlphaJet.class.getPackage())); // EAR lib
        ear.addAsModule(createSeamJar("bravo.jar", Bravo.class, Foo.class, Bar.class)); // EAR module
        ear.addAsLibraries(DELTASPIKE_CORE, REFLECTIONS);
        ear.addAsLibrary(createSeamClassic());
        ear.setApplicationXML("cz/muni/fi/xharting/classic/test/bootstrap/scan/application.xml");
        ear.addAsManifestResource("META-INF/jboss-deployment-structure.xml", "jboss-deployment-structure.xml");

        WebArchive war = createSeamWebApp("test.war", false, false, Alpha.class);
        ear.addAsModule(war);
        //log.trace(ear.toString(true));
        return ear;
    }

    @Test
    public void testAnnotationScanning() {
        Set<Class<?>> classes = scanner.getTypesAnnotatedWith(Name.class);
        assertEquals(2, classes.size());
    }
}
