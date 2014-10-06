package cz.muni.fi.xharting.classic.util;

import java.lang.annotation.Annotation;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;

import org.apache.deltaspike.core.api.literal.ApplicationScopedLiteral;
import org.apache.deltaspike.core.api.literal.ConversationScopedLiteral;
import org.apache.deltaspike.core.api.literal.DefaultLiteral;
import org.apache.deltaspike.core.api.literal.DependentScopeLiteral;
import org.apache.deltaspike.core.api.literal.RequestScopedLiteral;
import org.apache.deltaspike.core.api.literal.SessionScopeLiteral;

import cz.muni.fi.xharting.classic.scope.page.PageScoped;
import cz.muni.fi.xharting.classic.scope.stateless.StatelessScoped;

public class ScopeUtils {

    public static final RequestScopedLiteral REQUEST_SCOPED_LITERAL_INSTANCE = new RequestScopedLiteral();

    public static final ConversationScopedLiteral CONVERSATION_SCOPED_LITERAL_INSTANCE = new ConversationScopedLiteral();

    public static final SessionScopeLiteral SESSION_SCOPED_LITERAL_INSTANCE = new SessionScopeLiteral();

    public static final ApplicationScopedLiteral APPLICATION_SCOPED_LITERAL_INSTANCE = new ApplicationScopedLiteral();

    public static final DependentScopeLiteral DEPENDENT_SCOPED_LITERAL_INSTANCE = new DependentScopeLiteral();

    private ScopeUtils() {
    }

    public static Annotation getScopeLiteral(Class<? extends Annotation> clazz) {
        if (RequestScoped.class.equals(clazz)) {
            return REQUEST_SCOPED_LITERAL_INSTANCE;
        }
        if (ConversationScoped.class.equals(clazz)) {
            return CONVERSATION_SCOPED_LITERAL_INSTANCE;
        }
        if (SessionScoped.class.equals(clazz)) {
            return SESSION_SCOPED_LITERAL_INSTANCE;
        }
        if (ApplicationScoped.class.equals(clazz)) {
            return APPLICATION_SCOPED_LITERAL_INSTANCE;
        }
        if (Dependent.class.equals(clazz)) {
            return DEPENDENT_SCOPED_LITERAL_INSTANCE;
        }
        if (StatelessScoped.class.equals(clazz)) {
            return StatelessScoped.StatelessScopedLiteral.INSTANCE;
        }
        if (PageScoped.class.equals(clazz)) {
            return PageScoped.PageScopedLiteral.INSTANCE;
        }
        throw new IllegalArgumentException("Unknown scope: " + clazz.getName());
    }
}
