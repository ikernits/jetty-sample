package org.ikernits.vaadin;

import com.vaadin.ui.Window;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.shared.ui.window.WindowRole;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.Window.ResizeListener;
import com.vaadin.ui.Window.WindowModeChangeListener;

@SuppressWarnings({"deprecation", "unused", "unchecked"})
public class WindowBuilder<T extends Window, B extends WindowBuilder<T, B>> extends PanelBuilder<T, B> {

    public WindowBuilder(T delegate) {
        super(delegate);
    }
    
    /**
     * @see com.vaadin.ui.Window#setParent
     */
    public B setParent(HasComponents parent) {
        delegate.setParent(parent);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Window#setPosition
     */
    public B setPosition(int param1, int param2) {
        delegate.setPosition(param1, param2);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Window#addBlurListener
     */
    public B addBlurListener(BlurListener blurListener) {
        delegate.addBlurListener(blurListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Window#addFocusListener
     */
    public B addFocusListener(FocusListener focusListener) {
        delegate.addFocusListener(focusListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Window#setClosable
     */
    public B setClosable(boolean closable) {
        delegate.setClosable(closable);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Window#addListener
     */
    public B addListener(CloseListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Window#addListener
     */
    public B addListener(BlurListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Window#addListener
     */
    public B addListener(ResizeListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Window#addListener
     */
    public B addListener(FocusListener listener) {
        delegate.addListener(listener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Window#setPositionX
     */
    public B setPositionX(int positionX) {
        delegate.setPositionX(positionX);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Window#setPositionY
     */
    public B setPositionY(int positionY) {
        delegate.setPositionY(positionY);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Window#addCloseListener
     */
    public B addCloseListener(CloseListener closeListener) {
        delegate.addCloseListener(closeListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Window#addWindowModeChangeListener
     */
    public B addWindowModeChangeListener(WindowModeChangeListener windowModeChangeListener) {
        delegate.addWindowModeChangeListener(windowModeChangeListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Window#addResizeListener
     */
    public B addResizeListener(ResizeListener resizeListener) {
        delegate.addResizeListener(resizeListener);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Window#setModal
     */
    public B setModal(boolean modal) {
        delegate.setModal(modal);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Window#setResizable
     */
    public B setResizable(boolean resizable) {
        delegate.setResizable(resizable);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Window#setResizeLazy
     */
    public B setResizeLazy(boolean resizeLazy) {
        delegate.setResizeLazy(resizeLazy);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Window#setDraggable
     */
    public B setDraggable(boolean draggable) {
        delegate.setDraggable(draggable);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Window#setWindowMode
     */
    public B setWindowMode(WindowMode windowMode) {
        delegate.setWindowMode(windowMode);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Window#setAssistivePrefix
     */
    public B setAssistivePrefix(String assistivePrefix) {
        delegate.setAssistivePrefix(assistivePrefix);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Window#setAssistivePostfix
     */
    public B setAssistivePostfix(String assistivePostfix) {
        delegate.setAssistivePostfix(assistivePostfix);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Window#setAssistiveRole
     */
    public B setAssistiveRole(WindowRole assistiveRole) {
        delegate.setAssistiveRole(assistiveRole);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Window#setTabStopEnabled
     */
    public B setTabStopEnabled(boolean tabStopEnabled) {
        delegate.setTabStopEnabled(tabStopEnabled);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Window#setTabStopTopAssistiveText
     */
    public B setTabStopTopAssistiveText(String tabStopTopAssistiveText) {
        delegate.setTabStopTopAssistiveText(tabStopTopAssistiveText);
        return self;
    }
    
    /**
     * @see com.vaadin.ui.Window#setTabStopBottomAssistiveText
     */
    public B setTabStopBottomAssistiveText(String tabStopBottomAssistiveText) {
        delegate.setTabStopBottomAssistiveText(tabStopBottomAssistiveText);
        return self;
    }
    
}
