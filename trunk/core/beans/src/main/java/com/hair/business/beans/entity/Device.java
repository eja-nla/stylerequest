package com.hair.business.beans.entity;


import com.hair.business.beans.abstracts.AbstractActorEnablerEntity;
import com.hair.business.beans.constants.DeviceType;

/**
 * Created by Olukorede Aguda on 22/06/2016.
 *
 * Represents the device of a customer/merchant
 */
public class Device extends AbstractActorEnablerEntity {

    private String deviceId;
    private String os;
    private DeviceType deviceType;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }
}

