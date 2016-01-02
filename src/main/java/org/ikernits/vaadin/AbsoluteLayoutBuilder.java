package org.ikernits.vaadin;

import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.AbsoluteLayout.ComponentPosition;
import com.vaadin.ui.Component;

public class AbsoluteLayoutBuilder<T extends AbsoluteLayout, B extends AbsoluteLayoutBuilder<T, B>> extends AbstractLayoutBuilder<T, B> {

    public AbsoluteLayoutBuilder(T delegate) {
        super(delegate);
    }
    
    /**
     * @see com.vaadin.ui.AbsoluteLayout#addComponent
     */
    public B addComponent(Component param1, String param2) {
        delegate.addComponent(param1, param2);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbsoluteLayout#addComponent
     */
    public B addComponent(Component component) {
        delegate.addComponent(component);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbsoluteLayout#addLayoutClickListener
     */
    public B addLayoutClickListener(LayoutClickListener layoutClickListener) {
        delegate.addLayoutClickListener(layoutClickListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbsoluteLayout#addListener
     */
    public B addListener(LayoutClickListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbsoluteLayout#setPosition
     */
    public B setPosition(Component param1, ComponentPosition param2) {
        delegate.setPosition(param1, param2);
        return self;
    }
    
}
