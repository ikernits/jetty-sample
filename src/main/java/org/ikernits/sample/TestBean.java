package org.ikernits.sample;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by ikernits on 04/10/15.
 */
public class TestBean implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(this.getClass() + " initialized");
    }
}
