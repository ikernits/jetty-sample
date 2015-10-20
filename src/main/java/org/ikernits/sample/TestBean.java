package org.ikernits.sample;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

/**
 * Created by ikernits on 04/10/15.
 */
public class TestBean implements InitializingBean {

    protected String testProp;

    @Required
    public void setTestProp(String testProp) {
        this.testProp = testProp;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(this.getClass() + " initialized: " + testProp);
    }

    public String getTest() {
        return Thread.currentThread().getName();
    }
}
