package com.electric.noinvade.infra;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class InfluxDBProxyFactory implements BeanFactoryPostProcessor {

    private final InfluxMapperProxy proxy = new InfluxMapperProxy();

    public Object newInstance(Class mapperInterface){
        return  Proxy.newProxyInstance(mapperInterface.getClassLoader(),new Class[]{mapperInterface},proxy);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        Reflections reflections =
        new Reflections(new ConfigurationBuilder()
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("com.electric.noinvade.repositry.influx")))
                .setUrls(ClasspathHelper.forPackage("com.electric.noinvade.repositry.influx"))
                .setScanners(new ResourcesScanner(),new SubTypesScanner(), new TypeAnnotationsScanner()));
        Set<String> resources = reflections.getResources(Pattern.compile(".*\\.class"));
        resources.forEach(clazz->{
            Object o = null;
            try {
                o = newInstance(Class.forName(clazz.replace(".class","").replaceAll("\\/",".")));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            configurableListableBeanFactory.registerSingleton(clazz,o);
        });
    }
}
