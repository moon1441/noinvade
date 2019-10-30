package com.electric.noinvade.infra;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class InfluxMapperProxy<T> implements InvocationHandler, Serializable {

    private InfluxDB influxDB;
    private String database;

    {
        Properties pp = System.getProperties();
        String influxUser = pp.getProperty("influxUser");
        String influxPsw = pp.getProperty("influxPsw");
        if(!StringUtils.isEmpty(influxUser) && !StringUtils.isEmpty(influxPsw)) {
            influxDB = InfluxDBFactory.connect(pp.getProperty("influxUrl"), influxUser, influxPsw);
        }else{
            influxDB =  InfluxDBFactory.connect(pp.getProperty("influxUrl"));
        }
        influxDB.setDatabase(pp.getProperty("influxDBName"));
        database=pp.getProperty("influxDBName");
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
            QueryResult queryResult = influxDB.query(new Query(sql,database), TimeUnit.MILLISECONDS);
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
