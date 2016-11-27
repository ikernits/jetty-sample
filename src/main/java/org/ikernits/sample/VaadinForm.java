package org.ikernits.sample;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.MoreExecutors;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.converter.AbstractStringToNumberConverter;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.data.util.converter.StringToLongConverter;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.ikernits.sample.util.ListenerList;
import org.ikernits.vaadin.VaadinBuilders;
import org.ikernits.vaadin.VaadinComponentAttributes.ComponentAttributes.Side;
import org.ikernits.vaadin.VaadinComponentAttributes.ComponentAttributes.Size;
import org.joda.time.DateTime;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.ikernits.vaadin.VaadinComponentAttributes.ComponentAttributes.vaStyleMargin;

public class VaadinForm {

    public enum FormLayoutType {
        Horizontal, Vertical, Form
    }

    public static class FormProperty<T> {
        private T value;
        private final ObjectProperty<T> property;
        private final String shortName;
        private final String longName;
        private final Converter<String, T> converter;
        private final List<T> allowedValues;
        private final Class<? extends AbstractComponent> componentType;
        private final ListenerList<FormProperty<?>> changeListeners = new ListenerList<>(
            MoreExecutors.directExecutor(), th -> {}
        );

        @SuppressWarnings("unchecked")
        private Map<FormLayoutType, List<Consumer<? super Component>>> customComponentModifiers = new HashMap<>();

        public static FormProperty<Boolean> checkBox(String shortName, String longName, boolean initialValue) {
            return new FormProperty<>(shortName, longName, Boolean.class, CheckBox.class, initialValue);
        }

        public static FormProperty<String> stringTextField(String shortName, String longName, String initialValue) {
            return new FormProperty<>(shortName, longName, String.class, TextField.class, initialValue);
        }

        public static FormProperty<Integer> integerTextField(String shortName, String longName, int initialValue) {
            return new FormProperty<>(shortName, longName, Integer.class, TextField.class, initialValue);
        }

        public static FormProperty<Double> doubleTextField(String shortName, String longName, double initialValue) {
            return new FormProperty<>(shortName, longName, Double.class, TextField.class, initialValue);
        }

        public static FormProperty<DateTime> dateField(String shortName, String longName, DateTime initialValue) {
            return new FormProperty<>(shortName, longName, DateTime.class, DateField.class, initialValue);
        }

        public static FormProperty<Void> button(String name) {
            return new FormProperty<>(name, name, Void.class, Button.class, null);
        }

        public static FormProperty<String> label(String initialValue) {
            return new FormProperty<>(null, null, String.class, Label.class, initialValue);
        }

        public static FormProperty<String> stringComboBox(String shortName, String longName, String initialValue, List<String> values) {
            return comboBox(shortName, longName, initialValue, values, Maps.uniqueIndex(values, v -> v), String.class);
        }

        public static FormProperty<Enum> enumComboBox(String shortName, String longName, Enum<?> initialValue) {
            List<Enum> values = Arrays.asList(initialValue.getClass().getEnumConstants());
            return comboBox(
                shortName,
                longName,
                initialValue,
                values,
                Maps.toMap(values, Enum::name),
                Enum.class
            );
        }

        public static <T> FormProperty<T> comboBox(
            String shortName, String longName, T initialValue,
            List<T> values, Map<T, String> valueMapping,
            Class<T> propertyType) {

            return new FormProperty<>(
                shortName,
                longName,
                propertyType,
                ComboBox.class,
                initialValue,
                createConverterForMap(valueMapping, String.class, propertyType),
                values
            );
        }

        private FormProperty(String shortName, String longName, Class<T> propertyType, Class<? extends AbstractComponent> componentType, T initialValue) {
           this(shortName, longName, propertyType, componentType, initialValue, null, null);
        }

        private static <P, M> Converter<P, M> createConverterForMap(Map<M, P> modelToPresentationMapping,
                                                                    Class<P> presentationClazz, Class<M> modelClazz) {
            return new Converter<P, M>() {
                final BiMap<M, P> presentationMapping = ImmutableBiMap.copyOf(modelToPresentationMapping);
                final BiMap<P, M> modelMapping = presentationMapping.inverse();

                @Override
                public M convertToModel(P value, Class<? extends M> targetType, Locale locale) throws ConversionException {
                    return modelMapping.get(value);
                }

                @Override
                public P convertToPresentation(M value, Class<? extends P> targetType, Locale locale) throws ConversionException {
                    return presentationMapping.get(value);
                }

                @Override
                public Class<M> getModelType() {
                    return modelClazz;
                }

                @Override
                public Class<P> getPresentationType() {
                    return presentationClazz;
                }
            };
        }

