package org.alcalaesmusica.app.interactor;

import android.content.Context;
import android.util.Log;

import org.alcalaesmusica.app.api.Api;
import org.alcalaesmusica.app.base.BaseInteractor;
import org.alcalaesmusica.app.base.BaseView;
import org.alcalaesmusica.app.model.Band;
import org.alcalaesmusica.app.model.Tag;
import org.alcalaesmusica.app.util.Util;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by julio on 14/02/16.
 */
public class BandInteractorWorkaround extends BaseInteractor {

    List<Band> bandsCache = new ArrayList<>();

    public interface BandCallback {

        void onResponse(int bandId, int index, Band band);

        void onError(String error);
    }

    public interface BandsCallback {

        void onResponse(List<Band> bands);

        void onError(String error);
    }

    public BandInteractorWorkaround(Context context, BaseView baseView) {
        this.baseView = baseView;
        this.context = context;

    }


    public void getBandsByIds(List<Integer> idsBands, final BaseCallback callback) {

        if (!Util.isConnected(context)) {
//            baseView.toast(R.string.no_connection);
            return;
        }

        bandsCache.clear();

        Log.i(TAG, "--> Start retrieving bands");

        getBandById(idsBands.get(0), 0,  new BandCallback() {
            @Override
            public void onResponse(int bandId, int index, Band band) {

                Log.i(TAG, "--> Band retrieved: " + band.toString());

                bandsCache.add(band);
                index++;
                if (index < idsBands.size()) {
                    getBandById(idsBands.get(index), index, this);
                } else {
                    storeBands(bandsCache);
                    bandsCache.clear();
                    Log.i(TAG, "--> bands stored");
                    callback.onSuccess();
                }
            }

            @Override
            public void onError(String error) {

                callback.onError(error);
                bandsCache.clear();
            }
        });


    }

    private void getBandById(Integer idBand, int index, BandCallback callback) {

        getApi().getBand(idBand)
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnTerminate(actionTerminate)
                .subscribe(new Observer<Band>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(Band band) {

                        callback.onResponse(idBand, index, band);


                    }
                });

    }

    private void storeBands(List<Band> bands) {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        realm.where(Band.class).findAll().deleteAllFromRealm();
        realm.where(Tag.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();

        realm.beginTransaction();
        realm.insertOrUpdate(bands);
        realm.commitTransaction();
    }



    private Api getApi() {
        return getApi(Api.class);
    }


}
