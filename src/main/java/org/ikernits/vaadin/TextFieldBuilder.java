package org.ikernits.vaadin;

import com.vaadin.ui.TextField;

public class TextFieldBuilder<T extends TextField, B extends TextFieldBuilder<T, B>> extends AbstractTextFieldBuilder<T, B> {

    public TextFieldBuilder(T delegate) {
        super(delegate);
    }
    
}
