package org.ikernits.sample;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.MoreExecutors;
import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.ikernits.sample.util.ListenerList;
import org.ikernits.vaadin.VaadinBuilders;
import org.ikernits.vaadin.VaadinComponentAttributes;
import org.ikernits.vaadin.VaadinComponentAttributes.ComponentAttributes.Side;
import org.ikernits.vaadin.VaadinComponentAttributes.ComponentAttributes.Size;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import static org.ikernits.vaadin.VaadinComponentAttributes.ComponentAttributes.vaStyleMargin;
import static org.ikernits.vaadin.VaadinComponentAttributes.ComponentAttributes.vaStyleMarginSmall;
import static org.ikernits.vaadin.VaadinComponentAttributes.ComponentAttributes.vaStyleMarginTiny;
import static org.ikernits.vaadin.VaadinComponentAttributes.ComponentAttributes.vaStylePadding;
import static org.ikernits.vaadin.VaadinComponentAttributes.ComponentAttributes.vaStylePaddingSmall;
import static org.ikernits.vaadin.VaadinComponentAttributes.ComponentAttributes.vaStylePaddingTiny;
import static org.ikernits.vaadin.VaadinComponentAttributes.LayoutAttributes.vaMargin;
import static org.ikernits.vaadin.VaadinComponentAttributes.LayoutAttributes.vaSpacing;

public class VaadinForm {
    private static class UiProperty<T> {
        private T value;
        private final ObjectProperty<T> property;
        private final String shortName;
        private final String longName;
        private final List<T> allowedValues;

        public UiProperty(String shortName, String longName, Class<T> clazz, T defaultValue, List<T> allowedValues) {
            this.shortName = shortName;
            this.longName = longName;
            this.property = new ObjectProperty<>(defaultValue, clazz);
            this.value = defaultValue;
            this.allowedValues = allowedValues;
        }

        public T getValue() {
            return value;
        }

        public Property<T> getProperty() {
            return property;
        }

        public String getShortName() {
            return shortName;
        }

        public String getLongName() {
            return longName;
        }

        public void load() {
            this.property.setValue(value);
        }

        public void store() {
            value = this.property.getValue();
        }

        public List<T> getAllowedValues() {
            return allowedValues;
        }
    }

    private static class UiProperties {
        final List<UiProperty<?>> properties;
        final ListenerList<UiProperty<?>> changeListeners = new ListenerList<>(
            MoreExecutors.directExecutor(), th -> {}
        );

        public UiProperties(List<UiProperty<?>> properties) {
            this.properties = properties;
        }

        private void onValueChange(UiProperty<?> uip) {

        }

        private Component createComponentForProperty(UiProperty<?> uip) {
            if (uip.getProperty().getType() == Void.class) {
                return VaadinBuilders.button()
                    .setCaption(uip.getShortName())
                    .addClickListener(e -> changeListeners.fire(uip))
                    .build();
            } else if (uip.getProperty().getType() == Boolean.class) {
                return VaadinBuilders.checkBox()
                    .setPropertyDataSource(uip.getProperty())
                    .addValueChangeListener(e -> changeListeners.fire(uip))
                    .build();
            } else if (uip.getProperty().getType() == Date.class) {
                return VaadinBuilders.dateField()
                    .setPropertyDataSource(uip.getProperty())
                    .addValueChangeListener(e -> changeListeners.fire(uip))
                    .build();
            } else {
                if (uip.getAllowedValues().isEmpty()) {
                    return VaadinBuilders.textField()
                        .setPropertyDataSource(uip.getProperty())
                        .addValueChangeListener(e -> changeListeners.fire(uip))
                        .build();
                } else {
                    return VaadinBuilders.comboBox()
                        .setMultiSelect(false)
                        .setNullSelectionAllowed(false)
                        .setTextInputAllowed(false)
                        .addItems(uip.getAllowedValues())
                        .setPropertyDataSource(uip.getProperty())
                        .addValueChangeListener(e -> changeListeners.fire(uip))
                        .build();
                }
            }
        }

