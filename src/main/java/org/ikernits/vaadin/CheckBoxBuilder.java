package org.ikernits.vaadin;

import com.vaadin.ui.CheckBox;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusListener;

@SuppressWarnings({"deprecation", "unused", "unchecked"})
public class CheckBoxBuilder<T extends CheckBox, B extends CheckBoxBuilder<T, B>> extends AbstractFieldBuilder<T, B> {

    public CheckBoxBuilder(T delegate) {
        super(delegate);
    }
    
    /**
     * @see com.vaadin.ui.CheckBox#addListener
     */
    public B addListener(FocusListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.CheckBox#addListener
     */
    public B addListener(BlurListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.CheckBox#addBlurListener
     */
    public B addBlurListener(BlurListener blurListener) {
        delegate.addBlurListener(blurListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.CheckBox#addFocusListener
     */
    public B addFocusListener(FocusListener focusListener) {
        delegate.addFocusListener(focusListener);
        return self;
    }
    
}
