package org.ikernits.vaadin.shared;

public interface VaadinPropertyContainer {
    default void initialize() {
        update();
    }
    void update();
}
