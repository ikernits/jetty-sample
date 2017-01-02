package org.ikernits.vaadin;

import com.vaadin.ui.ProgressBar;

@SuppressWarnings({"deprecation", "unused", "unchecked"})
public class ProgressBarBuilder<T extends ProgressBar, B extends ProgressBarBuilder<T, B>> extends AbstractFieldBuilder<T, B> {

    public ProgressBarBuilder(T delegate) {
        super(delegate);
    }
    
    /**
     * @see com.vaadin.ui.ProgressBar#setValue
     */
    public B setValue(Float value) {
        delegate.setValue(value);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.ProgressBar#setIndeterminate
     */
    public B setIndeterminate(boolean indeterminate) {
        delegate.setIndeterminate(indeterminate);
        return self;
    }
    
}
