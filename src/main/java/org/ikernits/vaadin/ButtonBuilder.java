package org.ikernits.vaadin;

import com.vaadin.ui.Button;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button.ClickListener;

public class ButtonBuilder<T extends Button, B extends ButtonBuilder<T, B>> extends AbstractComponentBuilder<T, B> {

    public ButtonBuilder(T delegate) {
        super(delegate);
    }
    
    /**
     * @see com.vaadin.ui.Button#addClickListener
     */
    public B addClickListener(ClickListener clickListener) {
        delegate.addClickListener(clickListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Button#addBlurListener
     */
    public B addBlurListener(BlurListener blurListener) {
        delegate.addBlurListener(blurListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Button#addFocusListener
     */
    public B addFocusListener(FocusListener focusListener) {
        delegate.addFocusListener(focusListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Button#addListener
     */
    public B addListener(ClickListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Button#addListener
     */
    public B addListener(FocusListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Button#addListener
     */
    public B addListener(BlurListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Button#setDisableOnClick
     */
    public B setDisableOnClick(boolean disableOnClick) {
        delegate.setDisableOnClick(disableOnClick);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Button#setIcon
     */
    public B setIcon(Resource param1, String param2) {
        delegate.setIcon(param1, param2);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Button#setTabIndex
     */
    public B setTabIndex(int tabIndex) {
        delegate.setTabIndex(tabIndex);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Button#setIconAlternateText
     */
    public B setIconAlternateText(String iconAlternateText) {
        delegate.setIconAlternateText(iconAlternateText);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Button#setHtmlContentAllowed
     */
    public B setHtmlContentAllowed(boolean htmlContentAllowed) {
        delegate.setHtmlContentAllowed(htmlContentAllowed);
        return self;
    }
    
}
