package com.electric.noinvade.repositry;

import com.electric.noinvade.bo.Power;
import com.electric.noinvade.infra.InfluxQuery;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PowerRepository {

    @InfluxQuery("select * from raw_aggr_10s where time >now() -100s")
    public List<Power>  getPower(){return null;}
}
