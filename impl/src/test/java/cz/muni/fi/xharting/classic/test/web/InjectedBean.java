package cz.muni.fi.xharting.classic.test.web;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;

@Name("injectedBean")
@Scope(ScopeType.APPLICATION)
public class InjectedBean {

    @RequestParameter("id")
    private String id;

    @RequestParameter
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
