package org.alcalaesmusica.app.base;

import android.content.Context;

import org.alcalaesmusica.app.R;
import org.alcalaesmusica.app.api.common.ApiClient;
import org.alcalaesmusica.app.util.Util;

import rx.functions.Action0;

/**
 * Created by julio on 14/02/16.
 */
public class BaseInteractor {

    public final String TAG = this.getClass().getSimpleName();

    public Context context;
    public BaseView baseView;

    public <T> T getApi(Class<T> service) {
        return ApiClient.getInstance().create(service);
    }

    public interface BaseCallback {

        void onSuccess();

        void onError(String error);
    }

    public Action0 actionTerminate = new Action0() {
        @Override
        public void call() {

            if (baseView != null) {
                baseView.setRefresing(false);
                baseView.hideProgressDialog();
            }

        }
    };


    public boolean isConnected() {

        boolean connected = Util.isConnected(context);

        if (!connected) {
            if (baseView != null) {
                baseView.toast(R.string.no_connection);
            }
        }

        return connected;
    }

}
