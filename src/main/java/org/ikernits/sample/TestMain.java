package org.ikernits.sample;

import org.apache.log4j.Logger;
import org.ikernits.sample.log.Log4jConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;

/**
 * Created by ikernits on 11/10/15.
 */
public class TestMain implements BeanFactoryPostProcessor {
    public static void main(String[] args) {
        Log4jConfigurer.configureIfRequired();
        ClassPathXmlApplicationContext c1 = new ClassPathXmlApplicationContext(
                "classpath:/META-INF/spring-context-main.xml"
        );
        ClassPathXmlApplicationContext c2 = new ClassPathXmlApplicationContext(
                new String[]{ "classpath:/WEB-INF/web-context.xml" }, true, c1
        );


        Logger.getLogger(TestMain.class).info("info record");
        Logger.getLogger(TestMain.class).error("error record");
        Logger.getLogger("control").info("control record");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Logger.getLogger(TestMain.class).info("post process: " + Arrays.asList(beanFactory.getBeanDefinitionNames()));
    }
}
