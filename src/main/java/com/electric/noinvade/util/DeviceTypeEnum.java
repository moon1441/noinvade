package com.electric.noinvade.util;

public enum  DeviceTypeEnum  {

    FRIDGE(1,"冰箱"),
    WASHER(2,"洗衣机"),
    AIR_CONDITIONER(3,"空调"),
    RICE_COOKER(4,"电饭煲"),
    MICROWAVE(5,"微波炉"),
    HOT_WATER_BOTTLE(6,"热水器"),
    UNDEFINED(7,"未知");

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


    DeviceTypeEnum(int code, String name) {
        this.name=name;
        this.code=code;
    }

    public static String getName(int code){
        for(DeviceTypeEnum deviceTypeEnum : DeviceTypeEnum.values()){
            if(code == deviceTypeEnum.code){
                return deviceTypeEnum.name;
            }
        }
        return null;
    }
}
