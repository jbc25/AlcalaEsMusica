package org.alcalaesmusica.app.ui.splash;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import org.alcalaesmusica.app.App;
import org.alcalaesmusica.app.R;
import org.alcalaesmusica.app.base.BaseInteractor;
import org.alcalaesmusica.app.base.BasePresenter;
import org.alcalaesmusica.app.interactor.EventInteractor;
import org.alcalaesmusica.app.interactor.SettingsInteractor;
import org.alcalaesmusica.app.interactor.VenueInteractor;
import org.alcalaesmusica.app.ui.intro.IntroPresenter;
import org.alcalaesmusica.app.ui.program.ProgramPresenter;
import org.alcalaesmusica.app.views.DialogShowNotification;

import static org.alcalaesmusica.app.App.ACTION_SHOW_NOTIFICATION;
import static org.alcalaesmusica.app.App.EXTRA_FIRST_TIME_APP_LAUNCHING;
import static org.alcalaesmusica.app.App.SHARED_DATA_VERSION;

/**
 * Created by julio on 29/05/17.
 */


 public class SplashPresenter extends BasePresenter {

    public static final String EXTRA_NOTIFICATION_TITLE = "extra_notif_title";
    public static final String EXTRA_NOTIFICATION_MESSAGE = "extra_notif_message";
    public static final String EXTRA_NOTIFICATION_CUSTOM_BUTTON_TEXT = "extra_notif_custom_button_text";
    public static final String EXTRA_NOTIFICATION_CUSTOM_BUTTON_LINK = "extra_notif_custom_button_link";

    private final SplashView view;
    private Handler handler;
    private boolean launchMainActivity = true;
    private SettingsInteractor settingsInteractor;
    private VenueInteractor venueInteractor;

    public static Intent newSplashActivity(Context context) {

         Intent intent = new Intent(context, SplashActivity.class);

         return intent;
     }

     public static SplashPresenter newInstance(SplashView view, Context context) {

         return new SplashPresenter(view, context);

     }

     private SplashPresenter(SplashView view, Context context) {
         super(context, view);

         this.view = view;

     }

    private BroadcastReceiver receiverRefreshData = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(App.ACTION_REFRESH_DATA)) {

                // update data from api finished
                launchNextScreen();
            }
        }
    };

    private Runnable runnableNextScreen = new Runnable() {
        @Override
        public void run() {
            // If connection failed, launch it anyway
            launchNextScreen();
        }
    };

     public void onCreate(Intent intent) {

         handler = new Handler();

         venueInteractor = new VenueInteractor(context, view);
         settingsInteractor = new SettingsInteractor(context, view);

         if (intent.getAction().equals(ACTION_SHOW_NOTIFICATION)) {

             launchMainActivity = false;
             String title = intent.getStringExtra(EXTRA_NOTIFICATION_TITLE);
             String message = intent.getStringExtra(EXTRA_NOTIFICATION_MESSAGE);
             String btnText = intent.getStringExtra(EXTRA_NOTIFICATION_CUSTOM_BUTTON_TEXT);
             String btnLink = intent.getStringExtra(EXTRA_NOTIFICATION_CUSTOM_BUTTON_LINK);

             DialogShowNotification.newInstace(context).show(title, message, btnText, btnLink);
         }

     }

    public void onResume() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(App.ACTION_REFRESH_DATA);
        context.registerReceiver(receiverRefreshData, intentFilter);

//         handler.postDelayed(runnableNextScreen, 5000);
        checkAndUpdateDataFromApi();
    }

    public void onPause() {
        handler.removeCallbacks(runnableNextScreen);
        context.unregisterReceiver(receiverRefreshData);
    }

    private void checkAndUpdateDataFromApi() {

        settingsInteractor.getDataVersion(new SettingsInteractor.SettingsVersionCallback() {

            @Override
            public void onResponse(Integer version) {

                int localDataVersion = getPrefs().getInt(SHARED_DATA_VERSION, 0);
                if (localDataVersion < version) {
                    view.showLoadingDataMessage(context.getString(R.string.loading_data));
                    updateDataFromApi(version);
                } else {
                    launchMainActivity();
                }
            }

            @Override
            public void onError(String error) {

                Log.i(TAG, "onError: " + error);

            }
        });
    }

    private void updateDataFromApi(int newVersion) {

        venueInteractor.getVenuePalomaInfoApi(new BaseInteractor.BaseCallback() {
            @Override
            public void onSuccess() {

                getPrefs().edit().putInt(SHARED_DATA_VERSION, newVersion).commit();
                EventInteractor.resetStarredState();
                launchMainActivity();
            }

            @Override
            public void onError(String error) {
                FirebaseCrash.report(new Error("Error updating venues from API: " + error));
                view.toast(error);

            }
        });
    }




     private void launchNextScreen() {

         if (getPrefs().getBoolean(EXTRA_FIRST_TIME_APP_LAUNCHING, true)) {
             launchIntroActivity();
         } else {
             launchMainActivity();
         }
     }

    private void launchIntroActivity() {
        context.startActivity(IntroPresenter.newIntroActivity(context));
        ((Activity)context).finish();
        ((Activity)context).overridePendingTransition(0,0);
    }

    private void launchMainActivity() {

        if (launchMainActivity) {
            context.startActivity(ProgramPresenter.newMainActivity(context));
            ((Activity)context).finish();
            ((Activity)context).overridePendingTransition(0,0);
        }
    }

 }
