package com.electric.noinvade.repositry.mysql;

import com.electric.noinvade.bo.Building;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BuildingMapper {

    @Select("SELECT * FROM building where meter_id=#{meterID} and phase=#{phase}")
    @Results({
            @Result(property = "meterID", column = "meter_id"),
            @Result(property = "id", column = "account")}
    )
    Building getBuildingByMeter(String meterID, String phase);

    @Select("SELECT * FROM building where account=#{id}")
    @Results({
            @Result(property = "meterID", column = "meter_id"),
            @Result(property = "id", column = "account")}

    )
    Building getBuildingByID(String id);

    @Select("SELECT * FROM building")
    @Results({
            @Result(property = "meterID", column = "meter_id"),
            @Result(property = "id", column = "account")}
    )
    List<Building> getAllBuildings();

}
