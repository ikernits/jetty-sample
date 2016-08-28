package org.ikernits.vaadin;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Component;

@SuppressWarnings({"deprecation", "unused", "unchecked"})
public class FormLayoutBuilder<T extends FormLayout, B extends FormLayoutBuilder<T, B>> extends AbstractOrderedLayoutBuilder<T, B> {

    public FormLayoutBuilder(T delegate) {
        super(delegate);
    }
    
    /**
     * @see com.vaadin.ui.FormLayout#setExpandRatio
     */
    public B setExpandRatio(Component param1, float param2) {
        delegate.setExpandRatio(param1, param2);
        return self;
    }
    
}
