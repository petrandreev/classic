package cz.muni.fi.xharting.classic.test.startup;

import java.util.List;

import javax.inject.Inject;

public class Superclass {

    @Inject
    private StartupEventListener listener;

    protected void verifyStartupOrder(String... dependencies) {
        List<String> startedComponents = listener.getStartedComponents();
        if (startedComponents == null || startedComponents.isEmpty()) {
            throw new AssertionError("No components started: " + startedComponents);
        }
        for (String dependency : dependencies) {
            if (!listener.getStartedComponents().contains(dependency)) {
                throw new AssertionError("Dependency not started: " + dependency);
            }
        }
    }
}
