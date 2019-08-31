package it.mulders.quarkusmvc.runtime;

import io.quarkus.arc.InjectableContext;
import org.eclipse.krazo.cdi.RedirectScopeManager;

import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.CDI;
import javax.mvc.annotation.RedirectScoped;
import java.lang.annotation.Annotation;
import java.util.Collections;

/**
 * Bridge between ArC's scope and Krazo's implementation of the @Redirect scope.
 * Krazo doesn't provide a way to access <strong>all</strong> contextual instances in the @Redirect scope,
 * so operations that relate to that are simply no-ops.
 */
public class RedirectScopeContext implements InjectableContext {
    private transient RedirectScopeManager manager;

    private RedirectScopeManager getManager() {
        if (manager == null) {
            manager = CDI.current().select(RedirectScopeManager.class).get();
        }
        return manager;
    }

    @Override
    public void destroy() {
        // no-op
    }

    @Override
    public ContextState getState() {
        // no-op
        return Collections::emptyMap;
    }

    @Override
    public void destroy(Contextual<?> contextual) {
        getManager().destroy(contextual);
    }

    @Override
    public Class<? extends Annotation> getScope() {
        return RedirectScoped.class;
    }

    @Override
    public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
        return getManager().get(contextual, creationalContext);
    }

    @Override
    public <T> T get(Contextual<T> contextual) {
        return getManager().get(contextual);
    }

    @Override
    public boolean isActive() {
        return true;
    }
}