        private void addComponentsToLayout(
            Layout layout,
            boolean useShortNames,
            boolean setCaptions,
            List<Consumer<Component>> labelModifiers,
            List<Consumer<Component>> componentModifiers
        ) {
            properties.forEach(uip -> {
                final String name = useShortNames ? uip.getShortName() : uip.getLongName();
                Component component = createComponentForProperty(uip);
                componentModifiers.forEach(cm -> cm.accept(component));

                if (setCaptions) {
                    component.setCaption(name);
                } else {
                    Label label = VaadinBuilders.label()
                        .setValue(uip.getShortName())
                        .build();
                    labelModifiers.forEach(cm -> cm.accept(label));
                    layout.addComponent(label);
                }

                layout.addComponent(component);
            });
        }

        @SafeVarargs
        public final HorizontalLayout createHorizontalLayout(Consumer<? super HorizontalLayout>... modifiers) {
            HorizontalLayout layout = VaadinBuilders.horizontalLayout()
                .setAttributes(modifiers)
                .setDefaultComponentAlignment(Alignment.MIDDLE_LEFT)
                .build();
            addComponentsToLayout(layout, true, false,
                ImmutableList.of(vaStyleMargin(Side.Right, Size.Tiny)),
                ImmutableList.of(vaStyleMargin(Side.Right, Size.Small)));
            return layout;
        };

        @SafeVarargs
        public final VerticalLayout createVerticalLayout(Consumer<? super VerticalLayout>... modifiers) {
            VerticalLayout layout = VaadinBuilders.verticalLayout()
                .setAttributes(modifiers)
                .build();
            addComponentsToLayout(layout, false, false,
                ImmutableList.of(vaStyleMargin(Side.Bottom, Size.Tiny)),
                ImmutableList.of(vaStyleMargin(Side.Bottom, Size.Small)));
            return layout;
        }

        @SafeVarargs
        public final FormLayout createFormLayout(Consumer<? super FormLayout>... modifiers) {
            FormLayout layout = VaadinBuilders.formLayout()
                .setAttributes(modifiers)
                .build();
            addComponentsToLayout(layout, false, true,
                ImmutableList.of(),
                ImmutableList.of());
            return layout;
        }

        public void addChangeListener(Consumer<UiProperty<?>> listener) {
            changeListeners.add(listener);
        }

        public void removeChangeListener(Object key) {
            changeListeners.remove(key);
        }
    }

    public Component createLayout() {
        UiProperties properties = new UiProperties(ImmutableList.of(
            new UiProperty<>("Button", "Button component", Void.class, null, ImmutableList.of()),
            new UiProperty<>("Text", "Text Field", String.class, "default", ImmutableList.of()),
            new UiProperty<>("Integer", "Integer Number", Integer.class, 10, ImmutableList.of()),
            new UiProperty<>("Double", "Double Number", Double.class, 10., ImmutableList.of()),
            new UiProperty<>("Boolean", "Boolean Flag", Boolean.class, Boolean.TRUE, ImmutableList.of()),
            new UiProperty<>("Date", "Date Value", Date.class, DateTime.now().toDate(), ImmutableList.of()),
            new UiProperty<>("String List", "String List", String.class, "A", ImmutableList.of("A", "B", "C")),
            new UiProperty<>("Number List", "Number List", Integer.class, 1, ImmutableList.of(1, 2, 3))
        ));

        properties.addChangeListener(uip -> {
            String caption =  "Update: '" + uip.getLongName()
                + "' from " + uip.getValue()
                + " to " + uip.getProperty().getValue();
            Notification.show(caption);
            uip.store();
        });

        return VaadinBuilders.verticalLayout()
            .setAttributes(vaMargin)
            .addComponent(VaadinBuilders.panel()
                .setAttributes(vaStyleMarginTiny)
                .setCaption("Horizontal")
                .setContent(properties.createHorizontalLayout(
                    vaStylePaddingTiny
                ))
                .build())
            .addComponent(VaadinBuilders.panel()
                .setAttributes(vaStyleMarginTiny)
                .setCaption("Vertical")
                .setContent(properties.createVerticalLayout(
                    vaStylePaddingTiny
                ))
                .build())
            .addComponent(VaadinBuilders.panel()
                .setAttributes(vaStyleMarginTiny)
                .setCaption("Form")
                .setContent(properties.createFormLayout())
                .build())
            .build();
    }
}
