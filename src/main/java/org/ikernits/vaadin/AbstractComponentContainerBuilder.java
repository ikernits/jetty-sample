package org.ikernits.vaadin;

import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents.ComponentAttachListener;
import com.vaadin.ui.HasComponents.ComponentDetachListener;

@SuppressWarnings({"deprecation", "unused", "unchecked"})
public class AbstractComponentContainerBuilder<T extends AbstractComponentContainer, B extends AbstractComponentContainerBuilder<T, B>> extends AbstractComponentBuilder<T, B> {

    public AbstractComponentContainerBuilder(T delegate) {
        super(delegate);
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponentContainer#setHeight
     */
    public B setHeight(float height, Unit unit) {
        delegate.setHeight(height, unit);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponentContainer#addComponentAttachListener
     */
    public B addComponentAttachListener(ComponentAttachListener componentAttachListener) {
        delegate.addComponentAttachListener(componentAttachListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponentContainer#addComponentDetachListener
     */
    public B addComponentDetachListener(ComponentDetachListener componentDetachListener) {
        delegate.addComponentDetachListener(componentDetachListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponentContainer#setWidth
     */
    public B setWidth(float width, Unit unit) {
        delegate.setWidth(width, unit);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponentContainer#addComponent
     */
    public B addComponent(Component component) {
        delegate.addComponent(component);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponentContainer#addListener
     */
    public B addListener(ComponentAttachListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponentContainer#addListener
     */
    public B addListener(ComponentDetachListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
}
