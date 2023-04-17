package com.github.guikeller.cordova.wearos;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import org.apache.cordova.LOG;

import java.util.List;

public class WearOsMessageSender {

    private static final String TAG = "WearOsPlugin";
    private static final String MSG_PATH = "/cordova/plugin/wearos";

    private Context context;

    public WearOsMessageSender(Context contextParam){
        super();
        LOG.i(TAG, "constructor");
        context = contextParam;
    }

    public void sendMessage(String msg) throws Exception {
        Task<List<Node>> listTask = Wearable.getNodeClient(context).getConnectedNodes();
        List<Node> nodes = Tasks.await(listTask);
        for(Node node : nodes){
            MessageClient client = Wearable.getMessageClient(context);
            client.sendMessage(node.getId(), MSG_PATH, msg.getBytes());
        }
    }

}