package org.ikernits.vaadin;

import com.vaadin.ui.HorizontalLayout;

public class HorizontalLayoutBuilder<T extends HorizontalLayout, B extends HorizontalLayoutBuilder<T, B>> extends AbstractOrderedLayoutBuilder<T, B> {

    public HorizontalLayoutBuilder(T delegate) {
        super(delegate);
    }
    
}
