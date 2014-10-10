package cz.muni.fi.xharting.classic.test.startup;

import java.io.Serializable;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;

@Name("hotel")
@Scope(ScopeType.SESSION)
@Startup(depends = "golf")
public class Hotel extends Superclass implements Serializable {

    private static final long serialVersionUID = -7789391199519676096L;

    @Inject
    void initialize(@Started Event<String> event) {
        verifyStartupOrder("golf");
        event.fire("hotel");
    }
}
