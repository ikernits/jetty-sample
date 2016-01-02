package org.ikernits.vaadin;

import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class VaadinBuilders {

    public static VerticalLayoutBuilder<VerticalLayout, ? extends VerticalLayoutBuilder<VerticalLayout, ?>> verticalLayout() {
        return new VerticalLayoutBuilder<>(new VerticalLayout());
    }
    
    public static HorizontalLayoutBuilder<HorizontalLayout, ? extends HorizontalLayoutBuilder<HorizontalLayout, ?>> horizontalLayout() {
        return new HorizontalLayoutBuilder<>(new HorizontalLayout());
    }
    
    public static AbsoluteLayoutBuilder<AbsoluteLayout, ? extends AbsoluteLayoutBuilder<AbsoluteLayout, ?>> absoluteLayout() {
        return new AbsoluteLayoutBuilder<>(new AbsoluteLayout());
    }
    
    public static ButtonBuilder<Button, ? extends ButtonBuilder<Button, ?>> button() {
        return new ButtonBuilder<>(new Button());
    }
    
    public static TextFieldBuilder<TextField, ? extends TextFieldBuilder<TextField, ?>> textField() {
        return new TextFieldBuilder<>(new TextField());
    }
    
    public static TextAreaBuilder<TextArea, ? extends TextAreaBuilder<TextArea, ?>> textArea() {
        return new TextAreaBuilder<>(new TextArea());
    }
    
    public static LabelBuilder<Label, ? extends LabelBuilder<Label, ?>> label() {
        return new LabelBuilder<>(new Label());
    }
    
    public static PanelBuilder<Panel, ? extends PanelBuilder<Panel, ?>> panel() {
        return new PanelBuilder<>(new Panel());
    }
    
    public static MenuBarBuilder<MenuBar, ? extends MenuBarBuilder<MenuBar, ?>> menuBar() {
        return new MenuBarBuilder<>(new MenuBar());
    }
    
    public static TabSheetBuilder<TabSheet, ? extends TabSheetBuilder<TabSheet, ?>> tabSheet() {
        return new TabSheetBuilder<>(new TabSheet());
    }
    
    public static TableBuilder<Table, ? extends TableBuilder<Table, ?>> table() {
        return new TableBuilder<>(new Table());
    }
    
    public static ComboBoxBuilder<ComboBox, ? extends ComboBoxBuilder<ComboBox, ?>> comboBox() {
        return new ComboBoxBuilder<>(new ComboBox());
    }
    
    public static CheckBoxBuilder<CheckBox, ? extends CheckBoxBuilder<CheckBox, ?>> checkBox() {
        return new CheckBoxBuilder<>(new CheckBox());
    }
    
    public static LinkBuilder<Link, ? extends LinkBuilder<Link, ?>> link() {
        return new LinkBuilder<>(new Link());
    }
    
    public static GridBuilder<Grid, ? extends GridBuilder<Grid, ?>> grid() {
        return new GridBuilder<>(new Grid());
    }
    
}
