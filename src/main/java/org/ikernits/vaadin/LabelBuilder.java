package org.ikernits.vaadin;

import com.vaadin.ui.Label;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.shared.ui.label.ContentMode;
import java.util.Locale;

@SuppressWarnings({"deprecation", "unused", "unchecked"})
public class LabelBuilder<T extends Label, B extends LabelBuilder<T, B>> extends AbstractComponentBuilder<T, B> {

    public LabelBuilder(T delegate) {
        super(delegate);
    }
    
    /**
     * @see com.vaadin.ui.Label#setValue
     */
    public B setValue(String value) {
        delegate.setValue(value);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Label#setLocale
     */
    public B setLocale(Locale locale) {
        delegate.setLocale(locale);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Label#setPropertyDataSource
     */
    public B setPropertyDataSource(Property propertyDataSource) {
        delegate.setPropertyDataSource(propertyDataSource);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Label#setConverter
     */
    public B setConverter(Converter converter) {
        delegate.setConverter(converter);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Label#addValueChangeListener
     */
    public B addValueChangeListener(ValueChangeListener valueChangeListener) {
        delegate.addValueChangeListener(valueChangeListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Label#setContentMode
     */
    public B setContentMode(ContentMode contentMode) {
        delegate.setContentMode(contentMode);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Label#addListener
     */
    public B addListener(ValueChangeListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
}
