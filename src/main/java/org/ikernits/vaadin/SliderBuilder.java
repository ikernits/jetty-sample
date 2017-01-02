package org.ikernits.vaadin;

import com.vaadin.ui.Slider;
import com.vaadin.shared.ui.slider.SliderOrientation;

@SuppressWarnings({"deprecation", "unused", "unchecked"})
public class SliderBuilder<T extends Slider, B extends SliderBuilder<T, B>> extends AbstractFieldBuilder<T, B> {

    public SliderBuilder(T delegate) {
        super(delegate);
    }
    
    /**
     * @see com.vaadin.ui.Slider#setValue
     */
    public B setValue(Double value) {
        delegate.setValue(value);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Slider#setResolution
     */
    public B setResolution(int resolution) {
        delegate.setResolution(resolution);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Slider#setMax
     */
    public B setMax(double max) {
        delegate.setMax(max);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Slider#setMin
     */
    public B setMin(double min) {
        delegate.setMin(min);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Slider#setOrientation
     */
    public B setOrientation(SliderOrientation orientation) {
        delegate.setOrientation(orientation);
        return self;
    }
    
}
