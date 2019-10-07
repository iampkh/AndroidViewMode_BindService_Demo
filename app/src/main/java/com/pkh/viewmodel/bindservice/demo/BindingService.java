package com.pkh.viewmodel.bindservice.demo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class BindingService extends Service {
    Messenger mMessengerFromActivity,mMessengerFromService;



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d(BindingService.class.getSimpleName(), "pkhDebug Service is Bounded to activity.");
        mMessengerFromActivity = intent.getParcelableExtra(Repository.INTENT_KEY_TO_SERVICE);

        mMessengerFromService = new Messenger(new ReceiverHandler());
        return mMessengerFromService.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(BindingService.class.getSimpleName(), "pkhDebug Service is unbounded from activity.");
        stopSelf();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(BindingService.class.getSimpleName(), "pkhDebug Service is destroyed .");
    }

    class ReceiverHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg != null && msg.what == Repository.POST){
                Toast.makeText(BindingService.this,"Post from Activity  :"+(String)msg.obj,Toast.LENGTH_LONG)
                        .show();

                //though the service is running in MainThread, for timing purpose using
                //handler.postDelayed.
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        sendResponseToActivity("Thanks for the msg");
                    }
                },2000);
               //
            }
        }

        private void sendResponseToActivity(String str) {
            if (mMessengerFromActivity == null) return;
            try {

                Message msg = new Message();
                msg.what = Repository.RESPONSE;
                msg.obj = str;
                mMessengerFromActivity.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
