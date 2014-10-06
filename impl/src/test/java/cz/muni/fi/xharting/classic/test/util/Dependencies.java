package cz.muni.fi.xharting.classic.test.util;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

public interface Dependencies {

    String POM_XML = "pom.xml";

    Archive<?>[] DELTASPIKE_CORE = Maven.resolver().loadPomFromFile(POM_XML).resolve("org.apache.deltaspike.core:deltaspike-core-impl").withTransitivity().as(GenericArchive.class);
    
    Archive<?>[] DELTASPIKE_MODULE_SERVLET = Maven.resolver().loadPomFromFile(POM_XML).resolve("org.apache.deltaspike.modules:deltaspike-servlet-module-impl").withoutTransitivity().as(GenericArchive.class);

    Archive<?>[] REFLECTIONS = Maven.resolver().loadPomFromFile(POM_XML).resolve("org.reflections:reflections").withTransitivity().as(GenericArchive.class);

}
