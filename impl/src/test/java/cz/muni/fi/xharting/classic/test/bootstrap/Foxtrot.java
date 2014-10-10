package cz.muni.fi.xharting.classic.test.bootstrap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;

/**
 * Native CDI bean for functionality comparison.
 *
 * @author pan
 *
 */
@RequestScoped
public class Foxtrot {

    private boolean initCalled = false;

    private static boolean destroyCalled = false;

    public Foxtrot() {
    }

    @PostConstruct
    public void init() {
        initCalled = true;
    }

    public boolean isInitCalled() {
        return initCalled;
    }

    public void ping() {
    }

    @PreDestroy
    public void destroy() {
        destroyCalled = true;
    }

    public static boolean isDestroyCalled() {
        return destroyCalled;
    }
}
