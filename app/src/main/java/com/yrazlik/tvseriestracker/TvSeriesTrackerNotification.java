package com.yrazlik.tvseriestracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by yrazlik on 25/03/17.
 */

public class TvSeriesTrackerNotification {

    public static final String PARAMETER_IS_PUSH = "isPush";

    public static final String DEEPLINK_HOME_PAGE = "home";
    public static final String DEEPLINK_PLAY_STORE = "market";
    public static final String DEEPLINK_TVSERIESTRACKERAPP = "tvseriestrackerapp";
    public static final String DEEPLIN_SHOW_DETAIL = "showdetail";
    public static final String DEEPLINK_HTTP = "http";
    public static final String DEEPLINK_HTTPS = "https";


    //push notification variables
    public static final String PUSH_NOTIFICATION_TITLE = "title";  //***
    public static final String DEEPLINK_EXTRA = "dl";  //***
    public static final String PUSH_NOTIFICATION_LOCALE = "locale"; //***
    public static final String PUSH_NOTIFICATION_TICKER = "ticker"; //***
    public static final String PUSH_NOTIFICATION_BODY = "body"; //***

    private String title;
    private String locale;
    private String deeplink;
    private String ticker;
    private String body;


    public enum NOTIFICATION_ACTION {
        ACTION_HOME,
        ACTION_PLAY_STORE,
        ACTION_WEB_URL,
        ACTION_SHOW_DETAIL,
        DEEPLINK_TVSERIESTRACKERAPP
    }

    private NOTIFICATION_ACTION notificationAction = NOTIFICATION_ACTION.ACTION_HOME;
    private boolean isPush = false;

    public TvSeriesTrackerNotification(Intent i) {
        createNotification(i);
    }

    public NOTIFICATION_ACTION getNotificationAction() {
        return notificationAction;
    }

    private void createNotification(Intent i) {
        boolean isPush = i.getBooleanExtra(PARAMETER_IS_PUSH, false);
        if(isPush) { // handle push notification (not deeplink)
            handlePushData(i);
        } else { // handle deeplink
            handleDeeplinkData(i);
        }
    }

    private void handlePushData(Intent i) {
        this.isPush = true;

        Bundle extras = i.getExtras();

        if(extras != null) {

            try {
                this.title = (String) extras.get(TvSeriesTrackerNotification.PUSH_NOTIFICATION_TITLE);
            } catch (Exception e) {
                this.title = TvSeriesTrackerApp.getAppContext().getResources().getString(R.string.app_name);
            }

            try {
                this.deeplink = (String) extras.get(TvSeriesTrackerNotification.DEEPLINK_EXTRA);
                Log.d("LolApplication", "Deeplink extra is: " + this.deeplink);
                Uri deeplinkUri = Uri.parse(deeplink);
                this.notificationAction = getNotificationAction(deeplinkUri.getScheme(), deeplinkUri.getHost());
            }catch (Exception ignored) {}

            try {
                this.locale = (String) extras.get(TvSeriesTrackerNotification.PUSH_NOTIFICATION_LOCALE);
            }catch (Exception ignored) {}

            try {
                this.ticker = (String) extras.get(TvSeriesTrackerNotification.PUSH_NOTIFICATION_TICKER);
            }catch (Exception ignored) {}

            try {
                this.body = (String) extras.get(TvSeriesTrackerNotification.PUSH_NOTIFICATION_BODY);
            }catch (Exception ignored) {}
        }
    }

    public String getDeeplink() {
        return deeplink;
    }

    private void handleDeeplinkData(Intent i) {
        isPush = false;
        Uri data = i.getData();
        if(data != null) {
            this.notificationAction = getNotificationAction(data.getScheme(), data.getHost());
        }
    }

    private NOTIFICATION_ACTION getNotificationAction(String deeplinkScheme, String deeplinkHost) {
        if(deeplinkScheme != null) {
            if(deeplinkScheme.equalsIgnoreCase(DEEPLINK_PLAY_STORE)) {
                return NOTIFICATION_ACTION.ACTION_PLAY_STORE;
            } else if(deeplinkScheme.equalsIgnoreCase(DEEPLINK_HTTP) || deeplinkScheme.equalsIgnoreCase(DEEPLINK_HTTPS)) {
                return NOTIFICATION_ACTION.ACTION_WEB_URL;
            } else if(deeplinkScheme.equalsIgnoreCase(DEEPLINK_TVSERIESTRACKERAPP)) {
                if(deeplinkHost != null) {
                    if(deeplinkHost.equalsIgnoreCase(DEEPLINK_HOME_PAGE)) {
                        return NOTIFICATION_ACTION.ACTION_HOME;
                    } else if(deeplinkHost.equalsIgnoreCase(DEEPLIN_SHOW_DETAIL)) {
                        return NOTIFICATION_ACTION.ACTION_SHOW_DETAIL;
                    }
                }
            }
        }
        return NOTIFICATION_ACTION.ACTION_HOME;
    }

    public String getBody() {
        return body;
    }

    public boolean isPush() {
        return isPush;
    }
}
