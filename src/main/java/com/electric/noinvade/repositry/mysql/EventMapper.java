package com.electric.noinvade.repositry.mysql;

import com.electric.noinvade.bo.Event;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface EventMapper {

    @Select("SELECT * FROM event where account=#{familyID}")
    @Results({
            @Result(property = "id", column = "account"),
            @Result(property = "alarmType", column = "alarm_type"),
            @Result(property = "deviceStatus", column = "device_status"),
            @Result(property = "deviceType", column = "device_type"),
            @Result(property = "time", column = "time_stamp")}
    )
    List<Event> getEvent(String familyID);

    @Select("SELECT * FROM event where alarm_type=1 order by time_stamp asc")
    @Results({
            @Result(property = "id", column = "account"),
            @Result(property = "alarmType", column = "alarm_type"),
            @Result(property = "deviceStatus", column = "device_status"),
            @Result(property = "deviceType", column = "device_type"),
            @Result(property = "time", column = "time_stamp")}
    )
    List<Event> getAllEvent();

    @Insert("INSERT INTO event(account,alarm_type,device_status,device_type,time_stamp,power) " +
            "VALUES(#{familyID}, #{alarmType}, #{deviceStatus},#{deviceType},#{time},#{power})")
    void insert(Event event);

    @Update("update  event_timestamp set time_stamp=#{time} where id=1")
    void updateTimestamp(long time);

    @Select("select time_stamp from event_timestamp  where id=1")
    long getTimestamp();

}