        private FormProperty(
            String shortName, String longName,
            Class<T> clazz, Class<? extends AbstractComponent> componentType,
            T initialValue, Converter<String, T> converter, List<T> allowedValues
        ) {
            this.shortName = shortName;
            this.longName = longName;
            this.property = new ObjectProperty<>(initialValue, clazz);
            this.value = initialValue;
            this.property.addValueChangeListener(e -> changeListeners.fire(this));
            this.converter = converter;
            this.allowedValues = allowedValues;
            this.componentType = componentType;
        }

        public T getValue() {
            return value;
        }

        public ObjectProperty<T> getProperty() {
            return property;
        }

        public Class<? extends AbstractComponent> getComponentType() {
            return componentType;
        }

        public String getShortName() {
            return shortName;
        }

        public String getLongName() {
            return longName;
        }

        public Converter<String, T> getConverter() {
            return converter;
        }

        public List<T> getAllowedValues() {
            return allowedValues;
        }

        public T store() {
            value = property.getValue();
            return value;
        }

        public T load() {
            property.setValue(value);
            return value;
        }

        @SafeVarargs
        public final FormProperty<T> setCustomComponentModifiers(
            FormLayoutType layoutType, Consumer<? super Component>... modifiers) {
            this.customComponentModifiers.put(layoutType, Arrays.asList(modifiers));
            return this;
        }


        public List<Consumer<? super Component>> getCustomComponentModifiers(FormLayoutType layoutType) {
            return customComponentModifiers.getOrDefault(layoutType, ImmutableList.of());
        }
    }

    private static final Map<Class<?>, AbstractStringToNumberConverter<?>> plainNumberConverters = ImmutableMap.of(
        Integer.class, new StringToIntegerConverter() {
            @Override
            protected java.text.NumberFormat getFormat(Locale locale) {
                NumberFormat format = super.getFormat(locale);
                format.setGroupingUsed(false);
                return format;
            }
        },
        Long.class, new StringToLongConverter() {
            @Override
            protected java.text.NumberFormat getFormat(Locale locale) {
                NumberFormat format = super.getFormat(locale);
                format.setGroupingUsed(false);
                return format;
            }
        },
        Double.class, new StringToDoubleConverter() {
            @Override
            protected java.text.NumberFormat getFormat(Locale locale) {
                NumberFormat format = super.getFormat(locale);
                format.setGroupingUsed(false);
                return format;
            }
        }
    );

    private static final Converter<Date, DateTime> dateTimeConverter = new Converter<Date, DateTime>() {
        @Override
        public DateTime convertToModel(Date value, Class<? extends DateTime> targetType, Locale locale) throws ConversionException {
            return new DateTime(value);
        }

        @Override
        public Date convertToPresentation(DateTime value, Class<? extends Date> targetType, Locale locale) throws ConversionException {
            return value.toDate();
        }

        @Override
        public Class<DateTime> getModelType() {
            return DateTime.class;
        }

        @Override
        public Class<Date> getPresentationType() {
            return Date.class;
        }
    };


    private final List<FormProperty<?>> properties;
    private final ListenerList<FormProperty<?>> changeListeners = new ListenerList<>(
        MoreExecutors.directExecutor(), th -> {}
    );

    public VaadinForm(List<FormProperty<?>> properties) {
        this.properties = properties;
        this.properties.forEach(p -> p.changeListeners.add(changeListeners::fire));
    }

    public VaadinForm(FormProperty<?>... properties) {
        this(Arrays.asList(properties));
    }

