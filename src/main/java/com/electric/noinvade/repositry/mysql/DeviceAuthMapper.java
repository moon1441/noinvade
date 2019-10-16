package com.electric.noinvade.repositry.mysql;

import com.electric.noinvade.bo.DeviceAuth;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface DeviceAuthMapper {

    @Select("SELECT * FROM device_auth where account=#{familyID}")
    @Results({
            @Result(property = "type", column = "device_type"),
            @Result(property = "status", column = "device_auth_status"),
            @Result(property = "familyID", column = "account")}

    )
    List<DeviceAuth> getAuthByFamilyID(String familyID);

    @Select("SELECT * FROM device_auth_record where account=#{familyID} and device_type=#{type} and auth_time<#{time} order by auth_time desc limit 1")
    @Results({
            @Result(property = "type", column = "device_type"),
            @Result(property = "status", column = "device_auth_status"),
            @Result(property = "familyID", column = "account")}

    )
    DeviceAuth getAuthSnapshotByTime(String familyID,int type,long time );

    @Insert("INSERT INTO device_auth_record(account,device_status,device_type,auth_time)"+
            "VALUES(#{familyID}, #{deviceStatus}, #{deviceType},#{time})")
    void insertSnapshot(String familyID,int deviceType,int deviceStatus,long time);

    @Update("Update device_auth set device_status=#{status},update_time=now()"+
            "where account=#{familyID} and device_type=#{type}")
    int update(DeviceAuth deviceAuth);

    @Insert("INSERT INTO device_auth(account,device_status,device_type)"+
            "VALUES(#{familyID}, #{status}, #{type})")
    int insert(DeviceAuth deviceAuth);
}
