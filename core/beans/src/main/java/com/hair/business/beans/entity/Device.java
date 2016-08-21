package com.hair.business.beans.entity;


import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.hair.business.beans.abstracts.AbstractActorEnablerEntity;
import com.hair.business.beans.constants.DeviceType;

/**
 * Created by Olukorede Aguda on 22/06/2016.
 *
 * Represents the device of a customer/merchant
 */
@Entity
@Cache
public class Device extends AbstractActorEnablerEntity {

    @Id
    private Long id;
    private String deviceId;
    private String os;
    private DeviceType deviceType;

    public Device() {
    }

    public Device(String deviceId, String os, DeviceType deviceType) {
        this();
        this.deviceId = deviceId;
        this.os = os;
        this.deviceType = deviceType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

