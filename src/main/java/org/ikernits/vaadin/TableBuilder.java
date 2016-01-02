package org.ikernits.vaadin;

import com.vaadin.ui.Table;
import com.vaadin.data.Container;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MultiSelectMode;
import com.vaadin.ui.AbstractSelect.ItemDescriptionGenerator;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.CellStyleGenerator;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.Table.ColumnReorderListener;
import com.vaadin.ui.Table.ColumnResizeListener;
import com.vaadin.ui.Table.FooterClickListener;
import com.vaadin.ui.Table.HeaderClickListener;
import com.vaadin.ui.Table.RowGenerator;
import com.vaadin.ui.Table.RowHeaderMode;
import com.vaadin.ui.Table.TableDragMode;
import com.vaadin.ui.TableFieldFactory;
import java.util.Collection;

public class TableBuilder<T extends Table, B extends TableBuilder<T, B>> extends AbstractSelectBuilder<T, B> {

    public TableBuilder(T delegate) {
        super(delegate);
    }
    
    /**
     * @see com.vaadin.ui.Table#addListener
     */
    public B addListener(FooterClickListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#addListener
     */
    public B addListener(ColumnResizeListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#addListener
     */
    public B addListener(ItemClickListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#addListener
     */
    public B addListener(HeaderClickListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#addListener
     */
    public B addListener(ColumnReorderListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#addItem
     */
    public B addItem(Object[] param1, Object param2) {
        delegate.addItem(param1, param2);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setColumnWidth
     */
    public B setColumnWidth(Object param1, int param2) {
        delegate.setColumnWidth(param1, param2);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setColumnExpandRatio
     */
    public B setColumnExpandRatio(Object param1, float param2) {
        delegate.setColumnExpandRatio(param1, param2);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setPageLength
     */
    public B setPageLength(int pageLength) {
        delegate.setPageLength(pageLength);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setCacheRate
     */
    public B setCacheRate(double cacheRate) {
        delegate.setCacheRate(cacheRate);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setCurrentPageFirstItemId
     */
    public B setCurrentPageFirstItemId(Object currentPageFirstItemId) {
        delegate.setCurrentPageFirstItemId(currentPageFirstItemId);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setColumnIcon
     */
    public B setColumnIcon(Object param1, Resource param2) {
        delegate.setColumnIcon(param1, param2);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setColumnHeader
     */
    public B setColumnHeader(Object param1, String param2) {
        delegate.setColumnHeader(param1, param2);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setColumnAlignment
     */
    public B setColumnAlignment(Object param1, Align param2) {
        delegate.setColumnAlignment(param1, param2);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setColumnCollapsed
     */
    public B setColumnCollapsed(Object param1, boolean param2) {
        delegate.setColumnCollapsed(param1, param2);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setColumnCollapsingAllowed
     */
    public B setColumnCollapsingAllowed(boolean columnCollapsingAllowed) {
        delegate.setColumnCollapsingAllowed(columnCollapsingAllowed);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setColumnCollapsible
     */
    public B setColumnCollapsible(Object param1, boolean param2) {
        delegate.setColumnCollapsible(param1, param2);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setColumnReorderingAllowed
     */
    public B setColumnReorderingAllowed(boolean columnReorderingAllowed) {
        delegate.setColumnReorderingAllowed(columnReorderingAllowed);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setCurrentPageFirstItemIndex
     */
    public B setCurrentPageFirstItemIndex(int currentPageFirstItemIndex) {
        delegate.setCurrentPageFirstItemIndex(currentPageFirstItemIndex);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setSelectable
     */
    public B setSelectable(boolean selectable) {
        delegate.setSelectable(selectable);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setColumnHeaderMode
     */
    public B setColumnHeaderMode(ColumnHeaderMode columnHeaderMode) {
        delegate.setColumnHeaderMode(columnHeaderMode);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setRowHeaderMode
     */
    public B setRowHeaderMode(RowHeaderMode rowHeaderMode) {
        delegate.setRowHeaderMode(rowHeaderMode);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setContainerDataSource
     */
    public B setContainerDataSource(Container param1, Collection param2) {
        delegate.setContainerDataSource(param1, param2);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setContainerDataSource
     */
    public B setContainerDataSource(Container containerDataSource) {
        delegate.setContainerDataSource(containerDataSource);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#addContainerProperty
     */
    public B addContainerProperty(Object param1, Class param2, Object param3) {
        delegate.addContainerProperty(param1, param2, param3);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#addContainerProperty
     */
    public B addContainerProperty(Object param1, Class param2, Object param3, String param4, Resource param5, Align param6) {
        delegate.addContainerProperty(param1, param2, param3, param4, param5, param6);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#addGeneratedColumn
     */
    public B addGeneratedColumn(Object param1, ColumnGenerator param2) {
        delegate.addGeneratedColumn(param1, param2);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setNewItemsAllowed
     */
    public B setNewItemsAllowed(boolean newItemsAllowed) {
        delegate.setNewItemsAllowed(newItemsAllowed);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#addItemAfter
     */
    public B addItemAfter(Object param1, Object param2) {
        delegate.addItemAfter(param1, param2);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#addItemAfter
     */
    public B addItemAfter(Object itemAfter) {
        delegate.addItemAfter(itemAfter);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setTableFieldFactory
     */
    public B setTableFieldFactory(TableFieldFactory tableFieldFactory) {
        delegate.setTableFieldFactory(tableFieldFactory);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setEditable
     */
    public B setEditable(boolean editable) {
        delegate.setEditable(editable);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setSortContainerPropertyId
     */
    public B setSortContainerPropertyId(Object sortContainerPropertyId) {
        delegate.setSortContainerPropertyId(sortContainerPropertyId);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setSortAscending
     */
    public B setSortAscending(boolean sortAscending) {
        delegate.setSortAscending(sortAscending);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setSortDisabled
     */
    public B setSortDisabled(boolean sortDisabled) {
        delegate.setSortDisabled(sortDisabled);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setSortEnabled
     */
    public B setSortEnabled(boolean sortEnabled) {
        delegate.setSortEnabled(sortEnabled);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setCellStyleGenerator
     */
    public B setCellStyleGenerator(CellStyleGenerator cellStyleGenerator) {
        delegate.setCellStyleGenerator(cellStyleGenerator);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#addItemClickListener
     */
    public B addItemClickListener(ItemClickListener itemClickListener) {
        delegate.addItemClickListener(itemClickListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setDragMode
     */
    public B setDragMode(TableDragMode dragMode) {
        delegate.setDragMode(dragMode);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setDropHandler
     */
    public B setDropHandler(DropHandler dropHandler) {
        delegate.setDropHandler(dropHandler);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setMultiSelectMode
     */
    public B setMultiSelectMode(MultiSelectMode multiSelectMode) {
        delegate.setMultiSelectMode(multiSelectMode);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#addHeaderClickListener
     */
    public B addHeaderClickListener(HeaderClickListener headerClickListener) {
        delegate.addHeaderClickListener(headerClickListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#addFooterClickListener
     */
    public B addFooterClickListener(FooterClickListener footerClickListener) {
        delegate.addFooterClickListener(footerClickListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setColumnFooter
     */
    public B setColumnFooter(Object param1, String param2) {
        delegate.setColumnFooter(param1, param2);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setFooterVisible
     */
    public B setFooterVisible(boolean footerVisible) {
        delegate.setFooterVisible(footerVisible);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#addColumnResizeListener
     */
    public B addColumnResizeListener(ColumnResizeListener columnResizeListener) {
        delegate.addColumnResizeListener(columnResizeListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#addColumnReorderListener
     */
    public B addColumnReorderListener(ColumnReorderListener columnReorderListener) {
        delegate.addColumnReorderListener(columnReorderListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setItemDescriptionGenerator
     */
    public B setItemDescriptionGenerator(ItemDescriptionGenerator itemDescriptionGenerator) {
        delegate.setItemDescriptionGenerator(itemDescriptionGenerator);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setRowGenerator
     */
    public B setRowGenerator(RowGenerator rowGenerator) {
        delegate.setRowGenerator(rowGenerator);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setConverter
     */
    public B setConverter(Object param1, Converter param2) {
        delegate.setConverter(param1, param2);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#addActionHandler
     */
    public B addActionHandler(Handler actionHandler) {
        delegate.addActionHandler(actionHandler);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setEnabled
     */
    public B setEnabled(boolean enabled) {
        delegate.setEnabled(enabled);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Table#setVisible
     */
    public B setVisible(boolean visible) {
        delegate.setVisible(visible);
        return self;
    }
    
}
