package com.electric.noinvade.util;

public enum DeviceAuthStatusEnum {

    AUTH(0,"授权"),
    UN_AUTH(1,"未授权");

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    private String name;
    private int code;


    DeviceAuthStatusEnum(int code, String name) {
        this.name=name;
        this.code=code;
    }

    public static String getName(int code){
        for(DeviceAuthStatusEnum deviceTypeEnum : DeviceAuthStatusEnum.values()){
            if(code == deviceTypeEnum.code){
                return deviceTypeEnum.name;
            }
        }
        return null;
    }
    public static int getCode(String name){
        for(DeviceAuthStatusEnum deviceTypeEnum : DeviceAuthStatusEnum.values()){
            if(name == deviceTypeEnum.name){
                return deviceTypeEnum.code;
            }
        }
        return 0;
    }
}
