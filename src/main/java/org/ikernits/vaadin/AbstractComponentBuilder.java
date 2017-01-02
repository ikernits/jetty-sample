package org.ikernits.vaadin;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.event.ContextClickEvent.ContextClickListener;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.HasComponents;
import java.util.Locale;

@SuppressWarnings({"deprecation", "unused", "unchecked"})
public class AbstractComponentBuilder<T extends AbstractComponent, B extends AbstractComponentBuilder<T, B>> extends ComponentBuilder<T, B> {

    public AbstractComponentBuilder(T delegate) {
        super(delegate);
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponent#setReadOnly
     */
    public B setReadOnly(boolean readOnly) {
        delegate.setReadOnly(readOnly);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponent#setParent
     */
    public B setParent(HasComponents parent) {
        delegate.setParent(parent);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponent#setIcon
     */
    public B setIcon(Resource icon) {
        delegate.setIcon(icon);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponent#setStyleName
     */
    public B setStyleName(String param1, boolean param2) {
        delegate.setStyleName(param1, param2);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponent#setStyleName
     */
    public B setStyleName(String styleName) {
        delegate.setStyleName(styleName);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponent#addStyleName
     */
    public B addStyleName(String styleName) {
        delegate.addStyleName(styleName);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponent#setPrimaryStyleName
     */
    public B setPrimaryStyleName(String primaryStyleName) {
        delegate.setPrimaryStyleName(primaryStyleName);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponent#setEnabled
     */
    public B setEnabled(boolean enabled) {
        delegate.setEnabled(enabled);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponent#setVisible
     */
    public B setVisible(boolean visible) {
        delegate.setVisible(visible);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponent#setCaption
     */
    public B setCaption(String caption) {
        delegate.setCaption(caption);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponent#setId
     */
    public B setId(String id) {
        delegate.setId(id);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponent#setDebugId
     */
    public B setDebugId(String debugId) {
        delegate.setDebugId(debugId);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponent#setCaptionAsHtml
     */
    public B setCaptionAsHtml(boolean captionAsHtml) {
        delegate.setCaptionAsHtml(captionAsHtml);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponent#setLocale
     */
    public B setLocale(Locale locale) {
        delegate.setLocale(locale);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponent#setImmediate
     */
    public B setImmediate(boolean immediate) {
        delegate.setImmediate(immediate);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponent#setDescription
     */
    public B setDescription(String description) {
        delegate.setDescription(description);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponent#setComponentError
     */
    public B setComponentError(ErrorMessage componentError) {
        delegate.setComponentError(componentError);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponent#setData
     */
    public B setData(Object data) {
        delegate.setData(data);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponent#setResponsive
     */
    public B setResponsive(boolean responsive) {
        delegate.setResponsive(responsive);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponent#addShortcutListener
     */
    public B addShortcutListener(ShortcutListener shortcutListener) {
        delegate.addShortcutListener(shortcutListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponent#addContextClickListener
     */
    public B addContextClickListener(ContextClickListener contextClickListener) {
        delegate.addContextClickListener(contextClickListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponent#setWidth
     */
    public B setWidth(String width) {
        delegate.setWidth(width);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponent#setWidth
     */
    public B setWidth(float width, Unit unit) {
        delegate.setWidth(width, unit);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponent#addListener
     */
    public B addListener(Listener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponent#setHeight
     */
    public B setHeight(String height) {
        delegate.setHeight(height);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponent#setHeight
     */
    public B setHeight(float height, Unit unit) {
        delegate.setHeight(height, unit);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponent#setSizeFull
     */
    public B setSizeFull() {
        delegate.setSizeFull();
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponent#setSizeUndefined
     */
    public B setSizeUndefined() {
        delegate.setSizeUndefined();
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponent#setWidthUndefined
     */
    public B setWidthUndefined() {
        delegate.setWidthUndefined();
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractComponent#setHeightUndefined
     */
    public B setHeightUndefined() {
        delegate.setHeightUndefined();
        return self;
    }
    
}
