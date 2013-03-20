package com.skplanet.cask.container;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class PostRegister implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(
            ConfigurableListableBeanFactory beanFactory) throws BeansException {

        System.out.println("Post registration bean : Nothing");

        // //System.out.println("count:"+bdf.getBeanDefinitionCount());
        // BeanDefinitionRegistry bdf = (BeanDefinitionRegistry)beanFactory;
        // System.out.println("count:"+bdf.getBeanDefinitionCount());
        // BeanDefinition bd = new RootBeanDefinition(SimpleController.class);
        // //bd.setAttribute("name", "my");
        // bdf.registerBeanDefinition("simpleController", bd);

    }
}
