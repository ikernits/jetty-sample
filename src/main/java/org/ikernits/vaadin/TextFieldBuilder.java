package org.ikernits.vaadin;

import com.vaadin.ui.TextField;

@SuppressWarnings({"deprecation", "unused", "unchecked"})
public class TextFieldBuilder<T extends TextField, B extends TextFieldBuilder<T, B>> extends AbstractTextFieldBuilder<T, B> {

    public TextFieldBuilder(T delegate) {
        super(delegate);
    }
    
}
