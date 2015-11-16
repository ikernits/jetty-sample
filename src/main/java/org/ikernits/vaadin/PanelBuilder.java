package org.ikernits.vaadin;

import com.vaadin.ui.Panel;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.MouseEvents.ClickListener;

public class PanelBuilder<T extends Panel, B extends PanelBuilder<T, B>> extends AbstractSingleComponentContainerBuilder<T, B> {

    public PanelBuilder(T delegate) {
        super(delegate);
    }
    
    /**
     * @see com.vaadin.ui.Panel#addListener
     */
    public B addListener(ClickListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Panel#setCaption
     */
    public B setCaption(String caption) {
        delegate.setCaption(caption);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Panel#addClickListener
     */
    public B addClickListener(ClickListener clickListener) {
        delegate.addClickListener(clickListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Panel#setTabIndex
     */
    public B setTabIndex(int tabIndex) {
        delegate.setTabIndex(tabIndex);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Panel#setScrollLeft
     */
    public B setScrollLeft(int scrollLeft) {
        delegate.setScrollLeft(scrollLeft);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Panel#setScrollTop
     */
    public B setScrollTop(int scrollTop) {
        delegate.setScrollTop(scrollTop);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Panel#addActionHandler
     */
    public B addActionHandler(Handler actionHandler) {
        delegate.addActionHandler(actionHandler);
        return self;
    }
    
}
