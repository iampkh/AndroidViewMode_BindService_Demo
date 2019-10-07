package com.pkh.viewmodel.bindservice.demo;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;


public class MainActivity extends AppCompatActivity {

    BinderViewModel mServiceViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //creation of ViewModelProvider instance, you can create it in many ways. objective is
        //by having the instance, we can start observe the changes in the Response String(ViewModel)
        ViewModelProvider viewModelProvider = new ViewModelProvider(
                new ViewModelStore() ,
                new BinderViewModel.Factory(getApplication()));
        mServiceViewModel = viewModelProvider.get(BinderViewModel.class);

        //code to observe the changes and show toast.
        mServiceViewModel.getResponseMessage().observe(this,
                new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        Toast.makeText(MainActivity.this,"Response from Service ::"+s,Toast.LENGTH_LONG)
                                .show();
                    }
                });


        findViewById(R.id.sendBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(MainActivity.class.getSimpleName(), "pkhDebug btn click");
                mServiceViewModel.sendMessage("Hi Service");
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //we requesting the service to be disconnected,
        //Note:Viewmodel will not destroy, when activity created
        //service will be bound again.
        if(mServiceViewModel != null) mServiceViewModel.unbindService();
    }
}
