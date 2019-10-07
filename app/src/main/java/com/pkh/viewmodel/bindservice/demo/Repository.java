package com.pkh.viewmodel.bindservice.demo;

import android.app.Application;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class Repository {
    public static final int RESPONSE = 0;
    public static final int  POST = 1;
    public static final String INTENT_KEY_TO_SERVICE = "key-bind-service";

    private Context mApplicationContext;
    private MutableLiveData<String> mResponseMsg ;
    private Intent mBindIntent;
    private Messenger mMessengerFromActivity, mMessengerFromService;

    ServiceConnection mServiceConnecion = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMessengerFromService = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMessengerFromService = null;
        }
    };

    public Repository(Application application) {
        mResponseMsg = new MutableLiveData<>();
        mMessengerFromActivity = new Messenger(new ResponseReceiveHandler());
        mApplicationContext = application.getApplicationContext();

        bindService(mApplicationContext);
    }

    private void bindService(Context applicationContext) {
        if(applicationContext == null || mServiceConnecion == null) return;

        mBindIntent = new Intent(applicationContext,BindingService.class);

        mBindIntent.putExtra(INTENT_KEY_TO_SERVICE,mMessengerFromActivity);
        applicationContext.bindService(mBindIntent, mServiceConnecion, Context.BIND_AUTO_CREATE);

    }
    public void unbindService() {
        if (mApplicationContext != null && mServiceConnecion != null) {
            mApplicationContext.unbindService(mServiceConnecion);
        }

    }
    public void sendMessage(String str) {
        try {
            Message message = new Message();
            message.what = POST;
            message.obj = str;
            Log.d(Repository.class.getSimpleName(), "pkhDebug btn click Repo");
            if(mMessengerFromService != null ) mMessengerFromService.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public LiveData<String> getResponse(){
        return mResponseMsg;
    }

    //Handler to receive message from service.
    class ResponseReceiveHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null && msg.what == RESPONSE) {
                String responseMsg = msg.obj != null ? (String)msg.obj : "";
                mResponseMsg.postValue(responseMsg);
            }
        }
    }
}
