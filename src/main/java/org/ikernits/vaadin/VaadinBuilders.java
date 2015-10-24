package org.ikernits.vaadin;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
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
    
}
