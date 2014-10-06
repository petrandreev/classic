package cz.muni.fi.xharting.classic.metadata;

import org.apache.deltaspike.core.api.exclude.Exclude;

/**
 * Represents an observer method configured in the component descriptor file.
 * 
 * @author Jozef Hartinger
 * 
 */
@Exclude
public class ElObserverMethodDescriptor extends AbstractObserverMethodDescriptor {

    private final String methodExpression;

    public ElObserverMethodDescriptor(String type, String methodExpression) {
        super(type);
        this.methodExpression = methodExpression;
    }

    public String getMethodExpression() {
        return methodExpression;
    }

    @Override
    public String toString() {
        return "ElObserverMethodDescriptor mapping " + getType() + " to " + methodExpression;
    }

}
