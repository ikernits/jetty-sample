package org.ikernits.sample;

import com.google.common.collect.ImmutableList;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.log4j.Logger;

/**
 * Created by ikernits on 10/10/15.
 */
@Title("Vaadin UI")
@Theme("valo-ext")
public class VaadinUI extends UI {


    Logger log = Logger.getLogger(VaadinUI.class);
    Property<Integer> count = new ObjectProperty<>(0);
    ObjectProperty<String> theme = new ObjectProperty<>("valo");
    Property<String> text = new ObjectProperty<>("123");

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout mainLayout = new VerticalLayout();

        VerticalLayout layout1 = new VerticalLayout();

        layout1.addComponent(new Label("Hello vaadin"));
        layout1.addComponent(new Button("Increment") {{
            addClickListener(clickEvent -> {
                count.setValue(count.getValue() + 1);
            });
            setStyleName(ValoTheme.BUTTON_TINY);
        }});
        layout1.setMargin(true);
        layout1.setSpacing(true);
        layout1.setSizeFull();

        TextField tf = new TextField(count);
        layout1.addComponent(tf);

        Panel panel1 = new Panel("Test panel 1", layout1);
        mainLayout.addComponent(panel1);

        VerticalLayout layout2 = new VerticalLayout();
        layout2.setMargin(true);
        layout2.setSpacing(true);

        Label l2 = new Label("123");
        l2.setImmediate(true);
        l2.setValue("321");

        Label l22 = new Label(text);
        l22.setImmediate(true);

        TextField tf2 = new TextField(text);
        tf2.setImmediate(true);
        tf2.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.EAGER);
        tf2.addTextChangeListener(event -> {
            log.info("text updated: '" + event.getText());
            l2.setValue(event.getText());
        });

        layout2.addComponents(l2, l22, tf2);
        Panel p2 = new Panel("Test panel 2", layout2);
        mainLayout.addComponent(p2);
        mainLayout.setMargin(true);

        setContent(mainLayout);

        ComboBox themeCb = new ComboBox("Theme", ImmutableList.of(
                "valo",
                "reindeer",
                "chameleon",
                "runo",
                "liferay",
                "valo-ext-blueprint",
                "valo-ext-dark",
                "valo-ext-facebook",
                "valo-ext-flat",
                "valo-ext-flatdark",
                "valo-ext-light",
                "valo-ext-metro"));

        themeCb.setNullSelectionAllowed(false);
        themeCb.setTextInputAllowed(false);
        themeCb.setPropertyDataSource(theme);
        themeCb.setPageLength(0);

        theme.addValueChangeListener(event -> {
            setTheme(theme.getValue());
        });

        mainLayout.addComponent(themeCb);
        mainLayout.setSpacing(true);
        //.setSizeFull();
    }
}

