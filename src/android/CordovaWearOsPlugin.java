package com.github.guikeller.cordova.wearos;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.wearable.Wearable;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

public class CordovaWearOsPlugin extends CordovaPlugin {

    private static final String TAG = "WearOsPlugin";

    private static CordovaInterface cordovaInterface;
    public static CallbackContext callbackContext;

    private WearOsListener messageListener;


    public CordovaWearOsPlugin(){
        super();
        Log.i(TAG, "constructor");
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        Log.i(TAG, "initialize");
        super.initialize(cordova, webView);
        cordovaInterface = cordova;
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Log.i(TAG, "execute");
        CordovaWearOsPluginAction pluginAction = CordovaWearOsPluginAction.fromValue(action);
        switch (pluginAction){
            case INIT:
                init(callbackContext);
                break;
            case SHUTDOWN:
                shutdown(callbackContext);
                break;
            case SEND_MESSAGE:
                sendMessage(args, callbackContext);
                break;
            case REGISTER_MESSAGE_LISTENER:
                registerMessageListener(callbackContext);
                break;
            default:
                return false;
        }
        return true;
    }

    protected void init(CallbackContext callbackContext){
        Log.i(TAG,"init");
        callbackContext.success();
    }

    protected void shutdown(CallbackContext callbackContextParam) {
        Log.i(TAG,"shutdown");
        messageListener = null;
        callbackContext = null;
        callbackContextParam.success();
    }

    protected void registerMessageListener(CallbackContext callbackContextParam) {
        callbackContext = callbackContextParam;
        Activity context = cordovaInterface.getActivity();
        listenToMessages(context);
        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK);
        pluginResult.setKeepCallback(true);
        callbackContext.sendPluginResult(pluginResult);
        Log.i(TAG,"registerMessageListener OK");
    }

    protected void sendMessage(JSONArray args, CallbackContext callbackContext){
        try {
            Log.i(TAG,"sendMessage :: args: "+args);
            if (args != null) {
                Activity context = cordovaInterface.getActivity();
                WearOsMessageSender sender = new WearOsMessageSender(context);
                String msg = args.getString(0);
                sender.sendMessage(msg);
                callbackContext.success();
            }
        } catch (Exception ex){
            callbackContext.error("Not able to send message: " + ex.getMessage());
        }
    }

    protected void listenToMessages(Context context) {
        if (this.messageListener == null) {
            this.messageListener = new WearOsListener(context);
            Wearable.getMessageClient(context).addListener(this.messageListener);
            Log.i(TAG,"Listener added successfully...");
        }
    }

}
