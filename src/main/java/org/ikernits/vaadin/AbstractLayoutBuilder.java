package org.ikernits.vaadin;

import com.vaadin.ui.AbstractLayout;

@SuppressWarnings({"deprecation", "unused", "unchecked"})
public class AbstractLayoutBuilder<T extends AbstractLayout, B extends AbstractLayoutBuilder<T, B>> extends AbstractComponentContainerBuilder<T, B> {

    public AbstractLayoutBuilder(T delegate) {
        super(delegate);
    }
    
}
