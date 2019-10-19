package com.electric.noinvade.util;

public enum DeviceStatusEnum{

    OPEN(1,"开机"),
    CLOSED(0,"关机/待机");

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


    DeviceStatusEnum(int code, String name) {
        this.name=name;
        this.code=code;
    }

    public static String getName(int code){
        for(DeviceStatusEnum deviceTypeEnum : DeviceStatusEnum.values()){
            if(code == deviceTypeEnum.code){
                return deviceTypeEnum.name;
            }
        }
        return null;
    }

    public static int getCode(String name){
        for(DeviceStatusEnum deviceTypeEnum : DeviceStatusEnum.values()){
            if(name == deviceTypeEnum.name){
                return deviceTypeEnum.code;
            }
        }
        return 0;
    }
}
