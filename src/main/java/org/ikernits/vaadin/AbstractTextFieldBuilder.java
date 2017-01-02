package org.ikernits.vaadin;

import com.vaadin.ui.AbstractTextField;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;

@SuppressWarnings({"deprecation", "unused", "unchecked"})
public class AbstractTextFieldBuilder<T extends AbstractTextField, B extends AbstractTextFieldBuilder<T, B>> extends AbstractFieldBuilder<T, B> {

    public AbstractTextFieldBuilder(T delegate) {
        super(delegate);
    }
    
    /**
     * @see com.vaadin.ui.AbstractTextField#setValue
     */
    public B setValue(String value) {
        delegate.setValue(value);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractTextField#addBlurListener
     */
    public B addBlurListener(BlurListener blurListener) {
        delegate.addBlurListener(blurListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractTextField#addFocusListener
     */
    public B addFocusListener(FocusListener focusListener) {
        delegate.addFocusListener(focusListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractTextField#setNullRepresentation
     */
    public B setNullRepresentation(String nullRepresentation) {
        delegate.setNullRepresentation(nullRepresentation);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractTextField#setNullSettingAllowed
     */
    public B setNullSettingAllowed(boolean nullSettingAllowed) {
        delegate.setNullSettingAllowed(nullSettingAllowed);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractTextField#setMaxLength
     */
    public B setMaxLength(int maxLength) {
        delegate.setMaxLength(maxLength);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractTextField#setColumns
     */
    public B setColumns(int columns) {
        delegate.setColumns(columns);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractTextField#setInputPrompt
     */
    public B setInputPrompt(String inputPrompt) {
        delegate.setInputPrompt(inputPrompt);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractTextField#setTextChangeEventMode
     */
    public B setTextChangeEventMode(TextChangeEventMode textChangeEventMode) {
        delegate.setTextChangeEventMode(textChangeEventMode);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractTextField#addTextChangeListener
     */
    public B addTextChangeListener(TextChangeListener textChangeListener) {
        delegate.addTextChangeListener(textChangeListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractTextField#setTextChangeTimeout
     */
    public B setTextChangeTimeout(int textChangeTimeout) {
        delegate.setTextChangeTimeout(textChangeTimeout);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractTextField#setSelectionRange
     */
    public B setSelectionRange(int param1, int param2) {
        delegate.setSelectionRange(param1, param2);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractTextField#setCursorPosition
     */
    public B setCursorPosition(int cursorPosition) {
        delegate.setCursorPosition(cursorPosition);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractTextField#addListener
     */
    public B addListener(TextChangeListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractTextField#addListener
     */
    public B addListener(BlurListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractTextField#addListener
     */
    public B addListener(FocusListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
}
