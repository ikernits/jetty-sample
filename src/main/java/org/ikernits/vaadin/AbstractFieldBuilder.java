package org.ikernits.vaadin;

import com.vaadin.ui.AbstractField;
import com.vaadin.data.Buffered.SourceException;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ReadOnlyStatusChangeListener;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator;
import com.vaadin.data.util.converter.Converter;
import java.util.Locale;

public class AbstractFieldBuilder<T extends AbstractField, B extends AbstractFieldBuilder<T, B>> extends AbstractComponentBuilder<T, B> {

    public AbstractFieldBuilder(T delegate) {
        super(delegate);
    }
    
    /**
     * @see com.vaadin.ui.AbstractField#setReadOnly
     */
    public B setReadOnly(boolean readOnly) {
        delegate.setReadOnly(readOnly);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractField#setValue
     */
    public B setValue(Object value) {
        delegate.setValue(value);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractField#addListener
     */
    public B addListener(ReadOnlyStatusChangeListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractField#addListener
     */
    public B addListener(ValueChangeListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractField#setTabIndex
     */
    public B setTabIndex(int tabIndex) {
        delegate.setTabIndex(tabIndex);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractField#setPropertyDataSource
     */
    public B setPropertyDataSource(Property propertyDataSource) {
        delegate.setPropertyDataSource(propertyDataSource);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractField#setInvalidCommitted
     */
    public B setInvalidCommitted(boolean invalidCommitted) {
        delegate.setInvalidCommitted(invalidCommitted);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractField#setBuffered
     */
    public B setBuffered(boolean buffered) {
        delegate.setBuffered(buffered);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractField#setConverter
     */
    public B setConverter(Class converter) {
        delegate.setConverter(converter);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractField#setConverter
     */
    public B setConverter(Converter converter) {
        delegate.setConverter(converter);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractField#setConvertedValue
     */
    public B setConvertedValue(Object convertedValue) {
        delegate.setConvertedValue(convertedValue);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractField#addValidator
     */
    public B addValidator(Validator validator) {
        delegate.addValidator(validator);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractField#setInvalidAllowed
     */
    public B setInvalidAllowed(boolean invalidAllowed) {
        delegate.setInvalidAllowed(invalidAllowed);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractField#addValueChangeListener
     */
    public B addValueChangeListener(ValueChangeListener valueChangeListener) {
        delegate.addValueChangeListener(valueChangeListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractField#addReadOnlyStatusChangeListener
     */
    public B addReadOnlyStatusChangeListener(ReadOnlyStatusChangeListener readOnlyStatusChangeListener) {
        delegate.addReadOnlyStatusChangeListener(readOnlyStatusChangeListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractField#setRequired
     */
    public B setRequired(boolean required) {
        delegate.setRequired(required);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractField#setRequiredError
     */
    public B setRequiredError(String requiredError) {
        delegate.setRequiredError(requiredError);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractField#setConversionError
     */
    public B setConversionError(String conversionError) {
        delegate.setConversionError(conversionError);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractField#setValidationVisible
     */
    public B setValidationVisible(boolean validationVisible) {
        delegate.setValidationVisible(validationVisible);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractField#setCurrentBufferedSourceException
     */
    public B setCurrentBufferedSourceException(SourceException currentBufferedSourceException) {
        delegate.setCurrentBufferedSourceException(currentBufferedSourceException);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractField#setLocale
     */
    public B setLocale(Locale locale) {
        delegate.setLocale(locale);
        return self;
    }
    
}
