package cz.muni.fi.xharting.classic.test.outjection;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("factory")
@Scope(ScopeType.APPLICATION)
public class FactoryBean implements Serializable {

    private static final long serialVersionUID = 2243923357688800219L;

    @Factory(scope = ScopeType.EVENT, value = "delta")
    public Message getDelta() {
        return new Message("factoryDelta");
    }

    @Factory(autoCreate = true, scope = ScopeType.SESSION, value = "echo")
    public Message getEcho() {
        return new Message("factoryEcho");
    }

    @Factory(autoCreate = true, scope = ScopeType.SESSION, value = "foxtrot")
    public Message getFoxtrot() {
        return new Message("factoryFoxtrot");
    }

    @Factory(autoCreate = true, scope = ScopeType.SESSION, value = "golf")
    public Message getGolf() {
        return new Message("factoryGolf");
    }
}
