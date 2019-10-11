package com.electric.noinvade.repositry.mysql;

import com.electric.noinvade.bo.DeviceAuth;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DeviceAuthMapper {

    @Select("SELECT * FROM device_auth where account=#{familyID} and auth_time<=time order by auth_time desc limit 1")
    @Results({
            @Result(property = "type", column = "device_type"),
            @Result(property = "status", column = "device_auth_status"),
            @Result(property = "familyID", column = "account")}

    )
    List<DeviceAuth> getAuthByFamilyID(String familyID,long time);
}
