package org.ikernits.sample.vaadin.components;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vaadin.ui.Notification;
import org.apache.commons.lang3.RandomStringUtils;

@JavaScript({
        "app://resources/js/jquery-2.1.4.min.js",
        "app://resources/js/highcharts.js",
        "app://resources/js/highcharts-exporting.js",
        "app://resources/js/highcharts-connector.js"})
public class HighChart extends AbstractJavaScriptComponent {
    private static final long serialVersionUID = 7738496276049495018L;

    public HighChart() {
        setId(RandomStringUtils.randomAlphanumeric(32));
        getState().domId = getId();
        getState().optionsJson = "";

        addFunction("test2", arguments -> Notification.show("test2: " + arguments.getNumber(0), Notification.Type.TRAY_NOTIFICATION));
    }

    @Override
    public HighChartState getState() {
        return (HighChartState) super.getState();
    }

    public void setOptionsJson(String optionsJson) {
        getState().optionsJson = optionsJson;
        callFunction("test1", "hello1");
        //  callFunction("test2", "hello2");
    }
}
