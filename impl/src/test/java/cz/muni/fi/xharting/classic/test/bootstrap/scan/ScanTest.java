package cz.muni.fi.xharting.classic.test.bootstrap.scan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Namespace;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;

import cz.muni.fi.xharting.classic.bijection.BijectionInterceptor;
import cz.muni.fi.xharting.classic.bootstrap.scan.AbstractScanner;
import cz.muni.fi.xharting.classic.bootstrap.scan.JBoss7UrlType;
import cz.muni.fi.xharting.classic.bootstrap.scan.ReflectionsScanner;
import cz.muni.fi.xharting.classic.bootstrap.scan.Scanner;
import cz.muni.fi.xharting.classic.event.RaiseEventInterceptor;

public abstract class ScanTest {

    protected Scanner scanner;

    public static JavaArchive createSeamClassic() {
        return ShrinkWrap.create(JavaArchive.class, "seam-classic.jar")
            .addClasses(Scanner.class, AbstractScanner.class, ReflectionsScanner.class, JBoss7UrlType.class, Namespace.class)
            .addClasses(Name.class, BijectionInterceptor.class, RaiseEventInterceptor.class);
    }

    @Before
    public void scan() {
        if (scanner == null) {
            scanner = new ReflectionsScanner(ScanTest.class.getClassLoader());
        }
    }

    @Test
    public void testAnnotationScanning() {
        Set<Class<?>> classes = scanner.getTypesAnnotatedWith(Name.class);
        assertEquals(4, classes.size());
        assertTrue(classes.contains(Alpha.class));
        assertTrue(classes.contains(Bravo.class));
        assertTrue(classes.contains(Charlie.class));
        assertTrue(classes.contains(Delta.class));
    }

    @Test
    public void testMetaAnnotationScanning() {
        Set<Class<?>> classes = ((ReflectionsScanner) scanner).getNonAnnotationTypesAnnotatedWithMetaAnnotation(Bar.class);
        assertEquals(1, classes.size());
        assertTrue(classes.contains(Bravo.class));
    }

    @Test
    public void testPackageScanning() {
        Set<Class<?>> namespaces = scanner.getTypesAnnotatedWith(Namespace.class);
        assertEquals(1, namespaces.size());
        Class<?> namespace = namespaces.iterator().next();
        assertTrue(namespace.isAnnotationPresent(Namespace.class));
        assertEquals("http://example.com/test", namespace.getAnnotation(Namespace.class).value());
    }
}
