package com.broadwaybazar.model;

import android.app.Application;

public class InventoryApplication extends Application {

    private static InventoryApplication mInstance;

    public static synchronized InventoryApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}