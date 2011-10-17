package org.jboss.seam.classic.config;

import org.jboss.seam.ScopeType;

public class ConfiguredManagedBean {

    private String name;
    private final Boolean installed;
    private ScopeType scope;
    private Boolean startup;
    private String[] startupDependends;
    private Class<?> clazz;
    private String jndiName;
    private Integer precedence;
    private Boolean autoCreate;

    public ConfiguredManagedBean(String name, String installed, String scope, String startup, String startupDependends,
            Class<?> clazz, String jndiName, String precedence, String autoCreate) throws ClassNotFoundException {
        this.name = name;
        this.installed = (installed != null) ? Boolean.valueOf(installed) : null;
        this.scope = (scope != null) ? ScopeType.valueOf(scope.toUpperCase()) : null;
        this.startup = (startup != null) ? Boolean.valueOf(startup) : null;
        this.startupDependends = (startupDependends != null) ? startupDependends.split(" ") : null;
        this.clazz = clazz;
        this.jndiName = jndiName;
        this.precedence = (precedence != null) ? Integer.valueOf(precedence) : null;
        this.autoCreate = (autoCreate != null) ? Boolean.valueOf(autoCreate) : null;
    }

    public String getName() {
        return name;
    }

    public Boolean getInstalled() {
        return installed;
    }

    public ScopeType getScope() {
        return scope;
    }

    public Boolean getStartup() {
        return startup;
    }

    public String[] getStartupDependends() {
        return startupDependends;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getJndiName() {
        return jndiName;
    }

    public Integer getPrecedence() {
        return precedence;
    }

    public Boolean getAutoCreate() {
        return autoCreate;
    }
}