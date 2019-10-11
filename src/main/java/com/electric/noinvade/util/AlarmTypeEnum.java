package com.electric.noinvade.util;

public enum AlarmTypeEnum {

    NORMAL(0,"正常"),
    EXCEPTION(1,"异常");

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


    AlarmTypeEnum(int code, String name) {
        this.name=name;
        this.code=code;
    }

    public static String getName(int code){
        for(AlarmTypeEnum deviceTypeEnum : AlarmTypeEnum.values()){
            if(code == deviceTypeEnum.code){
                return deviceTypeEnum.name;
            }
        }
        return null;
    }
}
