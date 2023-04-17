package com.github.guikeller.cordova.wearos;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;

import org.apache.cordova.PluginResult;

import java.nio.charset.StandardCharsets;

public class WearOsListener implements MessageClient.OnMessageReceivedListener {

    private static final String TAG = "WearOsPlugin";
    private static final String MESSAGE_PATH = "/cordova/plugin/wearos";

    public WearOsListener(Context context) {
        Log.i(TAG, "constructor");
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if(messageEvent != null && MESSAGE_PATH.equals(messageEvent.getPath())){
            String message = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Log.i(TAG,"onMessageReceived: "+message);
            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, message);
            pluginResult.setKeepCallback(true);
            com.github.guikeller.cordova.wearos.CordovaWearOsPlugin.callbackContext.sendPluginResult(pluginResult);
        }
    }

}
