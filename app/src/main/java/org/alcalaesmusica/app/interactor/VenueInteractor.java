package org.alcalaesmusica.app.interactor;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.alcalaesmusica.app.api.Api;
import org.alcalaesmusica.app.base.BaseInteractor;
import org.alcalaesmusica.app.base.BaseView;
import org.alcalaesmusica.app.model.Event;
import org.alcalaesmusica.app.model.Venue;
import org.alcalaesmusica.app.util.Util;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by julio on 14/02/16.
 */
public class VenueInteractor extends BaseInteractor {



    public interface VenuesCallback {

        void onResponse(List<Venue> venues);

        void onError(String error);
    }

    public VenueInteractor(Context context, BaseView baseView) {
        this.baseView = baseView;
        this.context = context;

    }


    public void initializeVenuesFirstTime() {
        Log.i(TAG, "initializeVenuesFirstTime: start");
        String filePath = "venues.json";
        try {

            final String jsonTotal = Util.getStringFromAssets(context, filePath);
            final Gson gson = new GsonBuilder().create();

            Type listType = new TypeToken<List<Venue>>() {
            }.getType();
            final List<Venue> venues = gson.fromJson(jsonTotal, listType);

            storeVenues(venues);

            Log.i(TAG, "initializeVenuesFirstTime: end");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void getVenuePalomaInfoApi(BaseCallback callback) {

        if (!Util.isConnected(context)) {
//            baseView.toast(R.string.no_connection);
            return;
        }

        getApi().getVenuePaloma()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(actionTerminate)
                .subscribe(new Observer<Venue>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(Venue venue) {

//                        baseView.setRefresing(false);

                        Log.i(TAG, "--> venue retrieved: " + venue.toString());

                        // REMEMBER!!!!!!! MUST STORE VENUES AFTER BANDS

                        List<Integer> idsBands = new ArrayList<Integer>();
                        for (Event event : venue.getEvents()) {
                            idsBands.add(event.getBand());
                        }

                        new BandInteractorWorkaround(context, baseView).getBandsByIds(idsBands, new BaseCallback() {
                            @Override
                            public void onSuccess() {

                                List<Venue> venues = new ArrayList<>();
                                venues.add(venue);
                                storeVenues(venues);

                                callback.onSuccess();
                            }

                            @Override
                            public void onError(String error) {
                                callback.onError(error);
                            }
                        });


                    }
                });
    }


    public List<Venue> getVenuesDB() {
        return Realm.getDefaultInstance().where(Venue.class).findAll();
    }

    public void getVenuesApi(final VenuesCallback callback) {

        if (!Util.isConnected(context)) {
//            baseView.toast(R.string.no_connection);
            return;
        }

//        baseView.setRefresing(true);

        getApi().getVenues()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(actionTerminate)
                .subscribe(new Observer<List<Venue>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Venue> venues) {

//                        baseView.setRefresing(false);

                        storeVenues(venues);
                        callback.onResponse(venues);


                    }
                });


    }

    private void storeVenues(List<Venue> venues) {

        for (Venue venue : venues) {
            for (Event event : venue.getEvents()) {
                event.setBandEntity(BandInteractor.getBandDB(event.getBand()));
                event.configureTimeMidnightSafe();
                event.setVenue(venue);
            }
        }
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        realm.where(Venue.class).findAll().deleteAllFromRealm();
        realm.where(Event.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();

        realm.beginTransaction();
        realm.insert(venues);
        realm.commitTransaction();
    }

    public Venue getVenue(int idVenue) {
        return Realm.getDefaultInstance().where(Venue.class).equalTo(Venue.ID, idVenue).findFirst();
    }


    private Api getApi() {
        return getApi(Api.class);
    }


}
