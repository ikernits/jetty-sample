package org.ikernits.vaadin;

import com.vaadin.ui.Link;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.BorderStyle;

@SuppressWarnings({"deprecation", "unused", "unchecked"})
public class LinkBuilder<T extends Link, B extends LinkBuilder<T, B>> extends AbstractComponentBuilder<T, B> {

    public LinkBuilder(T delegate) {
        super(delegate);
    }
    
    /**
     * @see com.vaadin.ui.Link#setResource
     */
    public B setResource(Resource resource) {
        delegate.setResource(resource);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Link#setTargetBorder
     */
    public B setTargetBorder(BorderStyle targetBorder) {
        delegate.setTargetBorder(targetBorder);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Link#setTargetHeight
     */
    public B setTargetHeight(int targetHeight) {
        delegate.setTargetHeight(targetHeight);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Link#setTargetName
     */
    public B setTargetName(String targetName) {
        delegate.setTargetName(targetName);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Link#setTargetWidth
     */
    public B setTargetWidth(int targetWidth) {
        delegate.setTargetWidth(targetWidth);
        return self;
    }
    
}
