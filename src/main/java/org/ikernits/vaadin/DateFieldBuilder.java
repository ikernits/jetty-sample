package org.ikernits.vaadin;

import com.vaadin.ui.DateField;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.shared.ui.datefield.Resolution;
import java.util.Date;
import java.util.TimeZone;

@SuppressWarnings({"deprecation", "unused", "unchecked"})
public class DateFieldBuilder<T extends DateField, B extends DateFieldBuilder<T, B>> extends AbstractFieldBuilder<T, B> {

    public DateFieldBuilder(T delegate) {
        super(delegate);
    }
    
    /**
     * @see com.vaadin.ui.DateField#addBlurListener
     */
    public B addBlurListener(BlurListener blurListener) {
        delegate.addBlurListener(blurListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.DateField#addFocusListener
     */
    public B addFocusListener(FocusListener focusListener) {
        delegate.addFocusListener(focusListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.DateField#setRangeStart
     */
    public B setRangeStart(Date rangeStart) {
        delegate.setRangeStart(rangeStart);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.DateField#setDateOutOfRangeMessage
     */
    public B setDateOutOfRangeMessage(String dateOutOfRangeMessage) {
        delegate.setDateOutOfRangeMessage(dateOutOfRangeMessage);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.DateField#setRangeEnd
     */
    public B setRangeEnd(Date rangeEnd) {
        delegate.setRangeEnd(rangeEnd);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.DateField#setResolution
     */
    public B setResolution(Resolution resolution) {
        delegate.setResolution(resolution);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.DateField#setDateFormat
     */
    public B setDateFormat(String dateFormat) {
        delegate.setDateFormat(dateFormat);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.DateField#setLenient
     */
    public B setLenient(boolean lenient) {
        delegate.setLenient(lenient);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.DateField#setShowISOWeekNumbers
     */
    public B setShowISOWeekNumbers(boolean showISOWeekNumbers) {
        delegate.setShowISOWeekNumbers(showISOWeekNumbers);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.DateField#setParseErrorMessage
     */
    public B setParseErrorMessage(String parseErrorMessage) {
        delegate.setParseErrorMessage(parseErrorMessage);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.DateField#addListener
     */
    public B addListener(BlurListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.DateField#addListener
     */
    public B addListener(FocusListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.DateField#setTimeZone
     */
    public B setTimeZone(TimeZone timeZone) {
        delegate.setTimeZone(timeZone);
        return self;
    }
    
}
