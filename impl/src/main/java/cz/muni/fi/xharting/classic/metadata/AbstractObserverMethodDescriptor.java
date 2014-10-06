package cz.muni.fi.xharting.classic.metadata;

import org.apache.deltaspike.core.api.exclude.Exclude;

@Exclude
public abstract class AbstractObserverMethodDescriptor {

    private String type;

    public AbstractObserverMethodDescriptor(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
