package com.electric.noinvade.repositry.mysql;

import com.electric.noinvade.bo.Building;
import com.electric.noinvade.bo.Event;
import org.apache.ibatis.annotations.*;

public interface EventMapper {

    @Select("SELECT * FROM event where account=#{buildingID}")
    @Results({
            @Result(property = "id", column = "account"),
            @Result(property = "alarmType", column = "alarm_type"),
            @Result(property = "deviceStatus", column = "device_status"),
            @Result(property = "deviceType", column = "device_type"),
            @Result(property = "time", column = "time_stamp")}
    )
    Event getEvent(String buildingID);

    @Insert("INSERT INTO event(account,alarm_type,device_status,device_type,time_stamp) " +
            "VALUES(#{id}, #{alarmType}, #{deviceStatus},#{deviceType},#{time})")
    void insert(Event event);

    @Update("update  event_timestamp set time_stamp=#{time} where id=1")
    void updateTimestamp(long time);

    @Select("select time_stamp from event_timestamp  where id=1")
    long getTimestamp(long time);

}
