package com.hair.business.beans.constants;

import com.hair.business.beans.abstracts.AbstractBean;

/**
 * Created by olukoredeaguda on 17/02/2017.
 *
 *
 */
public class Preferences extends AbstractBean {

    private boolean placedNotificationEnabled = true;
    private boolean acceptedNotificationEnabled = true;
    private boolean cancelledNotificationEnabled = true;
    private boolean completedNotificationEnabled = true;

    public String getApnsNotificationType() {
        return apnsNotificationType;
    }

    public void setApnsNotificationType(String apnsNotificationType) {
        this.apnsNotificationType = apnsNotificationType;
    }

    private String apnsNotificationType;

    public Preferences(){}

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
