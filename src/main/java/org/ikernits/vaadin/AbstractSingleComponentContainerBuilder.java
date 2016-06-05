package org.ikernits.vaadin;

import com.vaadin.ui.AbstractSingleComponentContainer;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents.ComponentAttachListener;
import com.vaadin.ui.HasComponents.ComponentDetachListener;

@SuppressWarnings({"deprecation", "unused", "unchecked"})
public class AbstractSingleComponentContainerBuilder<T extends AbstractSingleComponentContainer, B extends AbstractSingleComponentContainerBuilder<T, B>> extends AbstractComponentBuilder<T, B> {

    public AbstractSingleComponentContainerBuilder(T delegate) {
        super(delegate);
    }
    
    /**
     * @see com.vaadin.ui.AbstractSingleComponentContainer#addComponentAttachListener
     */
    public B addComponentAttachListener(ComponentAttachListener componentAttachListener) {
        delegate.addComponentAttachListener(componentAttachListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractSingleComponentContainer#setHeight
     */
    public B setHeight(float height, Unit unit) {
        delegate.setHeight(height, unit);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractSingleComponentContainer#addComponentDetachListener
     */
    public B addComponentDetachListener(ComponentDetachListener componentDetachListener) {
        delegate.addComponentDetachListener(componentDetachListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractSingleComponentContainer#setContent
     */
    public B setContent(Component content) {
        delegate.setContent(content);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractSingleComponentContainer#setWidth
     */
    public B setWidth(float width, Unit unit) {
        delegate.setWidth(width, unit);
        return self;
    }
    
}
