package cz.muni.fi.xharting.classic.test.bootstrap.scan;

import static cz.muni.fi.xharting.classic.test.util.Archives.createSeamJar;
import static cz.muni.fi.xharting.classic.test.util.Archives.createSeamWebApp;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.muni.fi.xharting.classic.test.bootstrap.scan.subpackage.AlphaJet;

@RunWith(Arquillian.class)
public class WarTest extends ScanTest {

    private static final Logger log = LoggerFactory.getLogger(WarTest.class);

    @Deployment
    public static Archive<?> getDeployment() {

        WebArchive war = createSeamWebApp("test.war", Alpha.class, ScanTest.class, WarTest.class).addPackage(AlphaJet.class.getPackage());
        war.addAsLibrary(createSeamJar("bravo.jar", Bravo.class, Foo.class, Bar.class));
        war.addAsLibrary(createSeamJar("charlie.jar", Charlie.class));
        war.addAsLibrary(createSeamJar("delta.jar", Delta.class));
        war.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        return war;
    }
}
