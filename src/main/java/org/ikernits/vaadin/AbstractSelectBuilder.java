package org.ikernits.vaadin;

import com.vaadin.ui.AbstractSelect;
import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeListener;
import com.vaadin.data.Container.PropertySetChangeListener;
import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.AbstractSelect.NewItemHandler;
import java.util.Collection;

public class AbstractSelectBuilder<T extends AbstractSelect, B extends AbstractSelectBuilder<T, B>> extends AbstractFieldBuilder<T, B> {

    public AbstractSelectBuilder(T delegate) {
        super(delegate);
    }
    
    /**
     * @see com.vaadin.ui.AbstractSelect#setValue
     */
    public B setValue(Object value) {
        delegate.setValue(value);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractSelect#addListener
     */
    public B addListener(ItemSetChangeListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractSelect#addListener
     */
    public B addListener(PropertySetChangeListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractSelect#addItem
     */
    public B addItem(Object item) {
        delegate.addItem(item);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractSelect#addItem
     */
    public B addItem() {
        delegate.addItem();
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractSelect#setContainerDataSource
     */
    public B setContainerDataSource(Container containerDataSource) {
        delegate.setContainerDataSource(containerDataSource);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractSelect#addContainerProperty
     */
    public B addContainerProperty(Object param1, Class param2, Object param3) {
        delegate.addContainerProperty(param1, param2, param3);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractSelect#setNewItemsAllowed
     */
    public B setNewItemsAllowed(boolean newItemsAllowed) {
        delegate.setNewItemsAllowed(newItemsAllowed);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractSelect#setItemCaptionMode
     */
    public B setItemCaptionMode(ItemCaptionMode itemCaptionMode) {
        delegate.setItemCaptionMode(itemCaptionMode);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractSelect#setNewItemHandler
     */
    public B setNewItemHandler(NewItemHandler newItemHandler) {
        delegate.setNewItemHandler(newItemHandler);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractSelect#addItems
     */
    public B addItems(Collection items) {
        delegate.addItems(items);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractSelect#setMultiSelect
     */
    public B setMultiSelect(boolean multiSelect) {
        delegate.setMultiSelect(multiSelect);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractSelect#setItemCaption
     */
    public B setItemCaption(Object param1, String param2) {
        delegate.setItemCaption(param1, param2);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractSelect#setItemIcon
     */
    public B setItemIcon(Object param1, Resource param2) {
        delegate.setItemIcon(param1, param2);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractSelect#setItemCaptionPropertyId
     */
    public B setItemCaptionPropertyId(Object itemCaptionPropertyId) {
        delegate.setItemCaptionPropertyId(itemCaptionPropertyId);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractSelect#setItemIconPropertyId
     */
    public B setItemIconPropertyId(Object itemIconPropertyId) {
        delegate.setItemIconPropertyId(itemIconPropertyId);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractSelect#addPropertySetChangeListener
     */
    public B addPropertySetChangeListener(PropertySetChangeListener propertySetChangeListener) {
        delegate.addPropertySetChangeListener(propertySetChangeListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractSelect#addItemSetChangeListener
     */
    public B addItemSetChangeListener(ItemSetChangeListener itemSetChangeListener) {
        delegate.addItemSetChangeListener(itemSetChangeListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractSelect#setNullSelectionAllowed
     */
    public B setNullSelectionAllowed(boolean nullSelectionAllowed) {
        delegate.setNullSelectionAllowed(nullSelectionAllowed);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.AbstractSelect#setNullSelectionItemId
     */
    public B setNullSelectionItemId(Object nullSelectionItemId) {
        delegate.setNullSelectionItemId(nullSelectionItemId);
        return self;
    }
    
}
