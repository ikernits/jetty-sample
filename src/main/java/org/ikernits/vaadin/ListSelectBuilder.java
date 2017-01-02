package org.ikernits.vaadin;

import com.vaadin.ui.ListSelect;

@SuppressWarnings({"deprecation", "unused", "unchecked"})
public class ListSelectBuilder<T extends ListSelect, B extends ListSelectBuilder<T, B>> extends AbstractSelectBuilder<T, B> {

    public ListSelectBuilder(T delegate) {
        super(delegate);
    }
    
    /**
     * @see com.vaadin.ui.ListSelect#setRows
     */
    public B setRows(int rows) {
        delegate.setRows(rows);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.ListSelect#setColumns
     */
    public B setColumns(int columns) {
        delegate.setColumns(columns);
        return self;
    }
    
}
