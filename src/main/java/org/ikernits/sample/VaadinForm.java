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
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.ikernits.sample.util.ListenerList;
import org.ikernits.vaadin.VaadinBuilders;
import org.ikernits.vaadin.VaadinComponentAttributes.ComponentAttributes.Side;
import org.ikernits.vaadin.VaadinComponentAttributes.ComponentAttributes.Size;
import org.joda.time.DateTime;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.ikernits.vaadin.VaadinComponentAttributes.ComponentAttributes.vaStyleMargin;
import static org.ikernits.vaadin.VaadinComponentAttributes.ComponentAttributes.vaStyleMarginTiny;
import static org.ikernits.vaadin.VaadinComponentAttributes.ComponentAttributes.vaStylePaddingTiny;
import static org.ikernits.vaadin.VaadinComponentAttributes.LayoutAttributes.vaMargin;

public class VaadinForm {

    public static class FormProperty<T> {
        private T value;
        private final ObjectProperty<T> property;
        private final String shortName;
        private final String longName;
        private final Converter<String, T> converter;
        private final List<T> allowedValues;
        final ListenerList<FormProperty<?>> changeListeners = new ListenerList<>(
            MoreExecutors.directExecutor(), th -> {}
        );

        public static FormProperty<Boolean> checkBox(String shortName, String longName, boolean initialValue) {
            return new FormProperty<>(shortName, longName, Boolean.class, initialValue);
        }

        public static FormProperty<String> stringTextField(String shortName, String longName, String initialValue) {
            return new FormProperty<>(shortName, longName, String.class, initialValue);
        }

        public static FormProperty<Integer> integerTextField(String shortName, String longName, int initialValue) {
            return new FormProperty<>(shortName, longName, Integer.class, initialValue);
        }

        public static FormProperty<Double> doubleTextField(String shortName, String longName, double initialValue) {
            return new FormProperty<>(shortName, longName, Double.class, initialValue);
        }

        public static FormProperty<DateTime> dateField(String shortName, String longName, DateTime initialValue) {
            return new FormProperty<>(shortName, longName, DateTime.class, initialValue);
        }

        public static FormProperty<Void> button(String name) {
            return new FormProperty<>(name, name, Void.class, null);
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

        public static <T> FormProperty<T> comboBox(String shortName, String longName, T initialValue, List<T> values, Map<T, String> valueMapping, Class<T> propertyType) {
            return new FormProperty<T>(
                shortName,
                longName,
                propertyType,
                initialValue,
                createConverterForMap(valueMapping, String.class, propertyType),
                values
            );
        }

        private FormProperty(String shortName, String longName, Class<T> clazz, T initialValue) {
           this(shortName, longName, clazz, initialValue, null, null);
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

        private FormProperty(String shortName, String longName, Class<T> clazz, T initialValue, Converter<String, T> converter, List<T> allowedValues) {
            this.shortName = shortName;
            this.longName = longName;
            this.property = new ObjectProperty<>(initialValue, clazz);
            this.value = initialValue;
            this.property.addValueChangeListener(e -> changeListeners.fire(this));
            this.converter = converter;
            this.allowedValues = allowedValues;
        }

        public T getValue() {
            return value;
        }

        public ObjectProperty<T> getProperty() {
            return property;
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
    }

    private static final Map<Class<?>, AbstractStringToNumberConverter<?>> plainNumberConverters = ImmutableMap.of(
        Integer.class, new StringToIntegerConverter() {
            @Override
            protected java.text.NumberFormat getFormat(Locale locale) {
                NumberFormat format = super.getFormat(locale);
                format.setGroupingUsed(false);
                return format;
            };
        },
        Long.class, new StringToLongConverter() {
            @Override
            protected java.text.NumberFormat getFormat(Locale locale) {
                NumberFormat format = super.getFormat(locale);
                format.setGroupingUsed(false);
                return format;
            };
        },
        Double.class, new StringToDoubleConverter() {
            @Override
            protected java.text.NumberFormat getFormat(Locale locale) {
                NumberFormat format = super.getFormat(locale);
                format.setGroupingUsed(false);
                return format;
            };
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


    final List<FormProperty<?>> properties;
    final ListenerList<FormProperty<?>> changeListeners = new ListenerList<>(
        MoreExecutors.directExecutor(), th -> {}
    );

    public VaadinForm(List<FormProperty<?>> properties) {
        this.properties = properties;
        this.properties.forEach(p -> p.changeListeners.add(changeListeners::fire));
    }

    private <T> Component createComponentForProperty(FormProperty<T> uip) {
        Class<?> propertyType = uip.getProperty().getType();

        if (propertyType == Void.class) {
            return VaadinBuilders.button()
                .setCaption(uip.getShortName())
                .addClickListener(e -> uip.getProperty().setValue(null))
                .build();
        } else if (propertyType == Boolean.class) {
            return VaadinBuilders.checkBox()
                .setPropertyDataSource(uip.getProperty())
                .setValidationVisible(true)
                .setImmediate(true)
                .build();
        } else if (propertyType == DateTime.class) {
            return VaadinBuilders.dateField()
                .setConverter(dateTimeConverter)
                .setPropertyDataSource(uip.getProperty())
                .setValidationVisible(true)
                .setImmediate(true)
                .build();
        } else {
            if (uip.getAllowedValues() == null) {
                TextField textField = VaadinBuilders.textField()
                    .setPropertyDataSource(uip.getProperty())
                    .setValidationVisible(true)
                    .setImmediate(true)
                    .build();

                if (Number.class.isAssignableFrom(propertyType)) {
                    textField.setNullRepresentation("");
                    if (plainNumberConverters.containsKey(propertyType)) {
                        textField.setConverter(plainNumberConverters.get(propertyType));
                    }
                }

                return textField;
            } else {
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
    }

    ;

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

    public void addChangeListener(Consumer<FormProperty<?>> listener) {
        changeListeners.add(listener);
    }

    public void removeChangeListener(Object key) {
        changeListeners.remove(key);
    }
}
