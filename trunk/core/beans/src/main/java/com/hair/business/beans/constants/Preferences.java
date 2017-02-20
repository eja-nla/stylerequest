package com.hair.business.beans.constants;

import com.hair.business.beans.abstracts.AbstractBean;

/**
 * Created by olukoredeaguda on 17/02/2017.
 *
 *
 */
public class Preferences extends AbstractBean {

    private boolean placedNotificationEnabled;
    private boolean acceptedNotificationEnabled;
    private boolean cancelledNotificationEnabled;
    private boolean completedNotificationEnabled;

    public Preferences(){}

    public Preferences(boolean placedNotificationEnabled, boolean acceptedNotificationEnabled, boolean cancelledNotificationEnabled, boolean completedNotificationEnabled) {
        this.placedNotificationEnabled = placedNotificationEnabled;
        this.acceptedNotificationEnabled = acceptedNotificationEnabled;
        this.cancelledNotificationEnabled = cancelledNotificationEnabled;
        this.completedNotificationEnabled = completedNotificationEnabled;
    }

    public boolean isPlacedNotificationEnabled() {
        return placedNotificationEnabled;
    }

    public void setPlacedNotificationEnabled(boolean placedNotificationEnabled) {
        this.placedNotificationEnabled = placedNotificationEnabled;
    }

    public boolean isAcceptedNotificationEnabled() {
        return acceptedNotificationEnabled;
    }

    public void setAcceptedNotificationEnabled(boolean acceptedNotificationEnabled) {
        this.acceptedNotificationEnabled = acceptedNotificationEnabled;
    }

    public boolean isCancelledNotificationEnabled() {
        return cancelledNotificationEnabled;
    }

    public void setCancelledNotificationEnabled(boolean cancelledNotificationEnabled) {
        this.cancelledNotificationEnabled = cancelledNotificationEnabled;
    }

    public boolean isCompletedNotificationEnabled() {
        return completedNotificationEnabled;
    }

    public void setCompletedNotificationEnabled(boolean completedNotificationEnabled) {
        this.completedNotificationEnabled = completedNotificationEnabled;
    }
}
