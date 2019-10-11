package com.electric.noinvade.util;

public enum DeviceStatusEnum{

    OPEN(0,"开启"),
    CLOSED(1,"关闭");

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
}
