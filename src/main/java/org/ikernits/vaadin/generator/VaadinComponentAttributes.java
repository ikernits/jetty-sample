package org.ikernits.vaadin.generator;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractOrderedLayout;

import java.util.function.Consumer;

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

    public interface ComponentAttributes {
        Attribute<? super AbstractComponent> vaWidth100 = new Attribute<>(c -> c.setWidth(100.f, Sizeable.Unit.PERCENTAGE));
        Attribute<? super AbstractComponent> vaHeight100 = new Attribute<>(c -> c.setHeight(100.f, Sizeable.Unit.PERCENTAGE));
        Attribute<? super AbstractComponent> vaSizeFull = new Attribute<>(AbstractComponent::setSizeFull);
        Attribute<? super AbstractComponent> vaReadOnly = new Attribute<>(c -> c.setReadOnly(true));
        Attribute<? super AbstractComponent> vaImmediate = new Attribute<>(c -> c.setImmediate(true));
        Attribute<? super AbstractComponent> vaStyleSmall = new Attribute<>(c -> c.addStyleName("small"));
        Attribute<? super AbstractComponent> vaStyleTiny = new Attribute<>(c -> c.addStyleName("tiny"));
        Attribute<? super AbstractComponent> vaStyleMonospace = new Attribute<>(c -> c.addStyleName("font-monospace"));
    }

    public interface LayoutAttributes {
        Attribute<? super AbstractOrderedLayout> vaMargin = new Attribute<>(c -> c.setMargin(true));
        Attribute<? super AbstractOrderedLayout> vaSpacing = new Attribute<>(c -> c.setSpacing(true));
    }
}
