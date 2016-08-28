package org.ikernits.vaadin;

import com.vaadin.ui.AbstractFocusable;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusListener;

@SuppressWarnings({"deprecation", "unused", "unchecked"})
public class AbstractFocusableBuilder<T extends AbstractFocusable, B extends AbstractFocusableBuilder<T, B>> extends AbstractComponentBuilder<T, B> {

    public AbstractFocusableBuilder(T delegate) {
        super(delegate);
    }
    
    /**
     * @see com.vaadin.ui.AbstractFocusable#addBlurListener
     */
    public B addBlurListener(BlurListener blurListener) {
        delegate.addBlurListener(blurListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractFocusable#addFocusListener
     */
    public B addFocusListener(FocusListener focusListener) {
        delegate.addFocusListener(focusListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractFocusable#setTabIndex
     */
    public B setTabIndex(int tabIndex) {
        delegate.setTabIndex(tabIndex);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractFocusable#addListener
     */
    public B addListener(BlurListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractFocusable#addListener
     */
    public B addListener(FocusListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
}
