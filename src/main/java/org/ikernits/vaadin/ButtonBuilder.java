package org.ikernits.vaadin;

import com.vaadin.ui.Button;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings({"deprecation", "unused", "unchecked"})
public class ButtonBuilder<T extends Button, B extends ButtonBuilder<T, B>> extends AbstractFocusableBuilder<T, B> {

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
     * @see com.vaadin.ui.Button#setDisableOnClick
     */
    public B setDisableOnClick(boolean disableOnClick) {
        delegate.setDisableOnClick(disableOnClick);
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
    
    /**
     * @see com.vaadin.ui.Button#setIcon
     */
    public B setIcon(Resource param1, String param2) {
        delegate.setIcon(param1, param2);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Button#addListener
     */
    public B addListener(ClickListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
}
