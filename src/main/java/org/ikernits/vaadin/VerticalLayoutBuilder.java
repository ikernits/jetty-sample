package org.ikernits.vaadin;

import com.vaadin.ui.VerticalLayout;

public class VerticalLayoutBuilder<T extends VerticalLayout, B extends VerticalLayoutBuilder<T, B>> extends AbstractOrderedLayoutBuilder<T, B> {

    public VerticalLayoutBuilder(T delegate) {
        super(delegate);
    }
    
}
