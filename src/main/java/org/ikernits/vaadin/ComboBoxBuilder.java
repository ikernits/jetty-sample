package org.ikernits.vaadin;

import com.vaadin.ui.ComboBox;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox.ItemStyleGenerator;

@SuppressWarnings({"deprecation", "unused", "unchecked"})
public class ComboBoxBuilder<T extends ComboBox, B extends ComboBoxBuilder<T, B>> extends AbstractSelectBuilder<T, B> {

    public ComboBoxBuilder(T delegate) {
        super(delegate);
    }
    
    /**
     * @see com.vaadin.ui.ComboBox#setInputPrompt
     */
    public B setInputPrompt(String inputPrompt) {
        delegate.setInputPrompt(inputPrompt);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.ComboBox#setPageLength
     */
    public B setPageLength(int pageLength) {
        delegate.setPageLength(pageLength);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.ComboBox#addListener
     */
    public B addListener(FocusListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.ComboBox#addListener
     */
    public B addListener(BlurListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.ComboBox#addBlurListener
     */
    public B addBlurListener(BlurListener blurListener) {
        delegate.addBlurListener(blurListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.ComboBox#addFocusListener
     */
    public B addFocusListener(FocusListener focusListener) {
        delegate.addFocusListener(focusListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.ComboBox#setMultiSelect
     */
    public B setMultiSelect(boolean multiSelect) {
        delegate.setMultiSelect(multiSelect);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.ComboBox#setTextInputAllowed
     */
    public B setTextInputAllowed(boolean textInputAllowed) {
        delegate.setTextInputAllowed(textInputAllowed);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.ComboBox#setFilteringMode
     */
    public B setFilteringMode(FilteringMode filteringMode) {
        delegate.setFilteringMode(filteringMode);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.ComboBox#setScrollToSelectedItem
     */
    public B setScrollToSelectedItem(boolean scrollToSelectedItem) {
        delegate.setScrollToSelectedItem(scrollToSelectedItem);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.ComboBox#setItemStyleGenerator
     */
    public B setItemStyleGenerator(ItemStyleGenerator itemStyleGenerator) {
        delegate.setItemStyleGenerator(itemStyleGenerator);
        return self;
    }
    
}
