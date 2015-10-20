package org.ikernits.sample.mvc;

import org.apache.log4j.Logger;
import org.ikernits.sample.TestBean;
import org.joda.time.Instant;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ikernits on 13/10/15.
 */
public class TestController extends AbstractController implements InitializingBean {

    protected TestBean testBean;
    protected String defaultProperty;
    protected String localProperty;
    protected String configProperty;

    @Required
    public void setDefaultProperty(String defaultProperty) {
        this.defaultProperty = defaultProperty;
    }

    public void setLocalProperty(String localProperty) {
        this.localProperty = localProperty;
    }

    public void setConfigProperty(String configProperty) {
        this.configProperty = configProperty;
    }

    @Required
    public void setTestBean(TestBean testBean) {
        this.testBean = testBean;
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return GsonView.createModelAndView(new Output(
                testBean.getTest(),
                defaultProperty,
                localProperty,
                configProperty
        ));
    }

    protected static class Output {
        protected Instant time = new Instant();
        protected String test;
        protected String defaultP;
        protected String localP;
        protected String configP;

        public Output(String test, String defaultP, String localP, String configP) {
            this.test = test;
            this.defaultP = defaultP;
            this.localP = localP;
            this.configP = configP;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Logger.getLogger(TestController.class).info("initializaiton done: " + defaultProperty);
    }
}

