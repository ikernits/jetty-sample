package org.ikernits.vaadin;

import com.vaadin.ui.TextArea;

@SuppressWarnings({"deprecation", "unused", "unchecked"})
public class TextAreaBuilder<T extends TextArea, B extends TextAreaBuilder<T, B>> extends AbstractTextFieldBuilder<T, B> {

    public TextAreaBuilder(T delegate) {
        super(delegate);
    }
    
    /**
     * @see com.vaadin.ui.TextArea#setWordwrap
     */
    public B setWordwrap(boolean wordwrap) {
        delegate.setWordwrap(wordwrap);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.TextArea#setRows
     */
    public B setRows(int rows) {
        delegate.setRows(rows);
        return self;
    }
    
}
