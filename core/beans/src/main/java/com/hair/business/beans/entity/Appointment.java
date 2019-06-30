package com.hair.business.beans.entity;

import com.googlecode.objectify.annotation.Id;
import com.hair.business.beans.constants.StyleRequestState;

import org.joda.time.DateTime;

/**
 * Created by ejanla on 6/30/19.
 */
public class Appointment {

    @Id
    private Long id;

    private StyleRequestState state;

    private DateTime startTime;
    private DateTime endTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StyleRequestState getState() {
        return state;
    }

    public void setState(StyleRequestState state) {
        this.state = state;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(DateTime endTime) {
        this.endTime = endTime;
    }
}
