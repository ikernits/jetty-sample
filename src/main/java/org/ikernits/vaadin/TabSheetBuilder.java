package org.ikernits.vaadin;

import com.vaadin.ui.TabSheet;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet.CloseHandler;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;

public class TabSheetBuilder<T extends TabSheet, B extends TabSheetBuilder<T, B>> extends AbstractComponentContainerBuilder<T, B> {

    public TabSheetBuilder(T delegate) {
        super(delegate);
    }
    
    /**
     * @see com.vaadin.ui.TabSheet#addComponent
     */
    public B addComponent(Component component) {
        delegate.addComponent(component);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.TabSheet#addListener
     */
    public B addListener(BlurListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.TabSheet#addListener
     */
    public B addListener(FocusListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.TabSheet#addListener
     */
    public B addListener(SelectedTabChangeListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.TabSheet#setTabIndex
     */
    public B setTabIndex(int tabIndex) {
        delegate.setTabIndex(tabIndex);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.TabSheet#addBlurListener
     */
    public B addBlurListener(BlurListener blurListener) {
        delegate.addBlurListener(blurListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.TabSheet#addFocusListener
     */
    public B addFocusListener(FocusListener focusListener) {
        delegate.addFocusListener(focusListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.TabSheet#addTab
     */
    public B addTab(Component tab) {
        delegate.addTab(tab);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.TabSheet#addTab
     */
    public B addTab(Component param1, String param2, Resource param3) {
        delegate.addTab(param1, param2, param3);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.TabSheet#addTab
     */
    public B addTab(Component param1, String param2) {
        delegate.addTab(param1, param2);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.TabSheet#addTab
     */
    public B addTab(Component param1, String param2, Resource param3, int param4) {
        delegate.addTab(param1, param2, param3, param4);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.TabSheet#addTab
     */
    public B addTab(Component param1, int param2) {
        delegate.addTab(param1, param2);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.TabSheet#setTabsVisible
     */
    public B setTabsVisible(boolean tabsVisible) {
        delegate.setTabsVisible(tabsVisible);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.TabSheet#setSelectedTab
     */
    public B setSelectedTab(Component selectedTab) {
        delegate.setSelectedTab(selectedTab);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.TabSheet#setSelectedTab
     */
    public B setSelectedTab(int selectedTab) {
        delegate.setSelectedTab(selectedTab);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.TabSheet#setSelectedTab
     */
    public B setSelectedTab(Tab selectedTab) {
        delegate.setSelectedTab(selectedTab);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.TabSheet#addSelectedTabChangeListener
     */
    public B addSelectedTabChangeListener(SelectedTabChangeListener selectedTabChangeListener) {
        delegate.addSelectedTabChangeListener(selectedTabChangeListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.TabSheet#setCloseHandler
     */
    public B setCloseHandler(CloseHandler closeHandler) {
        delegate.setCloseHandler(closeHandler);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.TabSheet#setTabPosition
     */
    public B setTabPosition(Tab param1, int param2) {
        delegate.setTabPosition(param1, param2);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.TabSheet#setTabCaptionsAsHtml
     */
    public B setTabCaptionsAsHtml(boolean tabCaptionsAsHtml) {
        delegate.setTabCaptionsAsHtml(tabCaptionsAsHtml);
        return self;
    }
    
}
