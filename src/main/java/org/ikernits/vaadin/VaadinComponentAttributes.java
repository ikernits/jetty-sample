package org.ikernits.vaadin;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class VaadinComponentAttributes {
    private static class Attribute<T> implements Consumer<T> {
        private final Consumer<T> consumer;

        public Attribute(Consumer<T> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void accept(T t) {
            consumer.accept(t);
        }
    }

    public static class ComponentAttributes {
        public static Attribute<Component> vaWidth100 = new Attribute<>(c -> c.setWidth(100.f, Sizeable.Unit.PERCENTAGE));
        public static Attribute<Component> vaHeight100 = new Attribute<>(c -> c.setHeight(100.f, Sizeable.Unit.PERCENTAGE));
        public static Attribute<AbstractComponent> vaSizeFull = new Attribute<>(AbstractComponent::setSizeFull);
        public static Attribute<Component> vaReadOnly = new Attribute<>(c -> c.setReadOnly(true));
        public static Attribute<AbstractComponent> vaImmediate = new Attribute<>(c -> c.setImmediate(true));
        public static Attribute<Component> vaStyleSmall = new Attribute<>(c -> c.addStyleName("small"));
        public static Attribute<Component> vaStyleTiny = new Attribute<>(c -> c.addStyleName("tiny"));
        public static Attribute<Component> vaStyleMarginNormal = new Attribute<>(c -> c.addStyleName("margin-normal"));
        public static Attribute<Component> vaStyleMarginSmall = new Attribute<>(c -> c.addStyleName("margin-small"));
        public static Attribute<Component> vaStyleMarginTiny = new Attribute<>(c -> c.addStyleName("margin-tiny"));
        public static Attribute<Component> vaStylePaddingNormal = new Attribute<>(c -> c.addStyleName("padding-normal"));
        public static Attribute<Component> vaStylePaddingSmall = new Attribute<>(c -> c.addStyleName("padding-small"));
        public static Attribute<Component> vaStylePaddingTiny = new Attribute<>(c -> c.addStyleName("padding-tiny"));
        public static Attribute<Component> vaStyleScrollable = new Attribute<>(c -> c.addStyleName("v-scrollable"));
        public static Attribute<Component> vaStyleMonospace = new Attribute<>(c -> c.addStyleName("font-monospace"));


        private static Map<Side, Map<Size, Attribute<Component>>> paddingAttributeMap =
            Arrays.stream(Side.values()).collect(
                Collectors.toMap(side -> side,
                    side -> Arrays.stream(Size.values()).collect(
                        Collectors.toMap(
                            size -> size,
                            size -> new Attribute<>(c -> c.addStyleName("padding-" + side.name().toLowerCase() + "-" + size.name().toLowerCase()))
                        )
                    ))
            );

        private static Map<Side, Map<Size, Attribute<Component>>> marginAttributeMap =
            Arrays.stream(Side.values()).collect(
                Collectors.toMap(side -> side,
                    side -> Arrays.stream(Size.values()).collect(
                        Collectors.toMap(
                            size -> size,
                            size -> new Attribute<>(c -> c.addStyleName("margin-" + side.name().toLowerCase() + "-" + size.name().toLowerCase()))
                        )
                    ))
            );
        
        public enum Side {
            Top, Left, Bottom, Right
        }

        public enum Size {
            Normal, Small, Tiny
        }

        public static Attribute<Component> vaStyleMargin(Side side, Size size) {
            return marginAttributeMap.get(side).get(size);
        }

        public static Attribute<Component> vaStylePadding(Side side, Size size) {
            return paddingAttributeMap.get(side).get(size);
        }
    }

    public interface LayoutAttributes {
        Attribute<AbstractOrderedLayout> vaMargin = new Attribute<>(c -> c.setMargin(true));
        Attribute<AbstractOrderedLayout> vaSpacing = new Attribute<>(c -> c.setSpacing(true));
    }
}
