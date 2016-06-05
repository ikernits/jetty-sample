package org.ikernits.vaadin;

import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;

@SuppressWarnings({"deprecation", "unused", "unchecked"})
public class AbstractOrderedLayoutBuilder<T extends AbstractOrderedLayout, B extends AbstractOrderedLayoutBuilder<T, B>> extends AbstractLayoutBuilder<T, B> {

    public AbstractOrderedLayoutBuilder(T delegate) {
        super(delegate);
    }
    
    /**
     * @see com.vaadin.ui.AbstractOrderedLayout#addComponent
     */
    public B addComponent(Component param1, int param2) {
        delegate.addComponent(param1, param2);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractOrderedLayout#addComponent
     */
    public B addComponent(Component component) {
        delegate.addComponent(component);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractOrderedLayout#addComponentAsFirst
     */
    public B addComponentAsFirst(Component componentAsFirst) {
        delegate.addComponentAsFirst(componentAsFirst);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractOrderedLayout#setComponentAlignment
     */
    public B setComponentAlignment(Component param1, Alignment param2) {
        delegate.setComponentAlignment(param1, param2);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractOrderedLayout#setSpacing
     */
    public B setSpacing(boolean spacing) {
        delegate.setSpacing(spacing);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractOrderedLayout#setExpandRatio
     */
    public B setExpandRatio(Component param1, float param2) {
        delegate.setExpandRatio(param1, param2);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractOrderedLayout#addLayoutClickListener
     */
    public B addLayoutClickListener(LayoutClickListener layoutClickListener) {
        delegate.addLayoutClickListener(layoutClickListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractOrderedLayout#addListener
     */
    public B addListener(LayoutClickListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractOrderedLayout#setMargin
     */
    public B setMargin(boolean margin) {
        delegate.setMargin(margin);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractOrderedLayout#setMargin
     */
    public B setMargin(MarginInfo margin) {
        delegate.setMargin(margin);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractOrderedLayout#setDefaultComponentAlignment
     */
    public B setDefaultComponentAlignment(Alignment defaultComponentAlignment) {
        delegate.setDefaultComponentAlignment(defaultComponentAlignment);
        return self;
    }
    
}