    private <T> AbstractComponent createComponentForProperty(FormProperty<T> uip) {
        Class<? extends AbstractComponent> type = uip.getComponentType();

        if (type == Button.class) {
            return VaadinBuilders.button()
                .setCaption(uip.getShortName())
                .addClickListener(e -> uip.getProperty().setValue(null))
                .build();
        } else if (type == Label.class) {
            return VaadinBuilders.label()
                .setContentMode(ContentMode.HTML)
                .setPropertyDataSource(uip.getProperty())
                .setWidthUndefined()
                .build();
        } else if (type == CheckBox.class) {
            return VaadinBuilders.checkBox()
                .setPropertyDataSource(uip.getProperty())
                .setValidationVisible(true)
                .setImmediate(true)
                .build();
        } else if (type == DateField.class) {
            return VaadinBuilders.dateField()
                .setConverter(dateTimeConverter)
                .setPropertyDataSource(uip.getProperty())
                .setValidationVisible(true)
                .setImmediate(true)
                .build();
        } else if (type == ComboBox.class) {
            return VaadinBuilders.comboBox()
                .setMultiSelect(false)
                .setConverter(uip.getConverter())
                .setNullSelectionAllowed(false)
                .setTextInputAllowed(false)
                .setValidationVisible(true)
                .addItems(uip.getAllowedValues().stream()
                    .map(v -> uip.getConverter().convertToPresentation(v, String.class, Locale.getDefault()))
                    .collect(Collectors.toList()))
                .setPropertyDataSource(uip.getProperty())
                .build();
        } else if (type == TextField.class) {
            TextField textField = VaadinBuilders.textField()
                .setPropertyDataSource(uip.getProperty())
                .setValidationVisible(true)
                .setImmediate(true)
                .build();

            Class<?> propertyType = uip.getProperty().getType();
            if (Number.class.isAssignableFrom(propertyType)) {
                textField.setNullRepresentation("");
                if (plainNumberConverters.containsKey(propertyType)) {
                    textField.setConverter(plainNumberConverters.get(propertyType));
                }
            }

            return textField;
        } else {
            throw new IllegalStateException("Component type: '" + type.getSimpleName() + "' is not supported");
        }
    }

    private void addComponentsToLayout(
        Layout layout, FormLayoutType layoutType,
        boolean useShortNames,
        boolean setCaptions,
        List<Consumer<Component>> labelModifiers,
        List<Consumer<Component>> componentModifiers) {
        properties.forEach(uip -> {
            final String name = useShortNames ? uip.getShortName() : uip.getLongName();
            AbstractComponent component = createComponentForProperty(uip);
            componentModifiers.forEach(cm -> cm.accept(component));
            uip.getCustomComponentModifiers(layoutType).forEach(cm -> cm.accept(component));

            if (setCaptions) {
                if (name != null) {
                    component.setCaption(name);
                    component.setCaptionAsHtml(true);
                }
            } else {
                if (name != null) {
                    Label label = VaadinBuilders.label()
                        .setContentMode(ContentMode.HTML)
                        .setValue(uip.getShortName())
                        .build();
                    labelModifiers.forEach(cm -> cm.accept(label));
                    layout.addComponent(label);
                }
            }

            if (component != null) {
                layout.addComponent(component);
            }
        });
    }

    @SafeVarargs
    public final HorizontalLayout createHorizontalLayout(Consumer<? super HorizontalLayout>... modifiers) {
        HorizontalLayout layout = VaadinBuilders.horizontalLayout()
            .setAttributes(modifiers)
            .setDefaultComponentAlignment(Alignment.MIDDLE_LEFT)
            .build();
        addComponentsToLayout(layout, FormLayoutType.Horizontal, true, false,
            ImmutableList.of(vaStyleMargin(Side.Right, Size.Tiny)),
            ImmutableList.of(vaStyleMargin(Side.Right, Size.Small)));
        return layout;
    }


    @SafeVarargs
    public final VerticalLayout createVerticalLayout(Consumer<? super VerticalLayout>... modifiers) {
        VerticalLayout layout = VaadinBuilders.verticalLayout()
            .setAttributes(modifiers)
            .build();
        addComponentsToLayout(layout, FormLayoutType.Vertical, false, false,
            ImmutableList.of(vaStyleMargin(Side.Bottom, Size.Tiny)),
            ImmutableList.of(vaStyleMargin(Side.Bottom, Size.Small)));
        return layout;
    }

    @SafeVarargs
    public final FormLayout createFormLayout(Consumer<? super FormLayout>... modifiers) {
        FormLayout layout = VaadinBuilders.formLayout()
            .setAttributes(modifiers)
            .build();
        addComponentsToLayout(layout, FormLayoutType.Form, false, true,
            ImmutableList.of(),
            ImmutableList.of());
        return layout;
    }

    public void addChangeListener(Consumer<FormProperty<?>> listener) {
        changeListeners.add(listener);
    }

    public void removeChangeListener(Consumer<FormProperty<?>> listener) {
        changeListeners.remove(listener);
    }
}
