package com.electric.noinvade;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
//		InfluxDB influxDB = InfluxDBFactory.connect("http://10.9.8.88:8086");
//		String dbName = "nilm";
//		influxDB.setDatabase(dbName);
//		QueryResult queryResult = influxDB.query(new Query("select * from raw_aggr_10s where time >now() -100s"));
//		InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
//		List<Gl> gl =  resultMapper.toPOJO(queryResult, Gl.class);
//		gl.forEach(c -> System.out.println(c));

	}
	
}
