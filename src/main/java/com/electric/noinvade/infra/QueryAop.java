package com.electric.noinvade.infra;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Properties;

@Aspect
@Component
public class QueryAop {

    private InfluxDB influxDB;

    @PostConstruct
    public  void init() throws IOException {
        Properties prop = new Properties();

        InputStream in = this.getClass().getClassLoader().getResourceAsStream(
                "influxdb.properties");
        prop.load(in);
        influxDB = InfluxDBFactory.connect(prop.getProperty("url"));
        influxDB.setDatabase(prop.getProperty("db"));
    }

    @Pointcut("within(com.electric.noinvade.repositry.*)")
    public void pointcut(){}

    @Pointcut("execution(public * *(..))")
    public void operation(){}

    @Around(value="pointcut()&&operation()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable{
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        InfluxQuery annotation = method.getAnnotation(InfluxQuery.class);
        if(annotation!=null){
            QueryResult queryResult = influxDB.query(new Query(annotation.value()));
            InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
            if(method.getReturnType() == java.util.List.class) {
                // 如果是List类型，得到其Generic的类型
                Type genericType = method.getGenericReturnType();
                // 如果是泛型参数的类型
                if (genericType instanceof ParameterizedType) {
                    ParameterizedType pt = (ParameterizedType) genericType;
                    //得到泛型里的class类型对象
                    Class<?> accountPrincipalApproveClazz = (Class<?>) pt.getActualTypeArguments()[0];
                    List<?> result = resultMapper.toPOJO(queryResult, accountPrincipalApproveClazz);
                    return result;
                }
            }else {
                List<?> result = resultMapper.toPOJO(queryResult, method.getDeclaringClass());
                return result;
            }
        }
        return null;
    }
}
