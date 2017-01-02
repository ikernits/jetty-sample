package org.ikernits.vaadin;

import com.vaadin.ui.Grid;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroupFieldFactory;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.event.SortEvent.SortListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Grid.CellDescriptionGenerator;
import com.vaadin.ui.Grid.CellStyleGenerator;
import com.vaadin.ui.Grid.ColumnReorderListener;
import com.vaadin.ui.Grid.ColumnResizeListener;
import com.vaadin.ui.Grid.ColumnVisibilityChangeListener;
import com.vaadin.ui.Grid.DetailsGenerator;
import com.vaadin.ui.Grid.EditorErrorHandler;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.Grid.RowDescriptionGenerator;
import com.vaadin.ui.Grid.RowStyleGenerator;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Grid.SelectionModel;
import java.util.List;

@SuppressWarnings({"deprecation", "unused", "unchecked"})
public class GridBuilder<T extends Grid, B extends GridBuilder<T, B>> extends AbstractFocusableBuilder<T, B> {

    public GridBuilder(T delegate) {
        super(delegate);
    }
    
    /**
     * @see com.vaadin.ui.Grid#setColumnReorderingAllowed
     */
    public B setColumnReorderingAllowed(boolean columnReorderingAllowed) {
        delegate.setColumnReorderingAllowed(columnReorderingAllowed);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#setContainerDataSource
     */
    public B setContainerDataSource(Indexed containerDataSource) {
        delegate.setContainerDataSource(containerDataSource);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#setCellStyleGenerator
     */
    public B setCellStyleGenerator(CellStyleGenerator cellStyleGenerator) {
        delegate.setCellStyleGenerator(cellStyleGenerator);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#addItemClickListener
     */
    public B addItemClickListener(ItemClickListener itemClickListener) {
        delegate.addItemClickListener(itemClickListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#setFooterVisible
     */
    public B setFooterVisible(boolean footerVisible) {
        delegate.setFooterVisible(footerVisible);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#addColumnResizeListener
     */
    public B addColumnResizeListener(ColumnResizeListener columnResizeListener) {
        delegate.addColumnResizeListener(columnResizeListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#addColumnReorderListener
     */
    public B addColumnReorderListener(ColumnReorderListener columnReorderListener) {
        delegate.addColumnReorderListener(columnReorderListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#addColumn
     */
    public B addColumn(Object param1, Class param2) {
        delegate.addColumn(param1, param2);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#addColumn
     */
    public B addColumn(Object column) {
        delegate.addColumn(column);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#setFrozenColumnCount
     */
    public B setFrozenColumnCount(int frozenColumnCount) {
        delegate.setFrozenColumnCount(frozenColumnCount);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#setHeightByRows
     */
    public B setHeightByRows(double heightByRows) {
        delegate.setHeightByRows(heightByRows);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#setHeightMode
     */
    public B setHeightMode(HeightMode heightMode) {
        delegate.setHeightMode(heightMode);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#setSelectionModel
     */
    public B setSelectionModel(SelectionModel selectionModel) {
        delegate.setSelectionModel(selectionModel);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#setSelectionMode
     */
    public B setSelectionMode(SelectionMode selectionMode) {
        delegate.setSelectionMode(selectionMode);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#addSelectionListener
     */
    public B addSelectionListener(SelectionListener selectionListener) {
        delegate.addSelectionListener(selectionListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#setSortOrder
     */
    public B setSortOrder(List sortOrder) {
        delegate.setSortOrder(sortOrder);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#addSortListener
     */
    public B addSortListener(SortListener sortListener) {
        delegate.addSortListener(sortListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#addHeaderRowAt
     */
    public B addHeaderRowAt(int headerRowAt) {
        delegate.addHeaderRowAt(headerRowAt);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#setDefaultHeaderRow
     */
    public B setDefaultHeaderRow(HeaderRow defaultHeaderRow) {
        delegate.setDefaultHeaderRow(defaultHeaderRow);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#setHeaderVisible
     */
    public B setHeaderVisible(boolean headerVisible) {
        delegate.setHeaderVisible(headerVisible);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#addFooterRowAt
     */
    public B addFooterRowAt(int footerRowAt) {
        delegate.addFooterRowAt(footerRowAt);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#setCellDescriptionGenerator
     */
    public B setCellDescriptionGenerator(CellDescriptionGenerator cellDescriptionGenerator) {
        delegate.setCellDescriptionGenerator(cellDescriptionGenerator);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#setRowDescriptionGenerator
     */
    public B setRowDescriptionGenerator(RowDescriptionGenerator rowDescriptionGenerator) {
        delegate.setRowDescriptionGenerator(rowDescriptionGenerator);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#setRowStyleGenerator
     */
    public B setRowStyleGenerator(RowStyleGenerator rowStyleGenerator) {
        delegate.setRowStyleGenerator(rowStyleGenerator);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#setEditorEnabled
     */
    public B setEditorEnabled(boolean editorEnabled) {
        delegate.setEditorEnabled(editorEnabled);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#setEditorFieldGroup
     */
    public B setEditorFieldGroup(FieldGroup editorFieldGroup) {
        delegate.setEditorFieldGroup(editorFieldGroup);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#setEditorFieldFactory
     */
    public B setEditorFieldFactory(FieldGroupFieldFactory editorFieldFactory) {
        delegate.setEditorFieldFactory(editorFieldFactory);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#setEditorErrorHandler
     */
    public B setEditorErrorHandler(EditorErrorHandler editorErrorHandler) {
        delegate.setEditorErrorHandler(editorErrorHandler);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#setEditorSaveCaption
     */
    public B setEditorSaveCaption(String editorSaveCaption) {
        delegate.setEditorSaveCaption(editorSaveCaption);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#setEditorCancelCaption
     */
    public B setEditorCancelCaption(String editorCancelCaption) {
        delegate.setEditorCancelCaption(editorCancelCaption);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#setEditorBuffered
     */
    public B setEditorBuffered(boolean editorBuffered) {
        delegate.setEditorBuffered(editorBuffered);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#addColumnVisibilityChangeListener
     */
    public B addColumnVisibilityChangeListener(ColumnVisibilityChangeListener columnVisibilityChangeListener) {
        delegate.addColumnVisibilityChangeListener(columnVisibilityChangeListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#setDetailsGenerator
     */
    public B setDetailsGenerator(DetailsGenerator detailsGenerator) {
        delegate.setDetailsGenerator(detailsGenerator);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#setDetailsVisible
     */
    public B setDetailsVisible(Object param1, boolean param2) {
        delegate.setDetailsVisible(param1, param2);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#addListener
     */
    public B addListener(ItemClickListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Grid#setHeight
     */
    public B setHeight(float height, Unit unit) {
        delegate.setHeight(height, unit);
        return self;
    }
    
}
