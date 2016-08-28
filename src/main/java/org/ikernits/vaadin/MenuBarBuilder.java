package org.ikernits.vaadin;

import com.vaadin.ui.MenuBar;
import com.vaadin.server.Resource;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

@SuppressWarnings({"deprecation", "unused", "unchecked"})
public class MenuBarBuilder<T extends MenuBar, B extends MenuBarBuilder<T, B>> extends AbstractComponentBuilder<T, B> {

    public MenuBarBuilder(T delegate) {
        super(delegate);
    }
    
    /**
     * @see com.vaadin.ui.MenuBar#addItem
     */
    public B addItem(String param1, Resource param2, Command param3) {
        delegate.addItem(param1, param2, param3);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.MenuBar#addItem
     */
    public B addItem(String param1, Command param2) {
        delegate.addItem(param1, param2);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.MenuBar#addItemBefore
     */
    public B addItemBefore(String param1, Resource param2, Command param3, MenuItem param4) {
        delegate.addItemBefore(param1, param2, param3, param4);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.MenuBar#setMoreMenuItem
     */
    public B setMoreMenuItem(MenuItem moreMenuItem) {
        delegate.setMoreMenuItem(moreMenuItem);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.MenuBar#setAutoOpen
     */
    public B setAutoOpen(boolean autoOpen) {
        delegate.setAutoOpen(autoOpen);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.MenuBar#setTabIndex
     */
    public B setTabIndex(int tabIndex) {
        delegate.setTabIndex(tabIndex);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.MenuBar#setHtmlContentAllowed
     */
    public B setHtmlContentAllowed(boolean htmlContentAllowed) {
        delegate.setHtmlContentAllowed(htmlContentAllowed);
        return self;
    }
    
}
