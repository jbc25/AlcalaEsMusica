package org.alcalaesmusica.app.interactor;

import android.content.Context;

import com.google.firebase.crash.FirebaseCrash;
import org.alcalaesmusica.app.api.Api;
import org.alcalaesmusica.app.base.BaseInteractor;
import org.alcalaesmusica.app.base.BaseView;
import org.alcalaesmusica.app.util.Util;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by julio on 14/02/16.
 */
public class SettingsInteractor extends BaseInteractor {



    public interface SettingsVersionCallback {

        void onResponse(Integer version);

        void onError(String error);
    }

    public SettingsInteractor(Context context, BaseView baseView) {
        this.baseView = baseView;
        this.context = context;

    }

    public void getAppVersionInMarket(final SettingsVersionCallback callback) {

        if (!Util.isConnected(context)) {
            return;
        }

        getApi().getAppVersionInMarket()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(actionTerminate)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(String version) {

                        try {
                            int versionInt = Integer.parseInt(version);
                            callback.onResponse(versionInt);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            FirebaseCrash.report(e);
                        }


                    }
                });


    }

    public void getDataVersion(final SettingsVersionCallback callback) {

        if (!Util.isConnected(context)) {
            return;
        }

        getApi().getDataVersion()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(actionTerminate)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(String version) {

                        try {
                            int versionInt = Integer.parseInt(version);
                            callback.onResponse(versionInt);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            FirebaseCrash.report(e);
                        }


                    }
                });


    }

    private Api getApi() {
        return getApi(Api.class);
    }


}
