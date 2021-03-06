package org.alcalaesmusica.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.alcalaesmusica.app.interactor.BandInteractor;
import org.alcalaesmusica.app.interactor.EventInteractor;
import org.alcalaesmusica.app.interactor.SettingsInteractor;
import org.alcalaesmusica.app.interactor.VenueInteractor;
import org.alcalaesmusica.app.model.Band;
import org.alcalaesmusica.app.model.Venue;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by julio on 17/06/16.
 */

public class App extends Application {

    private static final String TAG = "App";

    public static final String PREFIX = "org.alcalaesmusica.app.";

    public static final String SHARED_FIRST_TIME = PREFIX + "first_time_7";
    public static final String SHARED_DATA_VERSION = PREFIX + "shared_data_version";

    public static final String EXTRA_FIRST_TIME_APP_LAUNCHING = PREFIX + "extra_first_time_app_lauching";
    public static final String ACTION_REFRESH_DATA = PREFIX + "action_refresh_data";
    public static final String ACTION_SHOW_NOTIFICATION = PREFIX + "action_show_notification";
    private VenueInteractor venueInteractor;
    private BandInteractor bandInteractor;
    private SettingsInteractor settingsInteractor;


    @Override
    public void onCreate() {
        super.onCreate();

        configureRealm();

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
//        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Exo-Regular.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        bandInteractor = new BandInteractor(this, null);
        venueInteractor = new VenueInteractor(this, null);
        settingsInteractor = new SettingsInteractor(this, null);

//        initializeDataFirstTime();

//        String token = FirebaseInstanceId.getInstance().getToken();
//        Log.d(TAG, "Refreshed token: " + token);


    }

    private void initializeDataFirstTime() {

        if (getPrefs(this).getBoolean(EXTRA_FIRST_TIME_APP_LAUNCHING, true)) {
            bandInteractor.initializeBandsFirstTime();
            venueInteractor.initializeVenuesFirstTime();
            // Set to false in MainActivity to reuse it for initial tutorial
        }
    }


    private void updateBandsFromApi() {
        bandInteractor.getBands(new BandInteractor.BandsCallback() {
            @Override
            public void onResponse(List<Band> bands) {

                updateVenuesFromApi();


//                int maxId = 0;
//                for (Band band : bands) {
//                    if (band.getId() > maxId) {
//                        maxId = band.getId();
//                    }
//                }
//
//                Log.i(TAG, "onResponse: Max id: " + maxId);
//                sendUpdateDataBroadcast();

            }

            @Override
            public void onError(String error) {
                FirebaseCrash.report(new Error("Error updating bands from API: " + error));
                Toast.makeText(App.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateVenuesFromApi() {
        venueInteractor.getVenuesApi(new VenueInteractor.VenuesCallback() {
            @Override
            public void onResponse(List<Venue> venues) {

                EventInteractor.resetStarredState();
                sendUpdateDataBroadcast();
            }

            @Override
            public void onError(String error) {
                FirebaseCrash.report(new Error("Error updating venues from API: " + error));
                Toast.makeText(App.this, error, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void sendUpdateDataBroadcast() {
        sendBroadcast(new Intent(ACTION_REFRESH_DATA));
    }

    private void configureRealm() {

        Realm.init(this);

        RealmConfiguration.Builder configBuilder = new RealmConfiguration.Builder()
//                .name("myrealm.realm")
//                .encryptionKey(getKey())
                .schemaVersion(org.alcalaesmusica.app.MyRealmMigration.VERSION)
//                .modules(new MySchemaModule())
                .migration(new org.alcalaesmusica.app.MyRealmMigration())
                ;

        if (BuildConfig.DEBUG) {
            configBuilder.deleteRealmIfMigrationNeeded();
        }

        RealmConfiguration config = configBuilder.build();

        Realm.setDefaultConfiguration(config);
    }


    public static SharedPreferences getPrefs(Context context) {
//        return new SecurePreferences(context);
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

}
