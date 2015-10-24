package org.ikernits.vaadin;

import com.vaadin.ui.AbstractComponent;

import java.util.Arrays;
import java.util.function.Consumer;

public class ComponentBuilder<T extends AbstractComponent, B extends ComponentBuilder<T, B>> {
    protected T delegate;
    protected B self = (B)this;

    public ComponentBuilder(T delegate) {
        this.delegate = delegate;
    }

    @SafeVarargs
    public final B setAttributes(Consumer<? super T>... attributes) {
        Arrays.stream(attributes).forEach(c -> c.accept(delegate));
        return self;
    }

    public T build() {
        T result = delegate;
        delegate = null;
        return result;
    }
}
