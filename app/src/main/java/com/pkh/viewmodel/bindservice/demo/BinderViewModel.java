package com.pkh.viewmodel.bindservice.demo;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class BinderViewModel extends AndroidViewModel {


    Repository mRepo;
    LiveData<String> mResponseMsg;
    public BinderViewModel(Application application) {
        super(application);
        mRepo = new Repository(application);
        mResponseMsg = mRepo.getResponse();
    }

    /**
     * sending message to service.
     * @param str
     */
    public void sendMessage(String str) {
        Log.d(BinderViewModel.class.getSimpleName(), "pkhDebug btn click viewmodel");
        if (mRepo != null) mRepo.sendMessage(str);
    }

    /**
     * Method to return the updated message from the binded service.
     * @return
     */
    public LiveData<String> getResponseMessage() {
        return mResponseMsg;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        unbindService();
    }

    public void unbindService() {
        if (mRepo != null) mRepo.unbindService();
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        public Factory(@NonNull Application application) {
            mApplication = application;

        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new BinderViewModel(mApplication);
        }
    }
}
