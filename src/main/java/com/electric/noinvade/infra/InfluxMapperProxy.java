package com.electric.noinvade.infra;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Properties;

public class InfluxMapperProxy<T> implements InvocationHandler, Serializable {

    private InfluxDB influxDB;

    {
        Properties prop = new Properties();
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(
                "influxdb.properties");
        try {
            prop.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        influxDB = InfluxDBFactory.connect(prop.getProperty("url"));
        influxDB.setDatabase(prop.getProperty("db"));
    }
    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        InfluxQuery annotation = method.getAnnotation(InfluxQuery.class);
        if(annotation!=null){
            String sql =  annotation.value();
            int paramIndex=0;
            while(sql.indexOf("?")>0){
                sql=sql.replaceFirst("\\?",String.valueOf(args[paramIndex]));
                paramIndex++;
            }
            QueryResult queryResult = influxDB.query(new Query(sql));
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
                List<?> result = resultMapper.toPOJO(queryResult, method.getReturnType());
                return result;
            }
        }
        return null;
    }
}
