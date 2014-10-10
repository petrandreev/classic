package cz.muni.fi.xharting.classic.test.startup;

import java.io.Serializable;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;

@Name("golf")
@Scope(ScopeType.SESSION)
@Startup(depends = "delta")
public class Golf extends Superclass implements Serializable {

    private static final long serialVersionUID = -8340227267852624721L;

    @Inject
    void initialize(@Started Event<String> event) {
        verifyStartupOrder("delta");
        event.fire("golf");
    }
}
